import sys
from part_repository import PartRepository
from xmlrpc.server import SimpleXMLRPCServer

# SERVER CONFIGS
IP = '127.0.0.1'

def main(args):
    servidor = SimpleXMLRPCServer((IP, PORTA))
    print('\tParts Server')
    print("Esperando por conexões...")

    repo = PartRepository(args[0])

    # Cria uma base de dados mock
    bootstrap_parts(repo)


    servidor.register_instance(repo)
    servidor.serve_forever()


if __name__ == "__main__":
    # TODO: Verify if all args are good to go
    main(sys.argv[1:])