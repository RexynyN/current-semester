import sys
from xmlrpc.server import SimpleXMLRPCServer

# SERVER CONFIGS
IP = '127.0.0.1'

def main(args):
    servidor = SimpleXMLRPCServer((IP, PORTA))
    print('\tParts Server')
    print("Esperando por conexões...")

    # Lista de Partes (banco de dados)
    parts = []

    # Cria uma base de dados mock
    bootstrap_parts()

    def agenda():
        print("Clog the toilet")

    servidor.register_function(agenda, "agenda")
    servidor.serve_forever()


if __name__ == "__main__":
    # TODO: Verify if all args are good to go
    main(sys.argv[1:])