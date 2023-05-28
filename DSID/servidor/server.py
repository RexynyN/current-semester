import json
import sys
from part_repository import PartRepository
from xmlrpc.server import SimpleXMLRPCServer

# SERVER CONFIGS
IP = '127.0.0.1'

def bootstrap_parts(repo:PartRepository, bobo=2):
    if bobo == 1:
        repo.create_part("Rebimboca da Parafuseta", "Aquilo que retorna o turbo encabulador", [])
        repo.create_part("Turbo Encabulador", "The original machine had a base plate of prefabulated amulite, surmounted by a malleable logarithmic casing.", [])
        repo.create_part("Chave Desbloqueadora", "IHULLLLLLLLL", [])
    elif bobo == 2:
        repo.create_part("Blinkenlights", "Blinkenlights is a neologism for diagnostic lights usually on the front panels of old mainframe computers", [])
        repo.create_part("Thiotimoline", "Thiotimoline is a fictitious chemical compound conceived by American biochemist and science fiction author Isaac Asimov.", [])
        repo.create_part("Unobtainium", "Unobtainium is a term used in fiction, engineering, and common situations for a material ideal for a particular application but impractically hard to get.", [])
        repo.create_part("Write-only memory", "Write-only memory (WOM), the opposite of read-only memory (ROM), began as a humorous reference to a memory device that could be written to but not read", [])
    else:
        return
    
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

    # Cria uma base de dados mock, bobo é 1 ou 2
    bootstrap_parts(repo, bobo=2)

    servidor.register_instance(repo)

    servidor.serve_forever()


if __name__ == "__main__":
    main(sys.argv[1:])
