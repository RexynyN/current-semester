import random
from part import Part

class PartRepository:
    def __init__(self, repo_name) -> None:
        self.name = repo_name
        self.parts = { }
        self.length = 0

    def handshake(self):
        return True
    
    def repo_info(self):
        '''Examinando o nome do repositorio e o numero de pecas nele contidas'''
        return (self.name, self.length)

    def insert_part(self, part:Part):
        '''Listando as peças no repositorio'''
        self.parts[part.get_id()] = part
        
    def list_parts(self):
        '''Listando as peças no repositório'''
        return list(self.parts.values())

    def search_part(self, key):
        '''Buscando uma peça (por código de peça) no repositório'''
        return self.parts.get(key, None)
    
    def part_type(self, id):
        if not id in self.parts.keys():
            return None
        part = self.parts[id]
        return "primitive" if part.subparts == [] else "aggregated"
    
    def add_subpart(self, id_part, subparts):
        if not id_part in self.parts.keys():
            return None
        
        for item in subparts:
            self.parts[id_part].subparts.append(item)
        return self.parts[id_part]

    def list_subparts(self):
        '''Listando suas sub-peças.'''
        if not id in self.parts.keys():
            return None
        
        return self.parts[id].subparts
    
    def clear_subparts(self, id):
        if not id in self.parts.keys():
            return None
        
        self.parts[id].subparts = []

        return self.parts[id]

    def create_part(self, name, desc, subparts) -> Part:
        '''Adicionando ao repositório novas peças (primitivas ou agregadas)'''
        id = str(random.randint(100,99999))

        new_part = Part(id, name, desc, subparts, self.name)
        self.parts[new_part.id] = new_part
        self.length += 1
        return new_part
