
class ActionConsole:
    def __init__(self) -> None:
        pass

    def run_command(self, command:str) -> str:
        return self.__switch_function(command)

    def __switch_function(self, cmd:str) -> str:
        verb = cmd.split()[0] 
        match verb:
            case "bind":
                return 
            
            case "listp":
                return 
            
            case "getp":
                return 
            
            case "showp":
                return 
            
            case "clearlist":
                return 
            
            case "addsubpart":
                return 

            case "addp":
                return 
            
            case "help":
                return 
            
            case "quit":
                return 
            
            case _:
                print("Comando não reconhecido!")
                return "error"

        

