-- Persons
INSERT INTO `Dione`.`person` (`personName`) VALUES ('Quintine Tarantino');
INSERT INTO `Dione`.`person` (`personName`) VALUES ('Steven Schpilberg');
INSERT INTO `Dione`.`person` (`personName`) VALUES ('Brad Pitt');
INSERT INTO `Dione`.`person` (`personName`) VALUES ('Morgan frieman');

-- Directors
INSERT INTO `Dione`.`director` (`idPerson`) VALUES ('1');
INSERT INTO `Dione`.`director` (`idPerson`) VALUES ('2');

-- Actors
INSERT INTO `Dione`.`actor` (`idPerson`) VALUES ('1');
INSERT INTO `Dione`.`actor` (`idPerson`) VALUES ('3');
INSERT INTO `Dione`.`actor` (`idPerson`) VALUES ('4');

-- Languages
INSERT INTO `Dione`.`language` (`LanguageName`) VALUES ('English');
INSERT INTO `Dione`.`language` (`LanguageName`) VALUES ('French');
INSERT INTO `Dione`.`language` (`LanguageName`) VALUES ('Italian');
INSERT INTO `Dione`.`language` (`LanguageName`) VALUES ('Hebrew');

-- Movies
INSERT INTO `Dione`.`movie` (`idLanguage`, `idDirector`, `movieName`, `movie_qualified_name`,`year`, `wiki`, `duration`, `plot`) VALUES ('1', '1', 'Pulp fiction', 'Pulp fiction', '1995', 'a@a.com', '150', 'aaasdf');
INSERT INTO `Dione`.`movie` (`idLanguage`, `idDirector`, `movieName`, `movie_qualified_name`, `year`, `wiki`, `duration`, `plot`) VALUES ('1', '2', 'E.T', 'E.E', '1989', 'et@movies.com', '135', 'fasdfsa');
INSERT INTO `Dione`.`movie` (`idLanguage`, `idDirector`, `movieName`, `movie_qualified_name`, `year`, `wiki`, `duration`, `plot`) VALUES ('2', '1', 'Resorvior dogs', 'Resorvior dogs', '1992', 'RD@movies.com', '135', 'WooHoo');

-- Users
INSERT INTO `Dione`.`users` (`userName`, `hashPassword`) VALUES ('w', '119');
INSERT INTO `Dione`.`users` (`userName`, `hashPassword`) VALUES ('shachar th', '321321');
INSERT INTO `Dione`.`users` (`userName`, `hashPassword`) VALUES ('MatanPoleg', '1');
INSERT INTO `Dione`.`users` (`userName`, `hashPassword`) VALUES ('Guy', '1');

-- Genre
INSERT INTO `Dione`.`genre` (`genreName`) VALUES ('Drama');
INSERT INTO `Dione`.`genre` (`genreName`) VALUES ('Action');
INSERT INTO `Dione`.`genre` (`genreName`) VALUES ('Comedy');
INSERT INTO `Dione`.`genre` (`genreName`) VALUES ('Horor');

-- Tags
INSERT INTO `Dione`.`tag` (`tagName`) VALUES ('1950s');
INSERT INTO `Dione`.`tag` (`tagName`) VALUES ('North-America');
INSERT INTO `Dione`.`tag` (`tagName`) VALUES ('disgusting');
INSERT INTO `Dione`.`tag` (`tagName`) VALUES ('chick flick');

-- actor_movie
INSERT INTO `Dione`.`actor_movie` (`idMovie`, `idActor`) VALUES ('1', '1');
INSERT INTO `Dione`.`actor_movie` (`idMovie`, `idActor`) VALUES ('1', '3');
INSERT INTO `Dione`.`actor_movie` (`idMovie`, `idActor`) VALUES ('2', '3');
INSERT INTO `Dione`.`actor_movie` (`idMovie`, `idActor`) VALUES ('2', '4');

-- friend_relation
INSERT INTO `Dione`.`friend_relation` (`friend1`, `friend2`, `friendshipDate`) VALUES ('1', '2', '2014-04-27 16:19:47');
INSERT INTO `Dione`.`friend_relation` (`friend1`, `friend2`, `friendshipDate`) VALUES ('1', '3', '2014-04-24 16:19:47');
INSERT INTO `Dione`.`friend_relation` (`friend1`, `friend2`, `friendshipDate`) VALUES ('4', '1', '2014-04-27 10:19:47');
INSERT INTO `Dione`.`friend_relation` (`friend1`, `friend2`, `friendshipDate`) VALUES ('2', '4', '2014-04-26 16:19:47');

-- genre_movie
INSERT INTO `Dione`.`genre_movie` (`idMovie`, `idGenre`) VALUES ('1', '2');
INSERT INTO `Dione`.`genre_movie` (`idMovie`, `idGenre`) VALUES ('2', '1');
INSERT INTO `Dione`.`genre_movie` (`idMovie`, `idGenre`) VALUES ('2', '4');
INSERT INTO `Dione`.`genre_movie` (`idMovie`, `idGenre`) VALUES ('3', '2');
INSERT INTO `Dione`.`genre_movie` (`idMovie`, `idGenre`) VALUES ('3', '3');

-- movie_tags
INSERT INTO `Dione`.`movie_tag` (`idMovie`, `idTag`) VALUES ('1', '1');
INSERT INTO `Dione`.`movie_tag` (`idMovie`, `idTag`) VALUES ('2', '2');
INSERT INTO `Dione`.`movie_tag` (`idMovie`, `idTag`) VALUES ('2', '1');
INSERT INTO `Dione`.`movie_tag` (`idMovie`, `idTag`) VALUES ('2', '3');
INSERT INTO `Dione`.`movie_tag` (`idMovie`, `idTag`) VALUES ('1', '3');
INSERT INTO `Dione`.`movie_tag` (`idMovie`, `idTag`) VALUES ('3', '2');
INSERT INTO `Dione`.`movie_tag` (`idMovie`, `idTag`) VALUES ('1', '4');

-- user_prefence
INSERT INTO `Dione`.`user_prefence` (`idUser`, `idTag`, `tag_user_rate`) VALUES ('1', '1', '5');
INSERT INTO `Dione`.`user_prefence` (`idUser`, `idTag`, `tag_user_rate`) VALUES ('1', '2', '0');
INSERT INTO `Dione`.`user_prefence` (`idUser`, `idTag`, `tag_user_rate`) VALUES ('1', '3', '-1');
INSERT INTO `Dione`.`user_prefence` (`idUser`, `idTag`, `tag_user_rate`) VALUES ('1', '4', '-5');
INSERT INTO `Dione`.`user_prefence` (`idUser`, `idTag`, `tag_user_rate`) VALUES ('2', '2', '4');
INSERT INTO `Dione`.`user_prefence` (`idUser`, `idTag`, `tag_user_rate`) VALUES ('2', '1', '0');
INSERT INTO `Dione`.`user_prefence` (`idUser`, `idTag`, `tag_user_rate`) VALUES ('2', '4', '-3');

-- user_rank
INSERT INTO `Dione`.`user_rank` (`idUser`, `idMovie`, `rank`, `rankDate`) VALUES ('1', '1', '1', '2014-04-26 17:19:47');
INSERT INTO `Dione`.`user_rank` (`idUser`, `idMovie`, `rank`, `rankDate`) VALUES ('1', '2', '2', '2014-05-26 16:19:00');
INSERT INTO `Dione`.`user_rank` (`idUser`, `idMovie`, `rank`, `rankDate`) VALUES ('2', '3', '3', '2014-04-11 18:10:47');
INSERT INTO `Dione`.`user_rank` (`idUser`, `idMovie`, `rank`, `rankDate`) VALUES ('2', '1', '3', '2014-04-17 22:19:47');


-- Commit
commit;