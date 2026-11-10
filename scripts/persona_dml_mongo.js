use persona_db

db.persona.insertMany([
	{
		"_id": NumberInt(123456789),
		"nombre": "Pepe",
		"apellido": "Perez",
		"genero": "M",
		"edad": NumberInt(30),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(987654321),
		"nombre": "Pepito",
		"apellido": "Perez",
		"genero": "M",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(321654987),
		"nombre": "Pepa",
		"apellido": "Juarez",
		"genero": "F",
		"edad": NumberInt(30),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(147258369),
		"nombre": "Pepita",
		"apellido": "Juarez",
		"genero": "F",
		"edad": NumberInt(10),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(963852741),
		"nombre": "Fede",
		"apellido": "Perez",
		"genero": "M",
		"edad": NumberInt(18),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	}
], { ordered: false })

db.profesion.insertMany([
	{ "_id": NumberInt(1), "nom": "Ingeniero de Sistemas", "des": "Diseña, desarrolla y mantiene sistemas de software", "_class": "co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument" },
	{ "_id": NumberInt(2), "nom": "Medico", "des": "Diagnostica y trata enfermedades de los pacientes", "_class": "co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument" },
	{ "_id": NumberInt(3), "nom": "Abogado", "des": "Asesora y representa a personas en asuntos juridicos", "_class": "co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument" }
], { ordered: false })

db.telefono.insertMany([
	{ "_id": "3001112233", "oper": "Claro", "primaryDuenio": NumberInt(123456789), "_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument" },
	{ "_id": "3104445566", "oper": "Movistar", "primaryDuenio": NumberInt(321654987), "_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument" },
	{ "_id": "3207778899", "oper": "Tigo", "primaryDuenio": NumberInt(963852741), "_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument" },
	{ "_id": "3159990011", "oper": "Claro", "primaryDuenio": NumberInt(987654321), "_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument" }
], { ordered: false })

db.estudios.insertMany([
	{ "_id": "123456789-1", "primaryPersona": NumberInt(123456789), "primaryProfesion": NumberInt(1), "fecha": ISODate("2015-06-30T05:00:00Z"), "univer": "Pontificia Universidad Javeriana", "_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument" },
	{ "_id": "963852741-1", "primaryPersona": NumberInt(963852741), "primaryProfesion": NumberInt(1), "fecha": ISODate("2018-03-15T05:00:00Z"), "univer": "Universidad de los Andes", "_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument" },
	{ "_id": "321654987-2", "primaryPersona": NumberInt(321654987), "primaryProfesion": NumberInt(2), "fecha": ISODate("2012-12-10T05:00:00Z"), "univer": "Universidad Nacional de Colombia", "_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument" },
	{ "_id": "147258369-3", "primaryPersona": NumberInt(147258369), "primaryProfesion": NumberInt(3), "fecha": ISODate("2020-11-20T05:00:00Z"), "univer": "Universidad Externado de Colombia", "_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument" }
], { ordered: false })
