# EP 3 - Banco de Dados 1 

# Breno da Silva Nogueira - 12400392
# Luigi Tramontim de Paula - 12727987
# Juliana Almeida Santos - 12566178
import datetime
import os
import psycopg2
import seaborn as sns
import matplotlib.pyplot as plt
import pandas as pd

conn = psycopg2.connect(
  host="localhost",
  user="postgres",
  password="admin",
  database = "ConflitosBelicos"
)

# Limpa o terminal
def clear():
    if os.name == 'nt':
        _ = os.system('cls')
    else:
        _ = os.system('clear')

def sql_command(sql):
    try:
        cursor = conn.cursor()

        cursor.execute(sql)
        conn.commit()

        print(cursor.rowcount, " linha(s) afetadas.")
        cursor.close()

    except (Exception, psycopg2.Error) as error:
        print("Houve um erro para executar o comando: ", error)

def sql_select(sql):
    try:
        cursor = conn.cursor()
        cursor.execute(sql)

        records = cursor.fetchall()

        cursor.close()

        return records

    except (Exception, psycopg2.Error) as error:
        print("Houve um erro para executar o select: ", error)


def ex_a():
    sentinel = True 

    while sentinel:
        sentinel = False
        print("Selecione a entidade que deseja adicionar no banco de dados: ")
        print("1 - Conflito Bélico")
        print("2 - Grupo Militar")
        print("3 - Divisão de um Grupo Militar")
        print("4 - Líder Político")
        print("5 - Chefe Militar")

        choice = int(input("Sua escolha: "))
        clear()
        if choice == 1:
            insert1()
        elif choice == 2:
            insert2()
        elif choice == 3:
            insert3()
        elif choice == 4:
            insert4()
        elif choice == 5:
            insert5()
        else:
            sentinel = True
            print("Escolha inválida, tente novamente!")

def insert1():
    print("Formulário de inserção de Conflitos Bélicos")

    nome = input("Nome do Conflito: ")
    pais = input("País afetado: ")
    mortos = int(input("Número de Mortos: "))

    SQL = f"""INSERT INTO conflitos_belicos.conflitos 
    VALUES (DEFAULT, '{nome}', '{pais}', {mortos})"""

    print(SQL)
    sql_command(SQL)

def insert2():
    print("Formulário de inserção de Grupo Militar:")

    nome = input("Nome do Grupo Militar: ")

    string = input("Data de Incorporação (dd/mm/aaaa): ")
    incorp = datetime.datetime.strptime(string, "%d/%m/%Y")
    incorp = incorp.strftime('%Y-%m-%d %H:%M:%S')
    
    string = input("Data de Saída (dd/mm/aaaa): ")
    saida = datetime.datetime.strptime(string, "%d/%m/%Y")
    saida = saida.strftime('%Y-%m-%d %H:%M:%S')

    SQL = f"""INSERT INTO conflitos_belicos.grupos_armados
    VALUES (DEFAULT, '{nome}', '{incorp}', '{saida}', 0)"""

    print(SQL)
    sql_command(SQL)
   
def insert3():
    print("Formulário de inserção de Divisão de Grupo Militar")

    homens = int(input("Nº de Soldados: "))
    avioes = int(input("Nº de Aviões Militares: "))
    barcos = int(input("Nº de Navios Militares: "))
    tanques = int(input("Nº de Tanques: "))
    baixas = int(input("Nº de Baixas Confirmadas: "))

    SQL = f"""INSERT INTO conflitos_belicos.divisoes 
    VALUES (DEFAULT, {homens}, {avioes}, {barcos}, {tanques}, {baixas})
    """

    print(SQL)
    sql_command(SQL)

def insert4():
    print("Formulário de inserção de Líder Político")

    # Informações do Líder Politico
    nome = input("Nome do Líder: ")
    descricao = input("Descrição do Apoio: ")

    # Seleciona a PK do Grupo Armado
    SQL = """
        SELECT id, nome
        FROM conflitos_belicos.grupos_armados 
        """
    content = sql_select(SQL)

    # Usuário seleciona qual Grupo Armado associar
    grupo = 0
    sentinel = True 
    while sentinel:
        print("Selecione o Grupo Armado para associar a esse Líder Político: ")   
        for i, row in enumerate(content):
            print(f"\t - {i} - {row[1]}")

        index = int(input("\nSua escolha: "))
        if index >= 0 and index < len(content): 
            grupo = content[index][0]
            sentinel = False

    SQL = f"""INSERT INTO conflitos_belicos.lideres_politicos 
    VALUES (DEFAULT, '{nome}', '{descricao}', {grupo})"""

    print(SQL)
    sql_command(SQL)

def insert5():
    print("Formulário de inserção de Chefe Militar")

    faixa = input("Faixa Hierárquica: ") 

    # Seleciona a PK do Líder Político
    SQL = """
        SELECT id, nome
        FROM conflitos_belicos.lideres_politicos 
        """
    content = sql_select(SQL)

    # Usuário seleciona qual Líder Político associar
    lider = 0
    sentinel = True 
    while sentinel:
        print("Selecione o Líder Político para associar a esse Chefe Militar")
        for i, row in enumerate(content):
            print(f"\t - {i} - {row[1]}")

        index = int(input("\nSua escolha: "))
        if index >= 0 and index < len(content): 
            lider = content[index][0]
            sentinel = False

    # Seleciona a PK da Divisão
    SQL = """
        SELECT id
        FROM conflitos_belicos.divisoes
        """
    content = sql_select(SQL)

    # Usuário seleciona qual Divisão associar
    divisao = 0
    sentinel = True 
    while sentinel:
        print("Selecione a Divisão para associar a esse Chefe Militar")
        for i, row in enumerate(content):
            print(f"\t - {i} - Divisão {row[0]}")

        index = int(input("Sua escolha:"))
        if index >= 0 and index < len(content): 
            divisao = content[index][0]
            sentinel = False

    SQL = f"""INSERT INTO conflitos_belicos.chefes_militares 
    VALUES (DEFAULT, '{faixa}', {lider}, {divisao})"""

    print(SQL)
    sql_command(SQL)

def ex_b():
    sentinel = True 

    while sentinel:
        sentinel = False
        alt = int(input("Digite o número da alternativa que quer rodar (1 até 6): "))

        clear()
        if alt == 1:
            alt1()
        elif alt == 2:
            alt2()
        elif alt == 3:
            alt3()
        elif alt == 4:
            alt4()
        elif alt == 5:
            alt5()
        elif alt == 6:
            alt6()
        else:
            sentinel = True
            print("Escolha inválida, tente novamente!")


# Alternativa 1
def alt1():
    data = []
    # Pega os dados do BD para colocar em um dataframe  
    content = sql_select("SELECT count(id) FROM conflitos_belicos.conflitos_religiosos NATURAL JOIN conflitos_belicos.conflitos")
    tipo = "Religioso"
    for row in content:
        data.append([tipo, row[0]])

    content = sql_select("SELECT count(id) FROM conflitos_belicos.conflitos_economicos NATURAL JOIN conflitos_belicos.conflitos")
    tipo = "Econômico"
    for row in content:
        data.append([tipo, row[0]])

    content = sql_select("SELECT count(id) FROM conflitos_belicos.conflitos_raciais NATURAL JOIN conflitos_belicos.conflitos")
    tipo = "Racial"
    for row in content:
        data.append([tipo, row[0]])

    content = sql_select("SELECT count(id) FROM conflitos_belicos.conflitos_territoriais NATURAL JOIN conflitos_belicos.conflitos")
    tipo = "Territorial"
    for row in content:
        data.append([tipo, row[0]])

    # Dá parse no dataframe
    df = pd.DataFrame(data, columns=["Tipo", "Qtd. Conflitos"])
    print(df)

    # Cria o gráfico
    sns.barplot(x = "Tipo", y = "Qtd. Conflitos", data = df)
    plt.show()
    plt.savefig('EP3-EX2-1.png')
    print("Gráfico salvo em PNG")

def fetch_dealer(weapon = "", mode = ""):
    if mode == "or":
        SQL = f'''SELECT DISTINCT T.Id, T.Nome
                FROM conflitos_belicos.Traficantes AS T INNER JOIN conflitos_belicos.traficante_fornece_armas
                ON id_traficante = T.id
                WHERE tipo_arma = 'Barret M82' OR tipo_arma = 'M200 Intervention';
            '''
        return sql_select(SQL)



    SQL = f'''SELECT DISTINCT T.Id, T.Nome
            FROM conflitos_belicos.Traficantes AS T INNER JOIN conflitos_belicos.traficante_fornece_armas
            ON id_traficante = T.id
            WHERE tipo_arma = '{weapon}';
            '''
    return sql_select(SQL)


# Alternativa 2
def alt2():
    content = fetch_dealer(mode="or")
    print("Fornedores de uma das duas armas:")
    for row in content:
        print(f"\t{row[1]} (ID: {row[0]})")

    content = fetch_dealer(weapon="Barret M82")
    print("\nFornedores da 'Barrett M82':")
    for row in content:
        print(f"\t{row[1]} (ID: {row[0]})")
    
    content = fetch_dealer(weapon="M200 Intervention")
    print("\nFornedores da 'M200 Intervention':")
    for row in content:
        print(f"\t{row[1]} (ID: {row[0]})")


# Alternativa 3
def alt3():
    SQL = """
        SELECT Nome, numero_mortos_feridos
        FROM conflitos_belicos.Conflitos 
        ORDER BY numero_mortos_feridos DESC
        FETCH FIRST 5 ROWS ONLY;	
            """

    content = sql_select(SQL)
    print("\nTop 5 conflitos que mais mataram pessoas:")
    for row in content:
        print(f"\t - {row[0]} -> {row[1]} Baixas")


# Alternativa 4
def alt4():
    SQL = """
        SELECT id_organizacao_mediadora, OM.nome, count(id_organizacao_mediadora)
        FROM conflitos_belicos.organizacoes_mediadoras AS OM 
        RIGHT JOIN conflitos_belicos.lideres_politicos_dialogam_com_organizacoes_mediadoras AS LOM
        ON OM.ID = LOM.id_organizacao_mediadora
        GROUP BY id_organizacao_mediadora, OM.NOME
        ORDER BY count(id_organizacao_mediadora) DESC
        FETCH FIRST 5 ROWS ONLY;
        """

    content = sql_select(SQL)
    print("\nTop 5 organizações que mais mediaram conflitos:")
    for row in content:
        print(f"\t - {row[1]} -> {row[2]} Mediações")

# Alternativa 5
def alt5():
    SQL = """
        SELECT id_grupo_armado, GA.nome, count(id_grupo_armado) AS "Número Traficado"
        FROM conflitos_belicos.grupos_armados AS GA 
        RIGHT JOIN conflitos_belicos.traficante_fornece_armas AS TGA
        ON GA.ID = TGA.id_grupo_armado
        GROUP BY id_grupo_armado, GA.NOME
        ORDER BY count(id_grupo_armado) DESC
        FETCH FIRST 5 ROWS ONLY;
          """

    content = sql_select(SQL)
    print("\nTop 5 maiores receptores de armas:")
    for row in content:
        print(f"\t - {row[1]} -> {row[2]} Receptações")

# Alternativa 6
def alt6():
    SQL = """
        SELECT count(pais_afetado), pais_afetado
        FROM conflitos_belicos.conflitos AS C INNER JOIN conflitos_belicos.conflitos_religiosos 
        ON C.ID = ID_CONFLITO
        GROUP BY pais_afetado
        ORDER BY count(pais_afetado) DESC
        FETCH FIRST 1 ROWS ONLY;
            """

    content = sql_select(SQL)
    row = content[0]
    print(f"\nO país com maior número de conflitos religiosos foi o {row[1]}, tendo {row[0]} conflitos desse tipo.")


def main():
    sentinel = True 

    while sentinel:
        sentinel = False
        print("Selecione a alternativa do exercício 2 para rodar: ")
        print("1 - Alternativa A")
        print("2 - Alternativa B")
        choice = int(input("Sua escolha: "))

        clear()
        if choice == 1:
            ex_a()
        elif choice == 2:
            ex_b()
        else:
            sentinel = True
            print("Escolha inválida, tente novamente!")
        
            

if __name__ == "__main__":
    try:
        main()
    except Exception:
        print("Ops, ocorreu um erro. Cheque as informações inseridas e tente novamente")
    finally:  
        conn.close()
