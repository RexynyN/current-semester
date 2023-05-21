from part import Part

class Part:
    def __init__(self, id:str, name:str, desc:str, subparts:Part=[]) -> None:
        self.id = id
        self.name = name
        self.description = desc
        # Uma subpart é um array de tuplas (subpart, quantity)
        self.subparts = subparts
 
