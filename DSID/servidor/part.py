
class Part:
    def __init__(self, id:str, name:str, desc:str, subpart:Part=None) -> None:
        self.id = id
        self.name = name
        self.description = desc
        self.subpart = subpart
 