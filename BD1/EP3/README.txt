EP 3 - Banco de Dados 1 

Breno da Silva Nogueira - 12400392
Luigi Tramontim de Paula - 12727987
Juliana Almeida Santos - 12566178

-> Instruções para o EP

-> Arquivo .sql
    - O arquivo está estruturado em 4 partes, devidamente comentadas, da seguinte maneira:
        1 - Criação das tabelas
        2 - Exercício 1 (Triggers, Checks, Rules)
        3 - Inserts para testes
        4 - Exercício 2 (Querys usadas para fazer a alternativa B)


-> Para rodar o programa é necessário:

    - Python 3 (Feito no python 3.10.6)

    - Instalar as dependência por meio do pip (rodar comando abaixo)
        - pip install psycopg2 psycopg2-binary seaborn matplotlib pandas
    
    - Se estiver no linux, convém instalar essas duas dependências para que não haja erros:
        - sudo apt install libpq-dev python3-dev
        - Referência: https://stackoverflow.com/questions/5420789/how-to-install-psycopg2-with-pip-on-python 

    - Criar o banco de dados postgres anexo no EP com o nome de "ConflitosBelicos" ou mudar nas 
    configurações do psycopg2 (linha 12 do arquivo) o nome do banco de dados 

    - Na linha 11 do arquivo ep3.py mudar para a senha do banco de dados local

    - Para rodar o programa, digite o seguinte comando 
        - python ep3.py 
        - python3 ep3.py (se estiver no linux)