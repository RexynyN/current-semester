-- EP 3 - Banco de Dados 1 

-- Breno da Silva Nogueira - 12400392
-- Luigi Tramontim de Paula - 12727987
-- Juliana Almeida Santos - 12566178

create schema conflitos_belicos;

-- ========================================== CRIAÇÃO DAS TABLES ===============================================

-- EXERCÍCIO 1A ------------------------------------------------------
create table conflitos_belicos.conflitos (
    id serial primary key not null,
    nome varchar(50) not null,
    pais_afetado varchar(50) not null,
    numero_mortos_feridos integer not null
);

create table conflitos_belicos.conflitos_raciais (
  id serial primary key not null,
  etnia_enfrentada varchar(50) not null,
  id_conflito integer not null,
  foreign key (id_conflito) references conflitos_belicos.conflitos(id) on delete cascade
);

create table conflitos_belicos.conflitos_territoriais (
  id serial primary key not null,
  regiao_afetada varchar(50) not null,
  id_conflito integer not null,
  foreign key (id_conflito) references conflitos_belicos.conflitos(id) on delete cascade
);

create table conflitos_belicos.conflitos_economicos (
  id serial primary key not null,
  materia_prima_afetada varchar(50) not null,
  id_conflito integer not null,
  foreign key (id_conflito) references conflitos_belicos.conflitos(id) on delete cascade
);

create table conflitos_belicos.conflitos_religiosos (
  id serial primary key not null,
  religiao_afetada varchar(50) not null,
  id_conflito integer not null,
  foreign key (id_conflito) references conflitos_belicos.conflitos(id) on delete cascade
);

create table conflitos_belicos.divisoes (
    id serial primary key not null,
    numero_homens integer not null,
    numero_avioes integer not null,
    numero_barco integer not null,
    numero_tanques integer not null,
    numero_baixas integer not null
);

create table conflitos_belicos.grupos_armados (
    id serial primary key not null,
    nome varchar(50) not null,
    data_incorporacao timestamp not null,
    data_saida timestamp not null,
    soma_numero_baixas integer not null
);

create table conflitos_belicos.organizacoes_mediadoras (
    id serial primary key not null, 
    nome varchar(50) not null,
    data_incorporacao timestamp not null,
    numero_pessoas_no_conflito integer not null,
    tipo varchar(50) not null,
    ajuda varchar(50) not null
);

create table conflitos_belicos.traficantes (
    id serial primary key not null, 
    nome varchar(50) not null
);

create table conflitos_belicos.lideres_politicos (
    id serial primary key not null, 
    nome varchar(50) not null,
    descricao_apoio varchar(50) not null,
    id_grupo_armado integer not null,
    foreign key(id_grupo_armado) references conflitos_belicos.grupos_armados(id) on delete cascade
);

create table conflitos_belicos.chefes_militares (
    id serial primary key not null,
    faixa_hierarquica varchar(50) not null,
    id_lider_politico integer not null,
    id_divisao integer not null,
    foreign key(id_lider_politico) references conflitos_belicos.lideres_politicos(id) on delete cascade,
    foreign key(id_divisao) references conflitos_belicos.divisoes(id) on delete cascade
);

create table conflitos_belicos.grupos_armados_sao_dispostos_em_divisoes (
    id serial primary key not null, 
    id_grupo_armado integer not null,
    id_divisao integer not null,
    foreign key(id_grupo_armado) references conflitos_belicos.grupos_armados(id) on delete cascade,
    foreign key(id_divisao) references conflitos_belicos.divisoes(id) on delete cascade
);

create table conflitos_belicos.grupos_armados_participam_de_conflitos (
    id serial primary key not null,
    id_conflito integer not null,
    id_grupo_armado integer not null,
    foreign key(id_conflito) references conflitos_belicos.conflitos(id) on delete cascade,
    foreign key(id_grupo_armado) references conflitos_belicos.grupos_armados(id) on delete cascade
);

create table conflitos_belicos.lideres_politicos_dialogam_com_organizacoes_mediadoras (
    id serial primary key not null, 
    id_lider_politico integer not null,
    id_organizacao_mediadora integer not null,
    registro_dialogo varchar(50) not null,
    foreign key(id_lider_politico) references conflitos_belicos.lideres_politicos(id) on delete cascade,
    foreign key(id_organizacao_mediadora) references conflitos_belicos.organizacoes_mediadoras(id) on delete cascade
);

create table conflitos_belicos.traficante_fornece_armas (
    id serial primary key not null, 
    id_grupo_armado integer not null,
    id_traficante integer not null,
    tipo_arma varchar(50) not null,
    quantidade_tipo integer not null,
    foreign key(id_grupo_armado) references conflitos_belicos.grupos_armados(id) on delete cascade,
    foreign key(id_traficante) references conflitos_belicos.traficantes(id) on delete cascade
);

create table conflitos_belicos.variedade_de_armas (
    id serial primary key not null, 
    id_traficante integer not null,
    tipo_arma varchar(50) not null,
    capacidade_destrutiva varchar(50) not null,
    foreign key(id_traficante) references conflitos_belicos.traficantes(id) on delete cascade
);

-- ========================================== TRIGGERS, RULES E CHECKS ===============================================

-- EXERCÍCIO 1B ------------------------------------------------------
create or replace rule maximo_tres_chefes_militares_por_divisao_insert as 
on insert to conflitos_belicos.chefes_militares
where new.id_divisao in (select distinct(cm.id_divisao)
					     from conflitos_belicos.chefes_militares as cm
					     group by cm.id_divisao
					     having	count(cm.id_divisao) >= 3)
do instead nothing;

create or replace rule maximo_tres_chefes_militares_por_divisao_update as 
on update to conflitos_belicos.chefes_militares
where new.id_divisao in (select	distinct(cm.id_divisao)
						 from conflitos_belicos.chefes_militares as cm
						 group by cm.id_divisao
						 having count(cm.id_divisao) >= 3)
do instead nothing;

-- EXERCÍCIO 1C ------------------------------------------------------
create or replace rule minimo_dois_grupos_armados_delete as 
on delete to conflitos_belicos.grupos_armados_participam_de_conflitos
where old.id_conflito in (select distinct(gapc.id_conflito)
					      from conflitos_belicos.grupos_armados_participam_de_conflitos as gapc
					      group by gapc.id_conflito
					      having count(gapc.id_grupo_armado) <= 2)
do instead nothing;

create or replace rule minimo_dois_grupos_armados_update as 
on update to conflitos_belicos.grupos_armados_participam_de_conflitos
where new.id_conflito in (select distinct(gapc.id_conflito)
						  from conflitos_belicos.grupos_armados_participam_de_conflitos as gapc
						  group by gapc.id_conflito
						  having count(gapc.id_grupo_armado) <= 2)
do instead nothing;

create or replace rule diff_grupos_armados_insert as 
on insert to conflitos_belicos.grupos_armados_participam_de_conflitos
where new.id_grupo_armado in (select distinct(gapc.id_grupo_armado)
							  from conflitos_belicos.grupos_armados_participam_de_conflitos as gapc
							  where new.id_conflito = gapc.id_conflito
							  group by gapc.id_grupo_armado)
do instead nothing;

create or replace rule diff_grupos_armados_update as 
on update to conflitos_belicos.grupos_armados_participam_de_conflitos
where new.id_grupo_armado in (select distinct(gapc.id_grupo_armado)
							  from conflitos_belicos.grupos_armados_participam_de_conflitos as gapc
							  where new.id_conflito = gapc.id_conflito
							  group by gapc.id_grupo_armado)
do instead nothing;

-- EXERCÍCIO 1D ------------------------------------------------------
create or replace rule diff_divisoes_insert as 
on insert to conflitos_belicos.grupos_armados_sao_dispostos_em_divisoes
where new.id_divisao in (select distinct(gasdd.id_divisao)
	                     from conflitos_belicos.grupos_armados_sao_dispostos_em_divisoes as gasdd
	                     where new.id_divisao = gasdd.id_divisao
	                     group by gasdd.id_divisao)
do instead nothing;

create or replace rule diff_divisoes_update as 
on update to conflitos_belicos.grupos_armados_sao_dispostos_em_divisoes
where new.id_divisao in (select distinct(gasdd.id_divisao)
	                     from conflitos_belicos.grupos_armados_sao_dispostos_em_divisoes as gasdd
	                     where new.id_divisao = gasdd.id_divisao
	                     group by gasdd.id_divisao)
do instead nothing;


create or replace rule divisoes_delete as 
on delete to conflitos_belicos.divisoes
where old.id in (select distinct(gasdd.id_divisao)
	                     from conflitos_belicos.grupos_armados_sao_dispostos_em_divisoes as gasdd
	                     where old.id = gasdd.id_divisao
	                     group by gasdd.id_divisao)
do instead nothing;

create or replace function conflitos_belicos.func_soma_numero_baixas_divisoes_update()
returns trigger as $trigger$
begin 
update conflitos_belicos.grupos_armados 
set soma_numero_baixas = (select sum(d.numero_baixas)
						  from conflitos_belicos.divisoes as d
						  where d.id in (select distinct(gasdd.id_divisao)
								  		 from conflitos_belicos.grupos_armados_sao_dispostos_em_divisoes as gasdd
								  		 where gasdd.id_grupo_armado in (select distinct(gasdd2.id_grupo_armado)
							  								  		     from conflitos_belicos.grupos_armados_sao_dispostos_em_divisoes as gasdd2
							  								  		     where gasdd2.id_divisao = old.id)))
where id in (select	distinct(gasdd2.id_grupo_armado)
			 from conflitos_belicos.grupos_armados_sao_dispostos_em_divisoes as gasdd2
			 where gasdd2.id_divisao = old.id);
return new;
end; $trigger$ language plpgsql volatile cost 100;

create trigger soma_numero_baixas_divisoes_update
after update on	conflitos_belicos.divisoes
for each row execute procedure conflitos_belicos.func_soma_numero_baixas_divisoes_update();

create or replace function conflitos_belicos.func_soma_numero_baixas_divisoes_delete()
returns trigger as $trigger$
begin 
update conflitos_belicos.grupos_armados 
set soma_numero_baixas = (select sum(d.numero_baixas)
						  from conflitos_belicos.divisoes as d
						  where d.id in (select distinct(gasdd.id_divisao)
								  		 from conflitos_belicos.grupos_armados_sao_dispostos_em_divisoes as gasdd
								  		 where gasdd.id_grupo_armado in (select distinct(gasdd2.id_grupo_armado)
							  								  		     from conflitos_belicos.grupos_armados_sao_dispostos_em_divisoes as gasdd2
							  								  		     where gasdd2.id_divisao = old.id)))
where id in (select	distinct(gasdd2.id_grupo_armado)
			 from conflitos_belicos.grupos_armados_sao_dispostos_em_divisoes as gasdd2
			 where gasdd2.id_divisao = old.id);
return new;
end; $trigger$ language plpgsql volatile cost 100;

create trigger soma_numero_baixas_divisoes_delete
after delete on	conflitos_belicos.divisoes
for each row execute procedure conflitos_belicos.func_soma_numero_baixas_divisoes_delete();

create or replace function conflitos_belicos.func_soma_numero_baixas_grupos_armados_sao_dispostos_em_divisoes_insert()
returns trigger as $trigger$
begin 
update conflitos_belicos.grupos_armados 
set soma_numero_baixas = (select sum(d.numero_baixas)
						  from conflitos_belicos.divisoes as d
						  where d.id = new.id_divisao)
where id = new.id_grupo_armado;
return new;
end; $trigger$ language plpgsql volatile cost 100;

create trigger soma_numero_baixas_grupos_armados_sao_dispostos_em_divisoes_insert
after insert on conflitos_belicos.grupos_armados_sao_dispostos_em_divisoes
for each row execute procedure conflitos_belicos.func_soma_numero_baixas_grupos_armados_sao_dispostos_em_divisoes_insert();

create or replace function conflitos_belicos.func_soma_numero_baixas_grupos_armados_sao_dispostos_em_divisoes_update()
returns trigger as $trigger$
begin 
update conflitos_belicos.grupos_armados 
set soma_numero_baixas = (select sum(d.numero_baixas)
						  from conflitos_belicos.divisoes as d
						  where d.id in (select distinct(gasdd.id_divisao)
								  		 from conflitos_belicos.grupos_armados_sao_dispostos_em_divisoes as gasdd
								  		 where gasdd.id_grupo_armado in (select distinct(gasdd2.id_grupo_armado)
							  								  		     from conflitos_belicos.grupos_armados_sao_dispostos_em_divisoes as gasdd2
							  								  		     where gasdd2.id = old.id)))
where id in (select	distinct(gasdd2.id_grupo_armado)
			 from conflitos_belicos.grupos_armados_sao_dispostos_em_divisoes as gasdd2
			 where gasdd2.id = old.id);
return new;
end; $trigger$ language plpgsql volatile cost 100;

create trigger soma_numero_baixas_grupos_armados_sao_dispostos_em_divisoes_update
after update on	conflitos_belicos.grupos_armados_sao_dispostos_em_divisoes
for each row execute procedure conflitos_belicos.func_soma_numero_baixas_grupos_armados_sao_dispostos_em_divisoes_update();

create or replace function conflitos_belicos.func_soma_numero_baixas_grupos_armados_sao_dispostos_em_divisoes_delete()
returns trigger as $trigger$
begin 
update conflitos_belicos.grupos_armados 
set soma_numero_baixas = soma_numero_baixas - (select sum(d.numero_baixas)
											   from conflitos_belicos.divisoes as d
											   where d.id = old.id_divisao)
where id = old.id_grupo_armado;
return new;
end; $trigger$ language plpgsql volatile cost 100;

create trigger soma_numero_baixas_grupos_armados_sao_dispostos_em_divisoes_delete
after delete on	conflitos_belicos.grupos_armados_sao_dispostos_em_divisoes
for each row execute procedure conflitos_belicos.func_soma_numero_baixas_grupos_armados_sao_dispostos_em_divisoes_delete();

-- ========================================== INSERTS ===============================================

INSERT INTO conflitos_belicos.conflitos VALUES(DEFAULT, 'Guerra dos Cem Anos', 'Inglaterra', 1584);
INSERT INTO conflitos_belicos.conflitos VALUES(DEFAULT, 'Revolução Francesa', 'França', 983);
INSERT INTO conflitos_belicos.conflitos VALUES(DEFAULT, 'Revolução Russa', 'Rússia', 3890);
INSERT INTO conflitos_belicos.conflitos VALUES(DEFAULT, 'Guerra dos Canudos', 'Brasil', 494);
INSERT INTO conflitos_belicos.conflitos VALUES(DEFAULT, 'Guerra Civil Norte-americana', 'Estados Unidos', 3764);
INSERT INTO conflitos_belicos.conflitos VALUES(DEFAULT, 'Guerra do Vietnã', 'Vietnã', 11384);
INSERT INTO conflitos_belicos.conflitos VALUES(DEFAULT, 'Guerra Fria', 'União Soviética', 0);
INSERT INTO conflitos_belicos.conflitos VALUES(DEFAULT, 'Revolta dos Farrapos', 'Brasil', 1012);
INSERT INTO conflitos_belicos.conflitos VALUES(DEFAULT, 'Cruzadas', 'Jerusalém', 19054);
INSERT INTO conflitos_belicos.conflitos VALUES(DEFAULT, 'Conflito da Palestina', 'Palestina', 2752);
INSERT INTO conflitos_belicos.conflitos VALUES (DEFAULT, 'Segunda Guerra Mundial', 'Alemanha', 2098238);
INSERT INTO conflitos_belicos.conflitos VALUES (DEFAULT, 'Guerra Por Israel', 'Palestina', 3921);


INSERT INTO conflitos_belicos.conflitos_raciais VALUES (DEFAULT, 'BRANCOS', 5);

INSERT INTO conflitos_belicos.conflitos_territoriais VALUES (DEFAULT, 'FRANÇA E  INGLATERRA', 1);
INSERT INTO conflitos_belicos.conflitos_territoriais VALUES (DEFAULT, 'VIETNÃ DO SUL,  VIETNÃ DO NORTE E ESTADOS UNIDOS', 6);
INSERT INTO conflitos_belicos.conflitos_territoriais VALUES (DEFAULT, 'ISRAEL E PALESTINA', 10);

INSERT INTO conflitos_belicos.conflitos_economicos VALUES (DEFAULT, 'Algodão', 2);
INSERT INTO conflitos_belicos.conflitos_economicos VALUES (DEFAULT, 'Moeda', 3);
INSERT INTO conflitos_belicos.conflitos_economicos VALUES (DEFAULT, 'Café', 4);
INSERT INTO conflitos_belicos.conflitos_economicos VALUES (DEFAULT, 'Moeda', 7);
INSERT INTO conflitos_belicos.conflitos_economicos VALUES (DEFAULT, 'Café', 8);

INSERT INTO conflitos_belicos.conflitos_religiosos VALUES (DEFAULT, 'Católica e Judaica', 10);
INSERT INTO conflitos_belicos.conflitos_religiosos VALUES (DEFAULT, 'Protestantismo', 5);
INSERT INTO conflitos_belicos.conflitos_religiosos VALUES (DEFAULT, 'Católica e Judaica', 12);


INSERT INTO conflitos_belicos.divisoes VALUES (DEFAULT, 2354, 884, 243, 543, 789);
INSERT INTO conflitos_belicos.divisoes VALUES (DEFAULT, 6767, 365, 46, 697, 4589);
INSERT INTO conflitos_belicos.divisoes VALUES (DEFAULT, 14345, 0, 3422, 0, 3432);
INSERT INTO conflitos_belicos.divisoes VALUES (DEFAULT, 5499, 344, 34, 1245, 99);
INSERT INTO conflitos_belicos.divisoes VALUES (DEFAULT, 88, 388, 34, 3, 254);
INSERT INTO conflitos_belicos.divisoes VALUES (DEFAULT, 43433, 0, 7898, 0, 7606);
INSERT INTO conflitos_belicos.divisoes VALUES (DEFAULT, 9898, 0, 565, 0, 1485);
INSERT INTO conflitos_belicos.divisoes VALUES (DEFAULT, 5546, 0, 76, 0, 3446);
INSERT INTO conflitos_belicos.divisoes VALUES (DEFAULT, 23432, 761, 254, 351, 2456);
INSERT INTO conflitos_belicos.divisoes VALUES (DEFAULT, 6765, 214, 37, 2, 1891);

INSERT INTO conflitos_belicos.grupos_armados VALUES (DEFAULT, 'Grupo de Choque', '2019-12-31 12:30:30', '2021-12-31 12:30:30', 23984); 
INSERT INTO conflitos_belicos.grupos_armados VALUES (DEFAULT, 'Grupo Especializado em Bombas', '1973-10-13 21:23:59', '1975-03-04 14:27:10', 879);
INSERT INTO conflitos_belicos.grupos_armados VALUES (DEFAULT, 'Grupo de Ataque com Espadas', '1103-01-14 07:53:09', '1119-03-04 23:35:16', 9023); 
INSERT INTO conflitos_belicos.grupos_armados VALUES (DEFAULT, 'Grupo de Ataque Frontal', '1960-01-21 17:33:49', '1961-10-11 00:04:32', 8745); 
INSERT INTO conflitos_belicos.grupos_armados VALUES (DEFAULT, 'Grupo de Planejamento de Ataque', '1897-01-03 15:21:49', '1897-05-03 10:34:21', 276); 
INSERT INTO conflitos_belicos.grupos_armados VALUES (DEFAULT, 'Tropa Lateral', '1339-12-31 03:23:06', '1409-10-13 05:44:12', 18945); 
INSERT INTO conflitos_belicos.grupos_armados VALUES (DEFAULT, 'Batalhão de Infantaria', '1780-04-11 04:53:32', '1787-01-31 02:14:52', 12694); 
INSERT INTO conflitos_belicos.grupos_armados VALUES (DEFAULT, 'Frente de Ataque', '1836-11-21 12:33:49', '1837-03-21 03:06:32', 823); 
INSERT INTO conflitos_belicos.grupos_armados VALUES (DEFAULT, 'Batalhão de Ataque', '1917-04-11 15:23:49', '1922-11-01 10:04:52', 4365); 
INSERT INTO conflitos_belicos.grupos_armados VALUES (DEFAULT, 'Tropa 192', '1862-11-11 12:13:49', '1864-10-21 10:44:35', 879); 

INSERT INTO conflitos_belicos.organizacoes_mediadoras VALUES (1, 'ONU', '1961-11-21 12:33:49', 8722, 'Organização Intergovernamental', 'Pacificar');
INSERT INTO conflitos_belicos.organizacoes_mediadoras VALUES (2, 'OTAN', '1950-01-12 11:35:21', 98294, 'Aliança Militar Intergovernamental', 'Controlar os Ataques');
INSERT INTO conflitos_belicos.organizacoes_mediadoras VALUES (3, 'União Europeia', '1993-04-11 21:54:20', 8902, 'Grupo Econômico e Político', 'Verificar tratados de paz');

INSERT INTO conflitos_belicos.traficantes VALUES (1, 'Manuel Ferreira');
INSERT INTO conflitos_belicos.traficantes VALUES (2, 'Joaquina Costa');
INSERT INTO conflitos_belicos.traficantes VALUES (3, 'Tomás Potter');
INSERT INTO conflitos_belicos.traficantes VALUES (4, 'Farid Soarez');
INSERT INTO conflitos_belicos.traficantes VALUES (5, 'Breno da Silva Nogueira');
INSERT INTO conflitos_belicos.traficantes VALUES (6, 'Phillipe Lopes');
INSERT INTO conflitos_belicos.traficantes VALUES (7, 'Kiara McCartney');
INSERT INTO conflitos_belicos.traficantes VALUES (8, 'Luisa Azevedo');
INSERT INTO conflitos_belicos.traficantes VALUES (9, 'Gustavo Coutinho');
INSERT INTO conflitos_belicos.traficantes VALUES (10, 'Matheus Pereira Tavares');
INSERT INTO conflitos_belicos.traficantes VALUES (11, 'Leonardo Germano');
INSERT INTO conflitos_belicos.traficantes VALUES (12, 'Moura Filho');

INSERT INTO conflitos_belicos.lideres_politicos VALUES (DEFAULT, 'Henrique da Silva', 'Mediador', 2);
INSERT INTO conflitos_belicos.lideres_politicos VALUES (DEFAULT, 'Janaina Gonçalves', 'Embaixadora', 4);
INSERT INTO conflitos_belicos.lideres_politicos VALUES (DEFAULT, 'Enzo Nogueira', 'Mediador', 1);
INSERT INTO conflitos_belicos.lideres_politicos VALUES (DEFAULT, 'João V', 'Papa', 3);
INSERT INTO conflitos_belicos.lideres_politicos VALUES (DEFAULT, 'Antonio Conselheiro', 'Líder', 5);
INSERT INTO conflitos_belicos.lideres_politicos VALUES (DEFAULT, 'Bento Gonçalves', 'Líder', 8);
INSERT INTO conflitos_belicos.lideres_politicos VALUES (DEFAULT, 'Abrahan Lincoln', 'Presidente', 10);
INSERT INTO conflitos_belicos.lideres_politicos VALUES (DEFAULT, 'Joana D Arc', 'Mártir', 6);
INSERT INTO conflitos_belicos.lideres_politicos VALUES (DEFAULT, 'Napoleão Bonaparte', 'Líder Militar', 7);
INSERT INTO conflitos_belicos.lideres_politicos VALUES (DEFAULT, 'Lênin', 'Representante do Povo', 9);

INSERT INTO conflitos_belicos.chefes_militares VALUES (DEFAULT, 'Comandante', 1, 1);
INSERT INTO conflitos_belicos.chefes_militares VALUES (DEFAULT, 'Capitão', 2, 4);
INSERT INTO conflitos_belicos.chefes_militares VALUES (DEFAULT, 'Conselheiro', 3, 2);
INSERT INTO conflitos_belicos.chefes_militares VALUES (DEFAULT, 'Comandante', 4, 3);
INSERT INTO conflitos_belicos.chefes_militares VALUES (DEFAULT, 'Conselheiro', 5, 5);
INSERT INTO conflitos_belicos.chefes_militares VALUES (DEFAULT, 'Comandante', 6, 8);
INSERT INTO conflitos_belicos.chefes_militares VALUES (DEFAULT, 'Capitão', 7, 10);
INSERT INTO conflitos_belicos.chefes_militares VALUES (DEFAULT, 'Guerreiro', 8, 6);
INSERT INTO conflitos_belicos.chefes_militares VALUES (DEFAULT, 'Capitão', 9, 7);
INSERT INTO conflitos_belicos.chefes_militares VALUES (DEFAULT, 'Conselheiro', 10, 9);

INSERT INTO conflitos_belicos.grupos_armados_sao_dispostos_em_divisoes VALUES (1, 1, 1);
INSERT INTO conflitos_belicos.grupos_armados_sao_dispostos_em_divisoes VALUES (2, 2, 2);
INSERT INTO conflitos_belicos.grupos_armados_sao_dispostos_em_divisoes VALUES (3, 3, 3);
INSERT INTO conflitos_belicos.grupos_armados_sao_dispostos_em_divisoes VALUES (4, 4, 4);
INSERT INTO conflitos_belicos.grupos_armados_sao_dispostos_em_divisoes VALUES (5, 5, 5);
INSERT INTO conflitos_belicos.grupos_armados_sao_dispostos_em_divisoes VALUES (6, 6, 6);
INSERT INTO conflitos_belicos.grupos_armados_sao_dispostos_em_divisoes VALUES (7, 7, 7);
INSERT INTO conflitos_belicos.grupos_armados_sao_dispostos_em_divisoes VALUES (8, 8, 8);
INSERT INTO conflitos_belicos.grupos_armados_sao_dispostos_em_divisoes VALUES (9, 9, 9);
INSERT INTO conflitos_belicos.grupos_armados_sao_dispostos_em_divisoes VALUES (10, 10, 10);

INSERT INTO conflitos_belicos.grupos_armados_participam_de_conflitos VALUES (1, 10, 1);
INSERT INTO conflitos_belicos.grupos_armados_participam_de_conflitos VALUES (2, 7, 2);
INSERT INTO conflitos_belicos.grupos_armados_participam_de_conflitos VALUES (3, 9, 3);
INSERT INTO conflitos_belicos.grupos_armados_participam_de_conflitos VALUES (4, 6, 4);
INSERT INTO conflitos_belicos.grupos_armados_participam_de_conflitos VALUES (5, 4, 5);
INSERT INTO conflitos_belicos.grupos_armados_participam_de_conflitos VALUES (6, 1, 6);
INSERT INTO conflitos_belicos.grupos_armados_participam_de_conflitos VALUES (7, 2, 7);
INSERT INTO conflitos_belicos.grupos_armados_participam_de_conflitos VALUES (8, 8, 8);
INSERT INTO conflitos_belicos.grupos_armados_participam_de_conflitos VALUES (9, 3, 9);
INSERT INTO conflitos_belicos.grupos_armados_participam_de_conflitos VALUES (10, 5, 10);

INSERT INTO conflitos_belicos.lideres_politicos_dialogam_com_organizacoes_mediadoras VALUES (1, 3, 1, 'PRIMEIRO TRATADO DE PAZ');
INSERT INTO conflitos_belicos.lideres_politicos_dialogam_com_organizacoes_mediadoras VALUES (2, 3, 1, 'SEGUNDO TRATADO DE PAZ');
INSERT INTO conflitos_belicos.lideres_politicos_dialogam_com_organizacoes_mediadoras VALUES (3, 3, 1, 'ACORDO PARA RETIRADA DE TROPAS');
INSERT INTO conflitos_belicos.lideres_politicos_dialogam_com_organizacoes_mediadoras VALUES (4, 2, 2, 'ENTEDIMENTO DA SITUAÇÃO NO VIETNA');
INSERT INTO conflitos_belicos.lideres_politicos_dialogam_com_organizacoes_mediadoras VALUES (5, 2, 2, 'ENVIO DAS TROPAS');
INSERT INTO conflitos_belicos.lideres_politicos_dialogam_com_organizacoes_mediadoras VALUES (6, 2, 2, 'PROPOSTA DE RENDIÇÃO');
INSERT INTO conflitos_belicos.lideres_politicos_dialogam_com_organizacoes_mediadoras VALUES (7, 3, 1, 'ACORDOS PARA ABRIGOS DE REFUGIADOS');

INSERT INTO conflitos_belicos.traficante_fornece_armas VALUES (1, 1, 4, 'M200 Intervention', 350);
INSERT INTO conflitos_belicos.traficante_fornece_armas VALUES (2, 1, 4, 'Sub-metralhadora', 700);
INSERT INTO conflitos_belicos.traficante_fornece_armas VALUES (3, 1, 2, 'Granada', 1500);
INSERT INTO conflitos_belicos.traficante_fornece_armas VALUES (4, 1, 6, 'Barret M82', 15000);
INSERT INTO conflitos_belicos.traficante_fornece_armas VALUES (5, 2, 1, 'Rifle', 700);
INSERT INTO conflitos_belicos.traficante_fornece_armas VALUES (6, 2, 7, 'Barret M82', 2500);
INSERT INTO conflitos_belicos.traficante_fornece_armas VALUES (7, 3, 8, 'Espada', 200000);
INSERT INTO conflitos_belicos.traficante_fornece_armas VALUES (8, 4, 1, 'Rifle', 10000);
INSERT INTO conflitos_belicos.traficante_fornece_armas VALUES (9, 4, 7, 'Barret M82', 30000);
INSERT INTO conflitos_belicos.traficante_fornece_armas VALUES (10, 5, 3, 'Rifle', 300);
INSERT INTO conflitos_belicos.traficante_fornece_armas VALUES (11, 6, 8, 'Espada', 100000);
INSERT INTO conflitos_belicos.traficante_fornece_armas VALUES (12, 6, 8, 'Escudo', 12350);
INSERT INTO conflitos_belicos.traficante_fornece_armas VALUES (13, 6, 8, 'Espada', 50000);
INSERT INTO conflitos_belicos.traficante_fornece_armas VALUES (14, 7, 5, 'Espada', 13500);
INSERT INTO conflitos_belicos.traficante_fornece_armas VALUES (15, 7, 5, 'Elmo', 50000);
INSERT INTO conflitos_belicos.traficante_fornece_armas VALUES (16, 8, 3, 'Rifle', 1700);
INSERT INTO conflitos_belicos.traficante_fornece_armas VALUES (17, 9, 1, 'Pistola', 40000);
INSERT INTO conflitos_belicos.traficante_fornece_armas VALUES (18, 9, 1, 'M200 Intervention', 10000);
INSERT INTO conflitos_belicos.traficante_fornece_armas VALUES (19, 9, 7, 'Barret M82', 10000);
INSERT INTO conflitos_belicos.traficante_fornece_armas VALUES (20, 9, 9, 'Granada', 13000);
INSERT INTO conflitos_belicos.traficante_fornece_armas VALUES (21, 10, 10, 'Pistola', 30000);
INSERT INTO conflitos_belicos.traficante_fornece_armas VALUES (22, 10, 10, 'Barret M82', 50000);


INSERT INTO conflitos_belicos.variedade_de_armas VALUES (1, 4, 'M200 Intervention', 'Muito alta');
INSERT INTO conflitos_belicos.variedade_de_armas VALUES (2, 4, 'Sub-metralhadora', 'Muito alta');
INSERT INTO conflitos_belicos.variedade_de_armas VALUES (3, 2, 'Granada', 'Alta');
INSERT INTO conflitos_belicos.variedade_de_armas VALUES (4, 6, 'Barret M82', 'Muito alta');
INSERT INTO conflitos_belicos.variedade_de_armas VALUES (5, 1, 'Rifle', 'Parcialmente alta');
INSERT INTO conflitos_belicos.variedade_de_armas VALUES (6, 7, 'Barret M82', 'Muito alta');
INSERT INTO conflitos_belicos.variedade_de_armas VALUES (7, 8, 'Espada', 'Baixa');
INSERT INTO conflitos_belicos.variedade_de_armas VALUES (8, 3, 'Rifle', 'Parcialmente alta');
INSERT INTO conflitos_belicos.variedade_de_armas VALUES (9, 8, 'Escudo', 'Nula, elemento de proteção');
INSERT INTO conflitos_belicos.variedade_de_armas VALUES (10, 5, 'Espada', 'Baixa');
INSERT INTO conflitos_belicos.variedade_de_armas VALUES (11, 5, 'Elmo', 'Nulas, elemento de proteção');
INSERT INTO conflitos_belicos.variedade_de_armas VALUES (12, 1, 'Pistola', 'Média');
INSERT INTO conflitos_belicos.variedade_de_armas VALUES (13, 1, 'M200 Intervention', 'Muito alta');
INSERT INTO conflitos_belicos.variedade_de_armas VALUES (14, 9, 'Granada', 'Alta');
INSERT INTO conflitos_belicos.variedade_de_armas VALUES (15, 10, 'Pistola', 'Média');
INSERT INTO conflitos_belicos.variedade_de_armas VALUES (16, 10, 'Barret M82', 'Muito alta');

-- ========================================== QUERYS ===============================================

-- Exercício 2:
-- Alternativa B

-- Enunciado 1
SELECT count(id) FROM conflitos_belicos.conflitos_territoriais NATURAL JOIN conflitos_belicos.conflitos;
SELECT count(id) FROM conflitos_belicos.conflitos_raciais NATURAL JOIN conflitos_belicos.conflitos;
SELECT count(id) FROM conflitos_belicos.conflitos_economicos NATURAL JOIN conflitos_belicos.conflitos;
SELECT count(id) FROM conflitos_belicos.conflitos_religiosos NATURAL JOIN conflitos_belicos.conflitos;

-- Enunciado 2
SELECT DISTINCT T.Id, T.Nome
FROM conflitos_belicos.Traficantes AS T INNER JOIN conflitos_belicos.traficante_fornece_armas
ON id_traficante = T.id
WHERE tipo_arma = 'Barret M82' OR tipo_arma = 'M200 Intervention';

-- Enunciado 3
SELECT Nome, numero_mortos_feridos
FROM conflitos_belicos.Conflitos 
ORDER BY numero_mortos_feridos DESC
FETCH FIRST 5 ROWS ONLY;		

-- Enunciado 4
SELECT id_organizacao_mediadora, OM.nome, count(id_organizacao_mediadora)
FROM conflitos_belicos.organizacoes_mediadoras AS OM 
RIGHT JOIN conflitos_belicos.lideres_politicos_dialogam_com_organizacoes_mediadoras AS LOM
ON OM.ID = LOM.id_organizacao_mediadora
GROUP BY id_organizacao_mediadora, OM.NOME
ORDER BY count(id_organizacao_mediadora) DESC
FETCH FIRST 5 ROWS ONLY;

-- Enunciado 5
SELECT id_grupo_armado, GA.nome, count(id_grupo_armado) AS "Número Traficado"
FROM conflitos_belicos.grupos_armados AS GA 
RIGHT JOIN conflitos_belicos.traficante_fornece_armas AS TGA
ON GA.ID = TGA.id_grupo_armado
GROUP BY id_grupo_armado, GA.NOME
ORDER BY count(id_grupo_armado) DESC
FETCH FIRST 5 ROWS ONLY;

-- Enunciado 6
SELECT count(pais_afetado), pais_afetado
FROM conflitos_belicos.conflitos AS C INNER JOIN conflitos_belicos.conflitos_religiosos 
ON C.ID = ID_CONFLITO
GROUP BY pais_afetado
ORDER BY count(pais_afetado) DESC
FETCH FIRST 1 ROWS ONLY;