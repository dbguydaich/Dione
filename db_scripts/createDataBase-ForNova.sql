SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

USE `DbMysql10` ;

-- -----------------------------------------------------
-- Table `invocations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `invocations` (
  `invokeCode` TINYINT NOT NULL,
  `invokeDate` DATETIME NOT NULL,
  `didFinish` TINYINT NOT NULL,
  PRIMARY KEY (`invokeCode`, `invokeDate`))
ENGINE = MyISAM;

-- -----------------------------------------------------
-- Table `person`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `person` (
  `idPerson` INT NOT NULL AUTO_INCREMENT,
  `personName` VARCHAR(25) NOT NULL,
  PRIMARY KEY (`idPerson`),
  INDEX `personName_idx` USING BTREE (`personName` ASC))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `actor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `actor` (
  `idPerson` INT NOT NULL,
  PRIMARY KEY (`idPerson`),
  CONSTRAINT `idPersonActor`
    FOREIGN KEY (`idPerson`)
    REFERENCES `person` (`idPerson`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `director`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `director` (
  `idPerson` INT NOT NULL,
  PRIMARY KEY (`idPerson`),
  CONSTRAINT `idPersonDirector`
    FOREIGN KEY (`idPerson`)
    REFERENCES `person` (`idPerson`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `language`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `language` (
  `idLanguage` TINYINT NOT NULL AUTO_INCREMENT,
  `languageName` CHAR(20) NOT NULL,
  UNIQUE INDEX `user_lang_unq` (`languageName` ASC),
  PRIMARY KEY (`idLanguage`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `movie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `movie` (
  `idMovie` INT NOT NULL AUTO_INCREMENT,
  `idLanguage` TINYINT NULL DEFAULT NULL,
  `idDirector` INT NULL DEFAULT 1,
  `movieName` VARCHAR(128) NOT NULL,
  `movie_qualified_name` VARCHAR(100) NULL DEFAULT NULL,
  `year` SMALLINT NULL DEFAULT NULL,
  `wiki` TEXT NULL DEFAULT NULL,
  `duration` SMALLINT NULL DEFAULT NULL,
  `plot` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`idMovie`),
  INDEX `idLanguage_idx` (`idLanguage` ASC),
  INDEX `idDirector_idx` (`idDirector` ASC),
  INDEX `movieName` (`movieName` (128)),
  CONSTRAINT `idDirector`
    FOREIGN KEY (`idDirector`)
    REFERENCES `director` (`idPerson`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT,
  CONSTRAINT `idLanguage`
    FOREIGN KEY (`idLanguage`)
    REFERENCES `language` (`idLanguage`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `users` (
  `idUsers` INT NOT NULL AUTO_INCREMENT,
  `userName` CHAR(10) NOT NULL,
  `hashPassword` INT NOT NULL,
  PRIMARY KEY (`idUsers`),
   UNIQUE INDEX `user_name_unq` (`userName` ASC))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `genre`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `genre` (
  `idGenre` SMALLINT NOT NULL AUTO_INCREMENT,
  `genreName` CHAR(20) NOT NULL,
  PRIMARY KEY (`idGenre`),
   UNIQUE INDEX `genre_name_unq` (`genreName` ASC))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tag` (
  `idTag` SMALLINT NOT NULL AUTO_INCREMENT,
  `tagName` VARCHAR(25) NOT NULL,
  PRIMARY KEY (`idtag`),
   UNIQUE INDEX `tag_name_unq` (`tagName` ASC),
  INDEX `tagName_idx` USING BTREE (`tagName` ASC))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `actor_movie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `actor_movie` (
  `idMovie` INT NOT NULL AUTO_INCREMENT,
  `idActor` INT NOT NULL,
  INDEX `idMovie_idx` (`idMovie` ASC),
  INDEX `idActor_idx` (`idActor` ASC),
  PRIMARY KEY (`idMovie`, `idActor`),
  CONSTRAINT `idActor`
    FOREIGN KEY (`idActor`)
    REFERENCES `actor` (`idPerson`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT,
  CONSTRAINT `idMovie`
    FOREIGN KEY (`idMovie`)
    REFERENCES `movie` (`idMovie`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `friend_relation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `friend_relation` (
  `friend1` INT NOT NULL,
  `friend2` INT NOT NULL,
  `friendshipDate` DATETIME NOT NULL,
  PRIMARY KEY (`friend1`, `friend2`),
  CONSTRAINT `friend1_fk`
    FOREIGN KEY (`friend1`)
    REFERENCES `users` (`idUsers`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT,
  CONSTRAINT `friend2_fk`
    FOREIGN KEY (`friend2`)
    REFERENCES `users` (`idUsers`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `movie_notes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `movie_tag` (
  `idMovie` INT NOT NULL,
  `idUser` SMALLINT NOT NULL,
  `noteDate` DATETIME NOT NULL,
  `note` VARCHAR(144) NOT NULL,
  PRIMARY KEY (`idmovie`, `idUser`, `noteDate`),
  INDEX `fk_tag_idx` (`idtag` ASC),
  CONSTRAINT `fk_movie_onNotes`
    FOREIGN KEY (`idMovie`)
    REFERENCES `movie` (`idMovie`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_user_onNotes`
    FOREIGN KEY (`idUser`)
    REFERENCES `users` (`idUsers`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `genre_movie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `genre_movie` (
  `idMovie` INT NOT NULL,
  `idGenre` SMALLINT NOT NULL,
  PRIMARY KEY (`idMovie`, `idGenre`),
  INDEX `idGenre_idx` (`idGenre` ASC),
  INDEX `idMovie_idx` (`idMovie` ASC),
  CONSTRAINT `idGenre`
    FOREIGN KEY (`idGenre`)
    REFERENCES `genre` (`idGenre`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT,
  CONSTRAINT `idMovie1`
    FOREIGN KEY (`idMovie`)
    REFERENCES `movie` (`idMovie`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `movie_tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `movie_tag` (
  `idMovie` INT NOT NULL,
  `idTag` SMALLINT NOT NULL,
  PRIMARY KEY (`idmovie`, `idtag`),
  INDEX `fk_tag_idx` (`idtag` ASC),
  CONSTRAINT `fk_movie`
    FOREIGN KEY (`idMovie`)
    REFERENCES `movie` (`idMovie`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_tag`
    FOREIGN KEY (`idTag`)
    REFERENCES `tag` (`idTag`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `user_prefence`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_prefence` (
  `idUser` INT NOT NULL,
  `idTag` SMALLINT NOT NULL,
  `tag_user_rate` INT NOT NULL,
  PRIMARY KEY (`idUser`, `idTag`),
  INDEX `tag_id_fk_user_prefences_idx` (`idTag` ASC),
  CONSTRAINT `tag_id_fk_user_prefences`
    FOREIGN KEY (`idTag`)
    REFERENCES `tag` (`idtag`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT,
  CONSTRAINT `user_id_fk_user_prefences`
    FOREIGN KEY (`idUser`)
    REFERENCES `users` (`idUsers`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `user_rank`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_rank` (
  `idUser` INT NOT NULL,
  `idMovie` INT NOT NULL,
  `rank` TINYINT NULL DEFAULT NULL,
  `rankDate` DATETIME NOT NULL,
  PRIMARY KEY (`idUser`, `idMovie`),
  INDEX `movie_fk_idx` (`idMovie` ASC),
  CONSTRAINT `movie_fk`
    FOREIGN KEY (`idMovie`)
    REFERENCES `movie` (`idMovie`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `user_tag_movie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_tag_movie` (
  `idUser` INT NOT NULL,
  `idTag` SMALLINT NOT NULL,
  `idMovie` INT NOT NULL,
  `rate` TINYINT NOT NULL,
  `reteDate` DATETIME NOT NULL,
  PRIMARY KEY (`idUser`, `idTag`, `idMovie`),
  INDEX `movie_fk_user_tags_movie_rating_idx` (`idMovie` ASC),
  INDEX `tag_fk_user_tags_movie_rating_idx` (`idTag` ASC),
  CONSTRAINT `movie_fk_user_tags_movie_rating`
    FOREIGN KEY (`idMovie`)
    REFERENCES `movie` (`idMovie`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT,
  CONSTRAINT `tag_fk_user_tags_movie_rating`
    FOREIGN KEY (`idTag`)
    REFERENCES `tag` (`idtag`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT,
  CONSTRAINT `user_fk_user_tags_movie_rating`
    FOREIGN KEY (`idUser`)
    REFERENCES `users` (`idUsers`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `movie_tag_rate`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `movie_tag_rate` (
  `idMovie` SMALLINT NOT NULL,
  `idTag` INT NOT NULL,
  `rate` TINYINT NOT NULL,
  PRIMARY KEY (`idTag`, `idMovie`))
ENGINE = InnoDB;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
