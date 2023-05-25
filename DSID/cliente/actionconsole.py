import xmlrpc.client as xc
from pprint import pprint

# bind 127.0.0.1 420

class ActionConsole:
    def __init__(self) -> None:
        self.connection = None
        self.part = None
        self.subpart = None

    def run_command(self, command:str) -> str:
        cmd_free = ["bind", "help", "quit"]
        if command.split()[0] not in cmd_free and self.connection == None:
            print("Não há uma conexão com um repositório para executar esse comando!")
            return "failed"
         
        return self.__switch_function(command)

    def __switch_function(self, cmd:str) -> str:
        verb = cmd.split()[0] 
        args = cmd.split()[1:]
        
        match verb:
            case "bind":
                if ":" in args[0]:
                    con_string = f"http://{args[0]}/"
                elif len(args) > 1:
                    con_string = f"http://{args[0]}:{args[1]}/"

                try:
                    self.connection = xc.ServerProxy(con_string, allow_none=True)
                except:
                    print(f"Erro ao conectar com o servidor no endereço {con_string}")
                    return "failed"
                
                print(f"Conectado com sucesso no host {con_string}")
                return "fulfilled"
            
            case "unbind":
                self.connection = None

                return "fulfilled"
            
            case "repo":
                request = self.connection.repo_info()
                print(f"Repositório Atual: {request[0]}")
                print(f"Número de Partes: {request[1]}")
                return "fulfilled"


            case "listp":
                parts = self.connection.list_parts()
                
                for item in parts:
                    print(item['id'], " => ", item['name'])
                return "fulfilled"
            
            case "getp":
                if args == []:
                    print("Nenhum Id passado para busca!")
                    return "failed"
                
                id = args[0].strip()
                if not id.isdigit():
                    print("Id inválido!")
                    return "failed"
                
                try:
                    self.part = self.connection.search_part(id)
                    
                    if not self.part:
                        print(f"Peça não encontrada no repositório atual ({self.connection.repo_info()[0]})")
                        return "failed"
                except Exception as e:
                    print(f"Houve um erro ao processar sua requisição pelo servidor: {e}")
                    return "failed"
                
                self.show_part()
                return "fulfilled" 
            
            case "showp":
                if not self.part:
                    print("Não há uma parte contexto atual, dê 'getp <id>' para procurar uma parte e retorná-la")
                    return "failed"
                
                self.show_part()
                return "fulfilled"

            case "getsp":
                if args == []:
                    print("Nenhum Id passado para busca!")
                    return "failed"
                
                id = args[0].strip()
                if not id.isdigit():
                    print("Id inválido!")
                    return "failed"
                
                try:
                    self.subpart = self.connection.search_part(id)
                    
                    if not self.subpart:
                        print(f"Subpeça não encontrada no repositório atual ({self.connection.repo_info()[0]})")
                        return "failed"
                except Exception as e:
                    print(f"Houve um erro ao processar sua requisição pelo servidor: {e}")
                    return "failed"
                
                self.show_part(sub=True)
                return "fulfilled" 
            
            case "showsp":
                if not self.subpart:
                    print("Não há uma subparte contexto atual, dê 'getsp <id>' para procurar uma parte e retorná-la")
                    return "failed"
                
                self.show_part(sub=True)
                return "fulfilled"  
            
            case "clearlist":
                if not self.subpart:
                    print("Não há uma subparte no contexto atual, dê 'getp <id>' para procurar uma parte e retorná-la")
                    return "failed"
                
                if self.subpart['subparts'] == []:
                    print("Não há subpartes para serem apagadas.")
                    return "failed"
                
                try:
                    response = self.connection.clear_subparts(self.part['id'])
                except Exception as e:
                    print(f"Houve um erro ao processar sua requisição pelo servidor: {e}")
                    return "failed"
                
                if not response:
                    print("Houve um conflito de Id's no servidor, tenha certeza que a peça é desse repositório")
                    return "failed"
                
                self.subpart = response
                print("Subpartes apagadas com sucesso!")
                return "fulfilled" 
            
            case "addsubpart":
                if len(args) < 1:
                    print("Número de peças não passado!")
                    return "failed"
                
                if not self.subpart or not self.part:
                    print("Não há uma parte ou subparte no contexto atual, procure por ambas e tente novamente!")
                    return "failed"

                try:
                    n_parts = int(args[0])
                except:
                    print("Número de peças em formato inválido!")
                    return "failed"

                response = self.connection.add_subpart(self.part['id'], self.subpart['id'], n_parts)
                if not response:
                    print("Houve um conflito de Id's no servidor, tenha certeza que a peça é desse repositório")
                    return "failed"
                
                self.part = response
                self.show_part()
                return "fulfilled"

            case "addp":
                name = input('Nome da peça: \n')

                desc = input('\nDescrição da peça: \n')

                new_part = self.connection.create_part(name, desc)
                self.part = new_part
                print(f"Peça criada! ID de número: {new_part['id']}")
                return "fulfilled"
            
            case "help":
                return 
            
            case "quit":
                return "quit"
            
            case _:
                print("Comando não reconhecido!")
                return "error"

        
    def show_part(self, sub=False):
        if sub:
            show = self.subpart
        else:
            show = self.part

        print(f"Id => {show['id']}")
        print(f"Nome => {show['name']}")
        print(f"Descrição => {show['description']}")
        print("Subpartes: ")

        if show['subparts'] != []:
            for sub, qtd in show['subparts']:
                part_s = self.connection.search_part(sub)

                if not part_s:
                    part_s = "Parte em outro repositório"
                else:
                    part_s = part_s['name']
                print(f"\tId => {sub} [{part_s}] | Qtd => {qtd}")        
        else:
            print("\t-")