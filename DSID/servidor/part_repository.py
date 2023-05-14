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

    def repo_part(self):
        pass

    def part_type(self):
        pass

    def subparts_info(self):
        pass

    def list_subparts(self):
        pass