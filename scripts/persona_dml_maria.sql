INSERT INTO 
	`persona_db`.`persona`(`cc`,`nombre`,`apellido`,`genero`,`edad`) 
VALUES
	(123456789,'Pepe','Perez','M',30),
	(987654321,'Pepito','Perez','M',null),
	(321654987,'Pepa','Juarez','F',30),
	(147258369,'Pepita','Juarez','F',10),
	(963852741,'Fede','Perez','M',18);
INSERT INTO
	`persona_db`.`profesion`(`id`,`nom`,`des`)
VALUES
	(1,'Ingeniero de Sistemas','Diseña, desarrolla y mantiene sistemas de software'),
	(2,'Medico','Diagnostica y trata enfermedades de los pacientes'),
	(3,'Abogado','Asesora y representa a personas en asuntos juridicos');

INSERT INTO
	`persona_db`.`telefono`(`num`,`oper`,`duenio`)
VALUES
	('3001112233','Claro',123456789),
	('3104445566','Movistar',321654987),
	('3207778899','Tigo',963852741),
	('3159990011','Claro',987654321);

INSERT INTO
	`persona_db`.`estudios`(`id_prof`,`cc_per`,`fecha`,`univer`)
VALUES
	(1,123456789,'2015-06-30','Pontificia Universidad Javeriana'),
	(1,963852741,'2018-03-15','Universidad de los Andes'),
	(2,321654987,'2012-12-10','Universidad Nacional de Colombia'),
	(3,147258369,'2020-11-20','Universidad Externado de Colombia');
