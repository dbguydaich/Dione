
-- Languages
INSERT INTO `dbmysql05`.`language` (`idLanguage`, `LanguageName`) VALUES ('1', 'English');
INSERT INTO `dbmysql05`.`language` (`idLanguage`, `LanguageName`) VALUES ('2', 'French');
INSERT INTO `dbmysql05`.`language` (`idLanguage`, `LanguageName`) VALUES ('3', 'Italian');
INSERT INTO `dbmysql05`.`language` (`idLanguage`, `LanguageName`) VALUES ('4', 'Hebrew');

-- Persons
INSERT INTO `dbmysql05`.`person` (`idPerson`, `personName`) VALUES ('1', 'Quintine Tarantino');
INSERT INTO `dbmysql05`.`person` (`idPerson`, `personName`) VALUES ('2', 'Steven Schpilberg');
INSERT INTO `dbmysql05`.`person` (`idPerson`, `personName`) VALUES ('3', 'Brad Pitt');

-- Directors
INSERT INTO `dbmysql05`.`director` (`idDirector`, `idPerson`) VALUES ('1', '1');
INSERT INTO `dbmysql05`.`director` (`idDirector`, `idPerson`) VALUES ('2', '2');

-- Actors
INSERT INTO `dbmysql05`.`actor` (`idActor`, `idPerson`) VALUES ('1', '1');
INSERT INTO `dbmysql05`.`actor` (`idActor`, `idPerson`) VALUES ('2', '3');

-- Movies
INSERT INTO `dbmysql05`.`movie` (`idMovie`, `idLanguage`, `idDirector`, `movieName`, `year`, `wiki`, `duration`, `plot`) VALUES ('1', '1', '1', 'Pulp fiction', '1995', 'a@a.com', '150', 'aaasdf');
INSERT INTO `dbmysql05`.`movie` (`idMovie`, `idLanguage`, `idDirector`, `movieName`, `year`, `wiki`, `duration`, `plot`) VALUES ('2', '1', '2', 'E.T', '1989', 'et@movies.com', '135', 'fasdfsa');

-- Users
INSERT INTO `dbmysql05`.`users` (`idUsers`, `userName`, `userPassword`, `hashPassword`) VALUES ('1', 'shachar the I', '123', '123123');
INSERT INTO `dbmysql05`.`users` (`idUsers`, `userName`, `userPassword`, `hashPassword`) VALUES ('2', 'shachar the II', '321', '321321');
INSERT INTO `dbmysql05`.`users` (`idUsers`, `userName`, `userPassword`, `hashPassword`) VALUES ('3', 'Matan', '1', '1');
INSERT INTO `dbmysql05`.`users` (`idUsers`, `userName`, `userPassword`, `hashPassword`) VALUES ('4', 'Guy', 'asdf', '1');
