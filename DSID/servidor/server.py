import sys
from part_repository import PartRepository
from xmlrpc.server import SimpleXMLRPCServer

# SERVER CONFIGS
IP = '127.0.0.1'
PORTA = 420


def bootstrap_parts(repo:PartRepository):
    repo.create_part("Rebimboca da Parafuseta", "Aquilo que retorna o turbo encabulador")
    repo.create_part("Turbo Encabulador", "The original machine had a base plate of prefabulated amulite, surmounted by a malleable logarithmic casing in such a way that the two spurving bearings were in a direct line with the panametric fan.")
    repo.create_part("Chave Desbloqueadora", "IHULLLLLLLLL")
    
def main(args):
    servidor = SimpleXMLRPCServer((IP, PORTA), allow_none=True)
    print('\tParts Server')
    print("Esperando por conexões...")

    repo = PartRepository("tester")

    # Cria uma base de dados mock
    bootstrap_parts(repo)

    servidor.register_instance(repo)



    servidor.serve_forever()




if __name__ == "__main__":
    # TODO: Verify if all args are good to go
    main(sys.argv[1:])