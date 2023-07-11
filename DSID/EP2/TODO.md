# Comandos do client

- bind <nome agência>
Conecta à uma agência cliente

- unbind
Desconecta a agência

- quit
Sai do client (lol)

- context
Mostra quais são as variáveis no contexto atual (agência conectada, agente atual, host ideal atual)

VVV Daqui pra baixo, tem que ter certeza de que está conectado em uma agência VVV 

- createagent <nome agente> 
Cria um agente e coloca como o atual no contexto, pede pra ler um .java e argumentos (pode ser em .txt, outro .java, sei lá), e cria um worker. Se criar e ja tiver um, vai sobrescrever.
    - Ler o .java assim: https://stackoverflow.com/questions/40255039/how-to-choose-file-in-java

VVV Daqui pra baixo, tem que ter certeza de que tem um agente no contexto VVV 

- requesthost
Vai até o servidor de nomes, chama um seeker, passa as especificações, e espera ele retornar com um host e adiciona ao contexto ou 
que não achou uma máquina 

- sendagent
Adiciona o host encontrado ao agente atual, manda para o host e espera ele voltar com os resultados

- finishagent <método (print ou save)> 
Retorna os valores calculados pelo agente (jogando na tela ou salvando o retorno e logs em .txt) e deleta o agente atual


# Algumas constraints 

- Deve ter apenas uma agência de seekers, que o servidor de nomes deve criar.
- Deve ter prints explicando oq o programa está fazendo no momento no client (usar os prints coloridos do Utils.java)
- Se quiser ter uma ideia de como fazer o client, usa esse como base: https://github.com/amandaperellon/DSID-EP1
    - Ou no nosso repo, procura pelo EP1, veja como foi feito e traduz pra java (não é muito dificil de entender python, juro)