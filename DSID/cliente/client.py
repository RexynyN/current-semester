from actionconsole import ActionConsole

print('\tCLIENTE')

console = ActionConsole()
effect = ""
while effect != 'quit':
	command = input("\n> ")
	effect = console.run_command(command)
	