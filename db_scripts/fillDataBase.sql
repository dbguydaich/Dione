
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
INSERT INTO `dbmysql05`.`movie` (`idMovie`, `idLanguage`, `idDirector`, `movieName`, `year`, `wiki`, `duration`, `plot`) VALUES ('3', '2', '1', 'Resorvior dogs', '1992', 'RD@movies.com', '135', 'WooHoo');
-- Users
INSERT INTO `dbmysql05`.`users` (`userName`, `hashPassword`) VALUES ('shachar the I', '123123');
INSERT INTO `dbmysql05`.`users` (`userName`, `hashPassword`) VALUES ('shachar the II', '321321');
INSERT INTO `dbmysql05`.`users` (`userName`, `hashPassword`) VALUES ('Matan', '1');
INSERT INTO `dbmysql05`.`users` (`userName`, `hashPassword`) VALUES ('Guy', '1');

-- Tags
INSERT INTO `dbmysql05`.`tag` (`idTag`, `tagName`) VALUES ('1', 'drama');
INSERT INTO `dbmysql05`.`tag` (`idTag`, `tagName`) VALUES ('2', 'comedy');
INSERT INTO `dbmysql05`.`tag` (`idTag`, `tagName`) VALUES ('3', 'horor');

-- movie_tags
INSERT INTO `dbmysql05`.`movie_tag` (`idMovie`, `idTag`) VALUES ('1', '1');
INSERT INTO `dbmysql05`.`movie_tag` (`idMovie`, `idTag`) VALUES ('2', '2');
INSERT INTO `dbmysql05`.`movie_tag` (`idMovie`, `idTag`) VALUES ('2', '1');
INSERT INTO `dbmysql05`.`movie_tag` (`idMovie`, `idTag`) VALUES ('2', '3');
INSERT INTO `dbmysql05`.`movie_tag` (`idMovie`, `idTag`) VALUES ('1', '3');
INSERT INTO `dbmysql05`.`movie_tag` (`idMovie`, `idTag`) VALUES ('3', '2');

--friend_relation
INSERT INTO `dbmysql05`.`friend_relation` (`friend1`, `friend2`, `friendshipDate`) VALUES ('1', '2', '2014-04-27 16:19:47');
INSERT INTO `dbmysql05`.`friend_relation` (`friend1`, `friend2`, `friendshipDate`) VALUES ('1', '3', '2014-04-24 16:19:47');
INSERT INTO `dbmysql05`.`friend_relation` (`friend1`, `friend2`, `friendshipDate`) VALUES ('4', '1', '2014-04-27 10:19:47');
INSERT INTO `dbmysql05`.`friend_relation` (`friend1`, `friend2`, `friendshipDate`) VALUES ('2', '5', '2014-04-26 16:19:47');

-- Commit
commit;