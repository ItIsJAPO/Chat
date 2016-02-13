/*
 Fecha: 28-Enero-2016

 Transact necesario para la aplicacion de CHAT en JAVA, libre de modificar.alterar
 
 Autor: José Antonio Pino Ocampo
 Autor: Juan Carlos Almeyda Cruz
 Autor: Pablo Gaddiel Carrillo Guerrero
 Autor: Otilio Guevara Dominguez
 
 Licencia MIT.
*/
CREATE DATABASE IF NOT EXISTS UserCHAT;
USE UserCHAT;

-- DROP USER 'userandy'@'localhost';
GRANT ALL ON UserCHAT.* TO userandy@'%' IDENTIFIED BY 'userandy';
FLUSH PRIVILEGES;

DROP TABLE IF EXISTS AMIGOS;
CREATE TABLE AMIGOS (
    User1 VARCHAR(45) NOT NULL,
    User2 VARCHAR(45) NOT NULL
);

DROP TABLE IF EXISTS LOGLOGIN;
CREATE TABLE LOGLOGIN (
    User VARCHAR(45) NOT NULL,
    FechaHoraInicio DATETIME DEFAULT NULL
);

DROP TABLE IF EXISTS LOGPENALIZACION;
CREATE TABLE LOGPENALIZACION (
    User VARCHAR(45) NOT NULL,
    Descripcion VARCHAR(45) DEFAULT NULL,
    FechaFin DATE DEFAULT NULL
);

DROP TABLE IF EXISTS PUNTAJE;
CREATE TABLE PUNTAJE (
    User VARCHAR(45) NOT NULL,
    Ganados INT(11) DEFAULT '0',
    Empatados INT(11) DEFAULT '0',
    Perdidos INT(11) DEFAULT '0'
);

DROP TABLE IF EXISTS USUARIOS;
CREATE TABLE USUARIOS (
  User varchar(45) DEFAULT NULL,
  Pass varchar(45) DEFAULT NULL,
  Apyn varchar(45) DEFAULT NULL,
  Mail varchar(45) DEFAULT NULL,
  Telefono varchar(45) DEFAULT NULL,
  FechaAlta date DEFAULT NULL,
  FechaNacimiento date DEFAULT NULL,
  Conectado tinytext
) ;

SELECT ' ¡Listo! ' as Estado;