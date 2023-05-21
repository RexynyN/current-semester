from part import Part

class PartRepository:
    def __init__(self, repo_name) -> None:
        self.name = repo_name
        self.parts = { }
        self.length = 0

    def repo_info(self):
        '''Examinando o nome do repositorio e o numero de pecas nele contidas'''
        return (self.name, self.length)

    def insert_part(self, part:Part):
        '''Listando as pecas no repositorio'''
        self.parts[part.get_id()] = part
        
    def list_parts(self):
        return self.parts.values()

    def search_part(self, key):
        if key in self.parts.keys:
            return self.parts[key]
        else:
            return None

    def repo_part(self, id):
        return self.name

    def part_type(self, id):
        if not id in self.parts.keys():
            return None
        part = self.parts[id]
        return "primitive" if part.subparts == [] else "aggregated"
    
    # Samsung S6 Lite        
    def subparts_info(self, id):
        if not id in self.parts.keys():
            return None
        part = self.parts[id]
        for key, value in self.parts.items():
            # TODO
            pass

    def list_subparts(self):
        if not id in self.parts.keys():
            return None
        
        return self.parts[id].subparts
    
    def create_part(self, data: dict):
        if not ["id", "name", "desc", "subparts"] in data.keys():
            return None
        
        new_part = Part(data["id"], data['name'], data['desc'], data['subparts'])

        self.parts[new_part.id] = new_part
        return new_part