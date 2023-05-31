HEADER = '\033[95m'
OKBLUE = '\033[94m'
OKCYAN = '\033[96m'
OKGREEN = '\033[92m'
WARNING = '\033[93m'
FAIL = '\033[91m'
ENDC = '\033[0m'
BOLD = '\033[1m'
UNDERLINE = '\033[4m'

def fprint(message):
    print(FAIL + message + ENDC)

def okprint(message):
    print(OKGREEN + message + ENDC)

def infoprint(message):
    print(WARNING + message + ENDC)

def helpprint(cmd, help):
    print(OKBLUE + cmd + ENDC, WARNING + f" => {help}" + ENDC)
