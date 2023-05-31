from actionconsole import ActionConsole

print('\tCLIENTE')
print("'help' para lista de comandos")
# Cria a interface para comunicar com o servidor
console = ActionConsole()
effect = ""

# Loop de comandos para serem interpretados
while effect != 'quit':
	command = input("\n>> ")
	effect = console.run_command(command)
	