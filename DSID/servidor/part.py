
class Part:
    def __init__(self, id:str, name:str, desc:str, subpart:Part=[]) -> None:
        self.id = id
        self.name = name
        self.description = desc
        # Uma subpart é um array de tuplas (subpart, quantity)
        self.subpart = subpart
 
