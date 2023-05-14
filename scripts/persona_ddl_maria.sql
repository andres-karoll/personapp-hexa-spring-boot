FLUSH PRIVILEGES;
--
DROP USER IF EXISTS 'persona_db'@'%';
DROP SCHEMA IF EXISTS `persona_db`;
--
CREATE USER IF NOT EXISTS 'persona_db'@'%' IDENTIFIED BY 'persona_db';
CREATE SCHEMA IF NOT EXISTS `persona_db`; 
--
GRANT EXECUTE, TRIGGER, INSERT, UPDATE, DELETE, SELECT ON `persona_db`.* TO 'persona_db'@'%'; 
FLUSH PRIVILEGES; 
--
USE `persona_db`;
--
CREATE TABLE IF NOT EXISTS `persona_db`.`persona` (
 `cc` INT(15) NOT NULL,
 `nombre` VARCHAR(45) NOT NULL,
 `apellido` VARCHAR(45) NOT NULL,
 `genero` ENUM('M', 'F') NOT NULL,
 `edad` INT(3) NULL DEFAULT NULL, 
 CONSTRAINT `persona_pk` PRIMARY KEY (`cc`)
);
--
CREATE TABLE IF NOT EXISTS `persona_db`.`profesion` (
 `id` INT(6) NOT NULL,
 `nom` VARCHAR(90) NOT NULL,
 `des` TEXT NULL DEFAULT NULL, 
 CONSTRAINT `profesion_pk` PRIMARY KEY (`id`)
);
--
CREATE TABLE IF NOT EXISTS `persona_db`.`telefono` (
 `num` VARCHAR(15) NOT NULL,
 `oper` VARCHAR(45) NOT NULL,
 `duenio` INT(15) NOT NULL, 
 CONSTRAINT `telefono_pk` PRIMARY KEY (`num`), 
 CONSTRAINT `telefono_persona_fk` FOREIGN KEY (`duenio`) REFERENCES `persona_db`.`persona` (`cc`)
);
--
CREATE TABLE IF NOT EXISTS `persona_db`.`estudios` (
 `id_prof` INT(6) NOT NULL,
 `cc_per` INT(15) NOT NULL,
 `fecha` DATE NULL DEFAULT NULL,
 `univer` VARCHAR(50) NULL DEFAULT NULL, 
 CONSTRAINT `estudios_pk` PRIMARY KEY (`id_prof`, `cc_per`),
 CONSTRAINT `estudio_persona_fk` FOREIGN KEY (`cc_per`) REFERENCES `persona_db`.`persona` (`cc`), 
 CONSTRAINT `estudio_profesion_fk` FOREIGN KEY (`id_prof`) REFERENCES `persona_db`.`profesion` (`id`)
);
--
COMMIT;
FLUSH PRIVILEGES;