
-- Persons
INSERT INTO `dbmysql05`.`person` (`personName`) VALUES ('Quintine Tarantino');
INSERT INTO `dbmysql05`.`person` (`personName`) VALUES ('Steven Schpilberg');
INSERT INTO `dbmysql05`.`person` (`personName`) VALUES ('Brad Pitt');
INSERT INTO `dbmysql05`.`person` (`personName`) VALUES ('Morgan frieman');

-- Directors
INSERT INTO `dbmysql05`.`director` (`idPerson`) VALUES ('1');
INSERT INTO `dbmysql05`.`director` (`idPerson`) VALUES ('2');

-- Actors
INSERT INTO `dbmysql05`.`actor` (`idPerson`) VALUES ('1');
INSERT INTO `dbmysql05`.`actor` (`idPerson`) VALUES ('3');
INSERT INTO `dbmysql05`.`actor` (`idPerson`) VALUES ('4');

-- Languages
INSERT INTO `dbmysql05`.`language` (`LanguageName`) VALUES ('English');
INSERT INTO `dbmysql05`.`language` (`LanguageName`) VALUES ('French');
INSERT INTO `dbmysql05`.`language` (`LanguageName`) VALUES ('Italian');
INSERT INTO `dbmysql05`.`language` (`LanguageName`) VALUES ('Hebrew');

-- Movies
INSERT INTO `dbmysql05`.`movie` (`idLanguage`, `idDirector`, `movieName`, `year`, `wiki`, `duration`, `plot`) VALUES ('1', '1', 'Pulp fiction', '1995', 'a@a.com', '150', 'aaasdf');
INSERT INTO `dbmysql05`.`movie` (`idLanguage`, `idDirector`, `movieName`, `year`, `wiki`, `duration`, `plot`) VALUES ('1', '2', 'E.T', '1989', 'et@movies.com', '135', 'fasdfsa');
INSERT INTO `dbmysql05`.`movie` (`idLanguage`, `idDirector`, `movieName`, `year`, `wiki`, `duration`, `plot`) VALUES ('2', '1', 'Resorvior dogs', '1992', 'RD@movies.com', '135', 'WooHoo');

-- Users
INSERT INTO `dbmysql05`.`users` (`userName`, `hashPassword`) VALUES ('shachar the I', '123123');
INSERT INTO `dbmysql05`.`users` (`userName`, `hashPassword`) VALUES ('shachar the II', '321321');
INSERT INTO `dbmysql05`.`users` (`userName`, `hashPassword`) VALUES ('Matan', '1');
INSERT INTO `dbmysql05`.`users` (`userName`, `hashPassword`) VALUES ('Guy', '1');

-- Genre
INSERT INTO `dbmysql05`.`genre` (`genreName`) VALUES ('Drama');
INSERT INTO `dbmysql05`.`genre` (`genreName`) VALUES ('Action');
INSERT INTO `dbmysql05`.`genre` (`genreName`) VALUES ('Comedy');
INSERT INTO `dbmysql05`.`genre` (`genreName`) VALUES ('Horor');

-- Tags
INSERT INTO `dbmysql05`.`tag` (`tagName`) VALUES ('1950s');
INSERT INTO `dbmysql05`.`tag` (`tagName`) VALUES ('North-America');
INSERT INTO `dbmysql05`.`tag` (`tagName`) VALUES ('disgusting');
INSERT INTO `dbmysql05`.`tag` (`tagName`) VALUES ('chick flick');

-- actor_movie
INSERT INTO `dbmysql05`.`actor_movie` (`idMovie`, `idActor`) VALUES ('1', '1');
INSERT INTO `dbmysql05`.`actor_movie` (`idMovie`, `idActor`) VALUES ('1', '3');
INSERT INTO `dbmysql05`.`actor_movie` (`idMovie`, `idActor`) VALUES ('2', '3');
INSERT INTO `dbmysql05`.`actor_movie` (`idMovie`, `idActor`) VALUES ('2', '4');

-- friend_relation
INSERT INTO `dbmysql05`.`friend_relation` (`friend1`, `friend2`, `friendshipDate`) VALUES ('1', '2', '2014-04-27 16:19:47');
INSERT INTO `dbmysql05`.`friend_relation` (`friend1`, `friend2`, `friendshipDate`) VALUES ('1', '3', '2014-04-24 16:19:47');
INSERT INTO `dbmysql05`.`friend_relation` (`friend1`, `friend2`, `friendshipDate`) VALUES ('4', '1', '2014-04-27 10:19:47');
INSERT INTO `dbmysql05`.`friend_relation` (`friend1`, `friend2`, `friendshipDate`) VALUES ('2', '4', '2014-04-26 16:19:47');

-- genre_movie
INSERT INTO `dbmysql05`.`genre_movie` (`idMovie`, `idGenre`) VALUES ('1', '2');
INSERT INTO `dbmysql05`.`genre_movie` (`idMovie`, `idGenre`) VALUES ('2', '1');
INSERT INTO `dbmysql05`.`genre_movie` (`idMovie`, `idGenre`) VALUES ('2', '4');
INSERT INTO `dbmysql05`.`genre_movie` (`idMovie`, `idGenre`) VALUES ('3', '2');
INSERT INTO `dbmysql05`.`genre_movie` (`idMovie`, `idGenre`) VALUES ('3', '3');

-- movie_tags
INSERT INTO `dbmysql05`.`movie_tag` (`idMovie`, `idTag`) VALUES ('1', '1');
INSERT INTO `dbmysql05`.`movie_tag` (`idMovie`, `idTag`) VALUES ('2', '2');
INSERT INTO `dbmysql05`.`movie_tag` (`idMovie`, `idTag`) VALUES ('2', '1');
INSERT INTO `dbmysql05`.`movie_tag` (`idMovie`, `idTag`) VALUES ('2', '3');
INSERT INTO `dbmysql05`.`movie_tag` (`idMovie`, `idTag`) VALUES ('1', '3');
INSERT INTO `dbmysql05`.`movie_tag` (`idMovie`, `idTag`) VALUES ('3', '2');
INSERT INTO `dbmysql05`.`movie_tag` (`idMovie`, `idTag`) VALUES ('1', '4');

-- user_prefence
INSERT INTO `dbmysql05`.`user_prefence` (`idUser`, `idTag`, `tag_user_rate`) VALUES ('1', '1', '5');
INSERT INTO `dbmysql05`.`user_prefence` (`idUser`, `idTag`, `tag_user_rate`) VALUES ('1', '2', '0');
INSERT INTO `dbmysql05`.`user_prefence` (`idUser`, `idTag`, `tag_user_rate`) VALUES ('1', '3', '-1');
INSERT INTO `dbmysql05`.`user_prefence` (`idUser`, `idTag`, `tag_user_rate`) VALUES ('1', '4', '-5');
INSERT INTO `dbmysql05`.`user_prefence` (`idUser`, `idTag`, `tag_user_rate`) VALUES ('2', '2', '4');
INSERT INTO `dbmysql05`.`user_prefence` (`idUser`, `idTag`, `tag_user_rate`) VALUES ('2', '1', '0');
INSERT INTO `dbmysql05`.`user_prefence` (`idUser`, `idTag`, `tag_user_rate`) VALUES ('2', '4', '-3');

-- user_rank
INSERT INTO `dbmysql05`.`user_rank` (`idUser`, `idMovie`, `rank`, `rankDate`) VALUES ('1', '1', '1', '2014-04-26 17:19:47');
INSERT INTO `dbmysql05`.`user_rank` (`idUser`, `idMovie`, `rank`, `rankDate`) VALUES ('1', '2', '2', '2014-05-26 16:19:00');
INSERT INTO `dbmysql05`.`user_rank` (`idUser`, `idMovie`, `rank`, `rankDate`) VALUES ('2', '3', '3', '2014-04-11 18:10:47');
INSERT INTO `dbmysql05`.`user_rank` (`idUser`, `idMovie`, `rank`, `rankDate`) VALUES ('2', '1', '3', '2014-04-17 22:19:47');


-- Commit
commit;