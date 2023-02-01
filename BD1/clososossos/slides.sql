CREATE DATABASE EX1; 
USE EX1;

CREATE TABLE DEPT(
    DEPTNO NUMERIC(2) CONSTRAINT PK_DEPT PRIMARY KEY,
	DNAME VARCHAR(14) ,
	LOC VARCHAR(13) 
)
;
CREATE TABLE EMP(
    EMPNO NUMERIC(4) CONSTRAINT PK_EMP PRIMARY KEY,
	ENAME VARCHAR(10),
	JOB VARCHAR(9),
	MGR NUMERIC(4),
	HIREDATE DATE,
	SAL NUMERIC(7,2),
	COMM NUMERIC(7,2),
	DEPTNO NUMERIC(2) CONSTRAINT FK_DEPTNO REFERENCES DEPT
);

CREATE TABLE BONUS
	(
	ENAME VARCHAR(10) primary key,
	JOB VARCHAR(9)  ,
	SAL NUMERIC,
	COMM NUMERIC
);

CREATE TABLE SALGRADE( 
    GRADE NUMERIC Primary key,
	LOSAL NUMERIC,
	HISAL NUMERIC 
);


-- =================== Exercicio 1 ==================

-- Selecione os nomes dos empregados que têm um salário > 1200 e foram
-- contratados em 1981
SELECT ENAME
FROM EMP
WHERE SAL > 1200 AND EXTRACT(YEAR FROM HIREDATE) = 1981 

-- - Selecione os nomes dos empregados que trabalham no departamento de
-- ‘ACCOUNTING’.
SELECT EMP.ENAME
FROM EMP NATURAL JOIN DEPT 
WHERE DEPT.DNAME = "ACCOUNTING"

-- Selecione os nomes dos empregados que têm um salário entre 1000 e 3000 e
-- trabalham no departamento de ‘RESEARCH’.
SELECT EMP.ENAME
FROM EMP NATURAL JOIN DEPT 
WHERE DEPT.DNAME = "RESEARCH" AND EMP.SAL BETWEEN 1000 AND 3000 

-- Selecione os nomes dos empregados que têm um salário maior que o salário
-- do ‘SMITH’.
SELECT ENAME 
FROM EMP
WHERE SAL > (
    SELECT SAL
    FROM EMP
    WHERE ENAME = "SMITH"
)

-- Selecione os nomes dos empregados que ganham mais que todos os
-- empregados que trabalham no departamento, cujo Deptno é 30.
SELECT ENAME 
FROM EMP
WHERE SAL > (
    SELECT MAX(SAL)
    FROM EMP NATURAL JOIN DEPT 
    WHERE DEPT.DEPTNO = 30
)

-- =================== Exercicio 2 ==================

-- Selecione os nomes, salários e faixa salarial de todos os empregados.
SELECT ENAME, SAL, GRADE
FROM EMP, SALGRADE
WHERE SAL BETWEEN LOSAL AND HISAL

-- Selecione os nomes dos empregados que ganham menos que algum
-- empregado do departamento de ‘SALES’
SELECT ENAME
FROM EMP
WHERE SAL < SOME(
    SELECT SAL
    FROM EMP NATURAL JOIN DEPT
    WHERE DNAME = "SALES"
)

-- Selecione os nomes dos empregados que têm um salário > 1200 e foram contratados em
-- 1981, ordenados alfabeticamente.
SELECT ENAME 
FROM EMP 
WHERE SAL > 1200 AND EXTRACT(YEAR FROM HIREDATE) = "1981"
ORDER BY ENAME ASC

-- Selecione o departamento (número e nome) e a soma total dos salários pagos aos seus
-- empregados.
SELECT DNAME, DEPTNO, SUM(SAL)
FROM DEPT NATURAL JOIN EMP
GROUP BY DEPT.DEPTNO, DEPT.DNAME

-- Selecione o departamento (número e nome) e o seu número total de empregados.
SELECT DNAME, DEPTNO, COUNT(*)
FROM DEPT NATURAL JOIN EMP
GROUP BY DNAME, DEPTNO

-- Selecione o departamento (numero e nome) que pague a maior quantidade em salários.
SELECT DNAME, DEPTNO 
FROM DEPT D NATURAL JOIN EMP E
GROUP BY D.DEPTNO, DNAME
HAVING SUM(SAL) >= ALL(
                        SELECT SUM(SAL)
                        FROM EMP 
                        GROUP BY DEPTNO)

-- Selecione o departamento com o menor numero de empregados.
SELECT DNAME, DEPTNO
FROM DEPT D NATURAL JOIN EMP E
GROUP BY D.DEPTNO, D.DNAME
HAVING COUNT(*) <= ALL(SELECT COUNT(*)
                    FROM EMP 
                    GROUP BY DEPTNO)

-- mude o empregado ‘SMITH’ para ele trabalhar no departamento de ‘ACCOUNTING’
UPDATE EMP 
SET DEPTNO = (
    SELECT DEPTNO
    FROM DEPT 
    WHERE DNAME = "ACCOUNTING"
)
WHERE ENAME = "SMITH"

-- Remova os empregados que trabalham no departamento de ‘RESEARCH’
DELETE FROM DEPT
WHERE DEPTNO = (
    SELECT DEPTNO
    FROM DEPT
    WHERE DNAME = "RESEARCH"
)

-- Decremente o salário dos empregados que ganham mais de 3000, em 10%
UPDATE EMP 
SET SAL = SAL - (SAL * 0.1)
WHERE SAL > 3000


-- =================== Exercicio 3 ==================


-- Quais são os Nomes dos fornecedores que fornecem a Peça ‘P2’?
SELECT FNOME
FROM FORNECEDOR NATURAL JOIN REMESSAS 
WHERE P# = "P2"

-- Quais são os Nomes dos fornecedores que fornecem pelo menos uma
-- peça de cor vermelha?
SELECT FNOME 
FROM FORNECEDOR NATURAL JOIN REMESSAS 
WHERE P# = (
    SELECT P#
    FROM PECAS 
    WHERE COR = "VERMELHO"
)
-- Ou 
SELECT FNOME
FROM (F NATURAL JOIN FP) NATURAL JOIN P
WHERE COR = "VERMELHO"

-- Quais são os códigos dos fornecedores que fornecem pelo menos
-- todas as peças fornecidas pelo fornecedor ‘F2’?
SELECT F#
FROM F 
WHERE NOT EXISTS (
    SELECT P#
    FROM FP 
    WHERE F# = "F2"
    EXCEPT(
        SELECT P# 
        FROM FP
        WHERE FP.F# = F.F# 
    )
)

-- Quais são os nomes dos fornecedores que não fornecem a peça ‘P2’?
SELECT FNOME
FROM F
WHERE F# NOT IN (
    SELECT F#
    FROM FP 
    WHERE P# = "P2"
)