import json
import random
import sys
from part_repository import PartRepository
from xmlrpc.server import SimpleXMLRPCServer

# SERVER CONFIGS
IP = '127.0.0.1'

def bootstrap_parts(repo:PartRepository, servername):
    with open("servers.json", 'r', encoding="utf-8") as f:
        servers = json.load(f)
    
    i = 0
    for index, ser in enumerate(servers):
        if ser['name'] == servername:
            i = index

    for peca in servers[i]['data']:
        repo.create_part(peca['name'], peca['desc'], peca['subparts'])
    
def lookup_server(servername):
    with open("servers.json", 'r', encoding="utf-8") as f:
        servers = json.load(f)
    
    for ser in servers:
        if ser['name'] == servername:
            return ser['link']
        
    return None

def main(args):
    if len(args) < 1:
        print("Não há argumentos o suficiente!")
        print("<nome servidor>")
        return
    
    server_link = lookup_server(args[0])
    if not server_link:
        print("Não há nenhum servidor registrado com o nome passado!")
        return 

    ip = server_link.split(":")[-2].replace("//", "")
    port = server_link.split(":")[-1]


    servidor = SimpleXMLRPCServer((ip, int(port)), allow_none=True)
    print('\tParts Server')
    print("Esperando por conexões...")

    repo = PartRepository(args[0])

    # Cria uma base de dados mock, 
    bootstrap_parts(repo, args[0])

    servidor.register_instance(repo)

    servidor.serve_forever()


if __name__ == "__main__":
    main(sys.argv[1:])
