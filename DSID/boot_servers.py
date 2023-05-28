import json
import platform
import subprocess 

print("CTRL + C para parar os servidores")

with open("servers.json", 'r', encoding="utf-8") as f:
    servers = json.load(f)

if platform.system() == "Linux":
    CMD = "sudo python3"
elif platform.system() == "Windows":
    CMD = "python" 

for s in servers:
    port = s['link'].split(":")[-1]
    cmd = CMD + f" servidor/server.py {port} {s['name']} {0}"
    p = subprocess.Popen(cmd, shell=True, close_fds=True)
    p.wait()


