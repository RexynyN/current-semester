import json
import xmlrpc.client as xc
from colors import fprint, okprint, infoprint, helpprint
from os.path import join

class ActionConsole:
    def __init__(self) -> None:
        # Conexão com o servidor atual
        self.connection = None
        # Parte do contexto atual
        self.part = None
        # Lista de subpartes atual
        self.subpart = []

        with open(join("servers.json"), 'r', encoding="utf-8") as f:
            # Lista de servidores
            self.servers = json.load(f)        

 
    # Valida o comando e roda o switch para interpretar o comando
    def run_command(self, command:str) -> str:
        cmd_free = ["servers", "bind", "help", "quit"]
        if command.split()[0] not in cmd_free and self.connection == None:
            fprint("Não há uma conexão com um repositório para executar esse comando!")
            return "failed"
         
        return self.__switch_function(command)

    def __switch_function(self, cmd:str) -> str:
        # Pega o verbo e argumentos do comando
        verb = str(cmd.split()[0]).lower()
        args = cmd.split()[1:]
        
        match verb:
            # Lista o nome de todos os servidores registrados
            case "servers":
                for s in self.servers:
                    print(s['name'])

                return "fulfilled"
            
            # Se conecta a um servidor usando o seu nome
            case "bind":
                if len(args) < 1:
                    fprint("Não há um nome para conectar!")
                    return "failed"
                
                con_string = self.lookup_server(args[0])

                if not con_string:
                    fprint("Não há um servidor com esse nome!")
                    return "failed"

                try:
                    self.connection = xc.ServerProxy(con_string, allow_none=True)
                    self.connection.handshake()
                except:
                    fprint(f"Erro ao conectar com o servidor '{args[0]}' no endereço {con_string}. O servidor pode estar indisponível.")
                    return "failed"
                
                okprint(f"Conectado com sucesso no host '{args[0]}'")
                return "fulfilled"
            
            # Desconecta do servidor atual
            case "unbind":
                if not self.connection:
                    fprint("Não há conexão para fechar")

                self.connection = None
                okprint("Conexão com o host fechada.")
                return "fulfilled"

            # Lista as informações do repositório atual            
            case "repo":
                request = self.connection.repo_info()
                infoprint(f"\nRepositório Atual => {request[0]}")
                infoprint(f"Número de Partes => {request[1]}")
                return "fulfilled"

            # Lista todas as partes do repositório
            case "listp":
                parts = self.connection.list_parts()

                print()
                if parts == []:
                    fprint("Este repositório não possui partes")
                
                for item in parts:
                    infoprint(f"{item['id']} => {item['name']}")
                return "fulfilled"
            
            # Coloca a peça com o ID passado no contexto atual
            case "getp":
                if args == []:
                    fprint("Nenhum Id passado para busca!")
                    return "failed"
                
                id = args[0].strip()
                if not id.isdigit():
                    fprint("Id inválido!")
                    return "failed"
                
                try:
                    response = self.connection.search_part(id)
                    
                    if not response:
                        fprint(f"Peça não encontrada no repositório atual ({self.connection.repo_info()[0]})")
                        return "failed"

                    self.part = response
                except Exception as e:
                    fprint(f"Houve um erro ao processar sua requisição pelo servidor: {e}")
                    return "failed"
                
                self.show_part()
                return "fulfilled"

            # Lista todas as subpartes da peça atual (se houver alguma)
            case "subp":
                if not self.part:
                    fprint("Não há uma parte no contexto atual, dê 'getsp <id>' para procurar uma parte e retorná-la")
                    return "failed"
                
                self.show_subparts(self.part['subparts'])
                return "fulfilled"       
            
            # Retorna a peça com ID passado, procurando por todos os servidores registrados
            case "seekp":
                if args == []:
                    fprint("Nenhum Id passado para busca!")
                    return "failed"
                
                id = args[0].strip()
                if not id.isdigit():
                    fprint("Id inválido!")
                    return "failed"
                
                for server in self.servers:
                    try:
                        aux_connection = xc.ServerProxy(server['link'], allow_none=True)
                        part = aux_connection.search_part(id)

                        if part:
                            self.show_part(part)
                            return "fulfilled"

                    except:
                        continue
                
                fprint("A peça não foi encontrada em nenhum servidor registrado")
                return "failed"
                
            # Mostra as informações da peça atual
            case "showp":
                if not self.part:
                    fprint("Não há uma parte contexto atual, dê 'getp <id>' para procurar uma parte e retorná-la")
                    return "failed"
                
                self.show_part()
                return "fulfilled"

            # Mostra a lista de subpeças no contexto atual
            case "showsub":
                if self.subpart == []:
                    fprint("Não há subpartes no contexto atual, dê 'getsp <id>' para procurar uma parte e retorná-la")
                    return "failed"
                
                self.show_subparts()
                return "fulfilled"  
            
            # Apaga a lista de subpeças atual
            case "clearlist":
                if self.subpart == []:
                    fprint("Não há subpartes para serem apagadas.")
                    return "failed"
 
                self.subpart = []
                okprint("Subpartes apagadas com sucesso!")
                return "fulfilled" 
            
            # Adiciona a peça atual na lista de subpeças
            case "addsub":
                if not self.part:
                    fprint("Não há uma parte no contexto atual, procure por ambas e tente novamente!")
                    return "failed"
                
                if len(args) < 1:
                    fprint("Número de peças não passado!")
                    return "failed"
                
                if not args[0].isdigit():
                    fprint("Número de peças em formato errado!")
                    return "failed"
                
                part_tuple = (self.part['id'], int(args[0]))
                self.subpart.append(part_tuple)
                self.part = None
                okprint("Subparte adicionada na lista!")
                return "fulfilled"

            # Adiciona todas as subpeças da lista atual na peça atual
            case "appendsub":
                if not self.part or self.subpart == []:
                    fprint("Não há uma parte no contexto atual ou não há subpartes para referenciar")
                    return "failed"
                
                response = self.connection.add_subpart(self.part['id'], self.subpart)
                if not response:
                    fprint("Houve um conflito de Id's no servidor, tenha certeza que a peça é desse repositório")
                    return "failed"
                
                self.subpart = []
                self.part = response
                self.show_part()

            # Cria uma nova peça, e adiciona as subpeças atuais (se houver alguma)
            case "addp":
                name = input('\nNome da peça: \n')
                desc = input('\nDescrição da peça: \n')

                new_part = self.connection.create_part(name, desc, self.subpart)
                self.part = new_part

                okprint(f"\nPeça criada! ID de peça: {new_part['id']}")
                return "fulfilled"
            
            # Help para os fracos que não sabem usar
            case "help": 
                helpprint("servers", "Lista o nome de todos os servidores registrados")
                helpprint("bind", "Se conecta a um servidor usando o seu nome")
                helpprint("unbind", "Desconecta do servidor atual")
                helpprint("repo", "Lista as informações do repositório atual")
                helpprint("listp", "Lista todas as partes do repositório")
                helpprint("getp", "Coloca a peça com o ID passado no contexto atual")
                helpprint("showp", "Mostra as informações da peça atual")
                helpprint("seekp", "Retorna a peça com ID passado, procurando por todos os servidores registrados")
                helpprint("subp", "Lista todas as subpartes da peça atual")
                helpprint("addp", "Cria uma nova peça, e adiciona as subpeças atuais (se houver alguma)")
                helpprint("showsub", "Mostra a lista de subpeças no contexto atual")
                helpprint("addsub", "Adiciona a peça atual na lista de subpeças")
                helpprint("appendsub", "Adiciona todas as subpeças da lista atual na peça atual")
                helpprint("clearlist", "Apaga a lista de subpeças atual")
                helpprint("quit", "Sai da aplicação")
                return "fulfilled" 
            
            # Sai do cliente
            case "quit":
                return "quit"
            
            # Pra caso jogue um verbo que não existe:)
            case _:
                fprint("Comando não reconhecido!")
                return "error"

    def lookup_server(self, servername):
        for ser in self.servers:
            if ser['name'] == servername:
                return ser['link']
            
        return None

    def show_subparts(self, subp=None):
        if subp or subp == []:
            show = subp
            num = len(show)
            infoprint(f"\nNome => {self.part['name']}")
            infoprint(f"Tipo de Peça => {'Agregada' if num > 0 else 'Primitiva'} ")
            infoprint(f"Número de Subpeças => {len(show)} \n")
        else:
            show = self.subpart   
        
        for sub, qtd in show:
            part_s = self.connection.search_part(sub)

            if not part_s:
                part_s = "<Parte em outro repositório>"
            else:
                part_s = part_s['name']
            infoprint(f"Id => {sub} [{part_s}] | Qtd => {qtd}")
    
    def show_part(self, part=None):
        if part:
            show = part
        else:
            show = self.part    

        infoprint(f"\nRepo Origem => {show['repo']}\n")
        infoprint(f"Id => {show['id']}")
        infoprint(f"Nome => {show['name']}")
        infoprint(f"Descrição => {show['description']}")
        infoprint("Subpartes: ")

        if show['subparts'] == []:
            infoprint("\t-")
            return 
                
        for sub, qtd in show['subparts']:
            part_s = self.connection.search_part(sub)

            if not part_s:
                part_s = "<Parte em outro repositório>"
            else:
                part_s = part_s['name']

            infoprint(f"\tId => {sub} [{part_s}] | Qtd => {qtd}")        
