-- Persons
INSERT INTO `Dione`.`person` (`idPerson`, `personName`) VALUES ('1', 'Not Listed');

-- Directors
INSERT INTO `Dione`.`director` (`idPerson`) VALUES ('1');

-- Actors
INSERT INTO `Dione`.`actor` (`idPerson`) VALUES ('1');

-- Languages
INSERT INTO `Dione`.`language` (`idLanguage`, `LanguageName`) VALUES ('0', 'Not Listed');

-- Movies

-- Users
INSERT INTO `Dione`.`users` (`userName`, `hashPassword`) VALUES ('w', '119');
INSERT INTO `Dione`.`users` (`userName`, `hashPassword`) VALUES ('MatanPoleg', '1');
INSERT INTO `Dione`.`users` (`userName`, `hashPassword`) VALUES ('Guy', '1');
INSERT INTO `Dione`.`users` (`userName`, `hashPassword`) VALUES ('q', '119');

-- Genre

-- Tags

-- actor_movie

-- friend_relation
INSERT INTO `Dione`.`friend_relation` (`friend1`, `friend2`, `friendshipDate`) VALUES ('1', '2', '2014-04-27 16:19:47');
INSERT INTO `Dione`.`friend_relation` (`friend1`, `friend2`, `friendshipDate`) VALUES ('1', '3', '2014-04-24 16:19:47');
INSERT INTO `Dione`.`friend_relation` (`friend1`, `friend2`, `friendshipDate`) VALUES ('4', '1', '2014-04-27 10:19:47');
INSERT INTO `Dione`.`friend_relation` (`friend1`, `friend2`, `friendshipDate`) VALUES ('2', '4', '2014-04-26 16:19:47');

-- genre_movie

-- movie_tags

-- user_prefence

-- user_rank

-- Commit
commit;