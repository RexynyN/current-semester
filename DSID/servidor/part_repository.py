import random
from part import Part

class PartRepository:
    def __init__(self, repo_name) -> None:
        # Nome do repo
        self.name = repo_name
        # Dicionário com todas as partes do repo
        self.parts = { }
        # Número de partes
        self.length = 0

    # Um método dummy para validar a conexão no cliente
    def handshake(self):
        return True

    # Retorna as informações do repositório
    def repo_info(self):
        return (self.name, self.length)

    # Lista todas as partes no repositório        
    def list_parts(self):
        return list(self.parts.values())

    # Procura a parte por ID
    def search_part(self, key):
        return self.parts.get(key, None)
    
    # Retorna o tipo de peça com base no ID
    def part_type(self, id):
        if not id in self.parts.keys():
            return None
        part = self.parts[id]
        return "primitive" if part.subparts == [] else "aggregated"
    
    # Insere novas subpartes na parte dada 
    def add_subpart(self, id_part, subparts):
        if not id_part in self.parts.keys():
            return None
        
        # Checa se há duplicidade de subpartes
        for item in subparts:
            dup = False
            for sub in self.parts[id_part].subparts:
                if sub[0] == item[0]:
                    sub[1] += item[1]
                    dup = True 
    
            if not dup:
                self.parts[id_part].subparts.append(item)
        
        return self.parts[id_part]

    # Cria uma nova parte com subparte
    def create_part(self, name, desc, subparts) -> Part:
        id = str(random.randint(100,99999))

        new_part = Part(id, name, desc, subparts, self.name)
        self.parts[new_part.id] = new_part
        self.length += 1
        return new_part



infoprint("servers => Lista o nome de todos os servidores registrados")
infoprint("bind => Se conecta a um servidor usando o seu nome")
infoprint("unbind => Desconecta do servidor atual")
infoprint("repo => Lista as informações do repositório atual")
infoprint("listp => Lista todas as partes do repositório")
infoprint("getp => Coloca a peça com o ID passado no contexto atual")
infoprint("showp => Mostra as informações da peça atual")
infoprint("seekp => Retorna a peça com ID passado, procurando por todos os servidores registrados")
infoprint("subp => Lista todas as subpartes da peça atual")
infoprint("addp => Cria uma nova peça, e adiciona as subpeças atuais (se houver alguma)")
infoprint("showsub => Mostra a lista de subpeças no contexto atual")
infoprint("clearlist => Apaga a lista de subpeças atual")
infoprint("addsub => Adiciona a peça atual na lista de subpeças")
infoprint("appendsub => Adiciona todas as subpeças da lista atual na peça atual")
infoprint("quit => Sai da aplicação")
