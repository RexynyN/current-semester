import xmlrpc.client
from actionconsole import ActionConsole

print('\tCLIENTE')

IP = input('- Digite o IP do Servidor: ')
PORTA = int(input('- Digite a PORTA: '))

servidor = xmlrpc.client.ServerProxy("http://{0}:{1}/".format(IP, PORTA))

console = ActionConsole()
while effect != 'quit':
	command = input(">")
	effect = console.run_command(command)
	