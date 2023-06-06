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
