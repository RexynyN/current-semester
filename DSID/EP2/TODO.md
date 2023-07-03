# Comandos do client

- bind
Conecta à agência

- unbind
Desconecta a agência

- createagent 
Cria um agente e coloca como o atual no contexto

- createtask
Lê um .java e cria uma task dentro do agente

- sendagent 
Manda o agente atual para a agência conectada atualmente e tira  

- seekagent 
Procura o ID de agente  no servidor de nomes

- retrieveagent
Tira o agente de ID específico da agência e retorna a agência inicial

- finishagent
Retorna os valores calculados na agência e deleta o agente atual

- quit
Sai do client (lol)

# Algumas constraints 

- Deve ter apenas uma agência de seekers, que o servidor de nomes deve criar.
- 