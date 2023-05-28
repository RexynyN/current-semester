import json
import xmlrpc.client as xc
from colors import fprint, okprint
from os.path import join

class ActionConsole:
    def __init__(self) -> None:
        self.connection = None
        self.part = None
        self.subpart = []

        with open(join("servers.json"), 'r', encoding="utf-8") as f:
            self.servers = json.load(f)        

 
    def run_command(self, command:str) -> str:
        cmd_free = ["servers", "bind", "help", "quit"]
        if command.split()[0] not in cmd_free and self.connection == None:
            fprint("Não há uma conexão com um repositório para executar esse comando!")
            return "failed"
         
        return self.__switch_function(command)

    def __switch_function(self, cmd:str) -> str:
        verb = cmd.split()[0] 
        args = cmd.split()[1:]
        
        match verb:
            case "servers":
                for s in self.servers:
                    print(s['name'])

                return "fulfilled"

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
            
            case "unbind":
                self.connection = None
                okprint("Conexão com o host fechada.")
                return "fulfilled"
            
            case "repo":
                request = self.connection.repo_info()
                print(f"Repositório Atual: {request[0]}")
                print(f"Número de Partes: {request[1]}")
                return "fulfilled"

            case "listp":
                parts = self.connection.list_parts()

                if parts == []:
                    print("Este repositório não possui partes")
                
                for item in parts:
                    print(item['id'], " => ", item['name'])
                return "fulfilled"
            
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
                
            
            case "showp":
                if not self.part:
                    fprint("Não há uma parte contexto atual, dê 'getp <id>' para procurar uma parte e retorná-la")
                    return "failed"
                
                self.show_part()
                return "fulfilled"

            case "showsub":
                if self.subpart == []:
                    fprint("Não há subpartes no contexto atual, dê 'getsp <id>' para procurar uma parte e retorná-la")
                    return "failed"
                
                self.show_subparts()
                return "fulfilled"  
            
            case "clearlist":
                if not self.subpart:
                    fprint("Não há uma subparte no contexto atual, dê 'getp <id>' para procurar uma parte e retorná-la")
                    return "failed"
                
                if self.subpart == []:
                    fprint("Não há subpartes para serem apagadas.")
                    return "failed"
                
                try:
                    response = self.connection.clear_subparts(self.part['id'])
                except Exception as e:
                    fprint(f"Houve um erro ao processar sua requisição pelo servidor: {e}")
                    return "failed"
                
                if not response:
                    fprint("Houve um conflito de Id's no servidor, tenha certeza que a peça é desse repositório")
                    return "failed"
                
                self.subpart = response
                okprint("Subpartes apagadas com sucesso!")
                return "fulfilled" 
            
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

            case "typep":
                if not self.part:
                    fprint("Não há uma parte contexto atual, dê 'getp <id>' para procurar uma parte e retorná-la")
                    return "failed"
                
                if self.part['subparts'] == []:
                    print("A peça é primitiva")
                else:
                    print("A peça é agregada")
                return "fulfilled"

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

            case "addp":
                name = input('\nNome da peça: \n')
                desc = input('\nDescrição da peça: \n')

                new_part = self.connection.create_part(name, desc, self.subpart)
                self.part = new_part

                okprint(f"\nPeça criada! ID de peça: {new_part['id']}")
                return "fulfilled"
            
            case "help":
                # TODO
                return 
            
            case "quit":
                return "quit"
            
            case _:
                fprint("Comando não reconhecido!")
                return "error"

    def lookup_server(self, servername):
        for ser in self.servers:
            if ser['name'] == servername:
                return ser['link']
            
        return None


    def show_subparts(self):
        for sub, qtd in self.subpart:
            part_s = self.connection.search_part(sub)

            if not part_s:
                part_s = "<Parte em outro repositório>"
            else:
                part_s = part_s['name']
            print(f"Id => {sub} [{part_s}] | Qtd => {qtd}")
    
    def show_part(self, part=None):
        if part:
            show = part
        else:
            show = self.part    

        print(f"Repo Origem => {show['repo']}\n")
        print(f"Id => {show['id']}")
        print(f"Nome => {show['name']}")
        print(f"Descrição => {show['description']}")
        print("Subpartes: ")

        if show['subparts'] == []:
            print("\t-")
            return 
                
        for sub, qtd in show['subparts']:
            part_s = self.connection.search_part(sub)

            if not part_s:
                part_s = "<Parte em outro repositório>"
            else:
                part_s = part_s['name']

            print(f"\tId => {sub} [{part_s}] | Qtd => {qtd}")        
