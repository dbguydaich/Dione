-- Persons
INSERT INTO `person` (`idPerson`, `personName`) VALUES ('1', 'Not Listed');

-- Directors
INSERT INTO `director` (`idPerson`) VALUES ('1');

-- Actors
-- Languages
-- Movies
-- Users
INSERT INTO `users` (`userName`, `hashPassword`) VALUES ('admin', '119');
INSERT INTO `users` (`userName`, `hashPassword`) VALUES ('user', '119');

-- Genre
-- Tags
-- actor_movie
-- friend_relation
INSERT INTO `friend_relation` (`friend1`, `friend2`, `friendshipDate`) VALUES ('1', '2', '2014-04-27 16:19:47');

-- genre_movie
-- movie_tags
-- user_prefence
-- user_rank
-- Commit

commit;