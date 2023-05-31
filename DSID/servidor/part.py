
class Part:
    def __init__(self, id:str, name:str, desc:str, subparts:list, repo: str) -> None:
        # ID da peça
        self.id = id
        # Nome da peça
        self.name = name
        # Descrição da peça
        self.description = desc
        # Uma subpart é um array de tuplas (subpart, quantity)
        self.subparts = subparts
        # Repo que ele pertence
        self.repo = repo
 
