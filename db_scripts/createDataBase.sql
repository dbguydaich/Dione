SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `Dione` DEFAULT CHARACTER SET utf8 ;
USE `Dione` ;

-- -----------------------------------------------------
-- Table `Dione`.`invocations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Dione`.`invocations` (
  `invokeCode` TINYINT NOT NULL,
  `invokeDate` DATETIME NOT NULL,
  PRIMARY KEY (`invokeCode`, `invokeDate`))
ENGINE = InnoDB	-- used InnoDB because this should have more writes than selects
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `Dione`.`person`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Dione`.`person` (
  `idPerson` INT NOT NULL AUTO_INCREMENT,
  `personName` VARCHAR(25) NOT NULL,
  PRIMARY KEY (`idPerson`),
  INDEX `personName_idx` USING BTREE (`personName` ASC))
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `Dione`.`actor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Dione`.`actor` (
  `idPerson` INT NOT NULL,
  PRIMARY KEY (`idPerson`),
  CONSTRAINT `idPersonActor`
    FOREIGN KEY (`idPerson`)
    REFERENCES `Dione`.`person` (`idPerson`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `Dione`.`director`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Dione`.`director` (
  `idPerson` INT NOT NULL,
  PRIMARY KEY (`idPerson`),
  CONSTRAINT `idPersonDirector`
    FOREIGN KEY (`idPerson`)
    REFERENCES `Dione`.`person` (`idPerson`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `Dione`.`language`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Dione`.`language` (
  `idLanguage` INT NOT NULL AUTO_INCREMENT,
  `languageName` CHAR(20) NOT NULL,
  UNIQUE INDEX `user_lang_unq` (`languageName` ASC),
  PRIMARY KEY (`idLanguage`))
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `Dione`.`movie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Dione`.`movie` (
  `idMovie` INT NOT NULL AUTO_INCREMENT,
  `idLanguage` INT NULL DEFAULT NULL,
  `idDirector` INT NULL DEFAULT NULL,
  `movieName` VARCHAR(128) NOT NULL,
  `movie_qualified_name` VARCHAR(100) NULL DEFAULT NULL,
  `year` INT NULL DEFAULT NULL,
  `wiki` TEXT NULL DEFAULT NULL,
  `duration` INT NULL DEFAULT NULL,
  `plot` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`idMovie`),
  INDEX `idLanguage_idx` (`idLanguage` ASC),
  INDEX `idDirector_idx` (`idDirector` ASC),
  INDEX `movieName` (`movieName` (128)),
  CONSTRAINT `idDirector`
    FOREIGN KEY (`idDirector`)
    REFERENCES `Dione`.`director` (`idPerson`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `idLanguage`
    FOREIGN KEY (`idLanguage`)
    REFERENCES `Dione`.`language` (`idLanguage`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `Dione`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Dione`.`users` (
  `idUsers` INT NOT NULL AUTO_INCREMENT,
  `userName` CHAR(10) NOT NULL,
  `hashPassword` INT NOT NULL,
  PRIMARY KEY (`idUsers`),
   UNIQUE INDEX `user_name_unq` (`userName` ASC))
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `Dione`.`genre`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Dione`.`genre` (
  `idGenre` INT NOT NULL AUTO_INCREMENT,
  `genreName` CHAR(20) NOT NULL,
  PRIMARY KEY (`idGenre`),
   UNIQUE INDEX `genre_name_unq` (`genreName` ASC))
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `Dione`.`tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Dione`.`tag` (
  `idTag` INT NOT NULL AUTO_INCREMENT,
  `tagName` VARCHAR(25) NOT NULL,
  PRIMARY KEY (`idtag`),
   UNIQUE INDEX `tag_name_unq` (`tagName` ASC),
  INDEX `tagName_idx` USING BTREE (`tagName` ASC))
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `Dione`.`actor_movie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Dione`.`actor_movie` (
  `idMovie` INT NOT NULL AUTO_INCREMENT,
  `idActor` INT NOT NULL,
  INDEX `idMovie_idx` (`idMovie` ASC),
  INDEX `idActor_idx` (`idActor` ASC),
  PRIMARY KEY (`idMovie`, `idActor`),
  CONSTRAINT `idActor`
    FOREIGN KEY (`idActor`)
    REFERENCES `Dione`.`actor` (`idPerson`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `idMovie`
    FOREIGN KEY (`idMovie`)
    REFERENCES `Dione`.`movie` (`idMovie`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `Dione`.`friend_relation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Dione`.`friend_relation` (
  `friend1` INT NOT NULL,
  `friend2` INT NOT NULL,
  `friendshipDate` DATETIME NOT NULL,
  PRIMARY KEY (`friend1`, `friend2`),
  CONSTRAINT `friend1_fk`
    FOREIGN KEY (`friend1`)
    REFERENCES `Dione`.`users` (`idUsers`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `friend2_fk`
    FOREIGN KEY (`friend2`)
    REFERENCES `Dione`.`users` (`idUsers`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `Dione`.`genre_movie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Dione`.`genre_movie` (
  `idMovie` INT NOT NULL,
  `idGenre` INT NOT NULL,
  PRIMARY KEY (`idMovie`, `idGenre`),
  INDEX `idGenre_idx` (`idGenre` ASC),
  INDEX `idMovie_idx` (`idMovie` ASC),
  CONSTRAINT `idGenre`
    FOREIGN KEY (`idGenre`)
    REFERENCES `Dione`.`genre` (`idGenre`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `idMovie1`
    FOREIGN KEY (`idMovie`)
    REFERENCES `Dione`.`movie` (`idMovie`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `Dione`.`movie_tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Dione`.`movie_tag` (
  `idMovie` INT NOT NULL,
  `idTag` INT NOT NULL,
  PRIMARY KEY (`idmovie`, `idtag`),
  INDEX `fk_tag_idx` (`idtag` ASC),
  CONSTRAINT `fk_movie`
    FOREIGN KEY (`idMovie`)
    REFERENCES `Dione`.`movie` (`idMovie`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tag`
    FOREIGN KEY (`idTag`)
    REFERENCES `Dione`.`tag` (`idTag`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `Dione`.`user_prefence`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Dione`.`user_prefence` (
  `idUser` INT NOT NULL,
  `idTag` INT NOT NULL,
  `tag_user_rate` INT NOT NULL,
  PRIMARY KEY (`idUser`, `idTag`),
  INDEX `tag_id_fk_user_prefences_idx` (`idTag` ASC),
  CONSTRAINT `tag_id_fk_user_prefences`
    FOREIGN KEY (`idTag`)
    REFERENCES `Dione`.`tag` (`idtag`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `user_id_fk_user_prefences`
    FOREIGN KEY (`idUser`)
    REFERENCES `Dione`.`users` (`idUsers`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `Dione`.`user_rank`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Dione`.`user_rank` (
  `idUser` INT NOT NULL,
  `idMovie` INT NOT NULL,
  `rank` INT NULL DEFAULT NULL,
  `rankDate` DATETIME NOT NULL,
  PRIMARY KEY (`idUser`, `idMovie`),
  INDEX `movie_fk_idx` (`idMovie` ASC),
  CONSTRAINT `movie_fk`
    FOREIGN KEY (`idMovie`)
    REFERENCES `Dione`.`movie` (`idMovie`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `Dione`.`user_tag_movie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Dione`.`user_tag_movie` (
  `idUser` INT NOT NULL,
  `idTag` INT NOT NULL,
  `idMovie` INT NOT NULL,
  `rate` INT NOT NULL,
  `reteDate` DATETIME NOT NULL,
  PRIMARY KEY (`idUser`, `idTag`, `idMovie`),
  INDEX `movie_fk_user_tags_movie_rating_idx` (`idMovie` ASC),
  INDEX `tag_fk_user_tags_movie_rating_idx` (`idTag` ASC),
  CONSTRAINT `movie_fk_user_tags_movie_rating`
    FOREIGN KEY (`idMovie`)
    REFERENCES `Dione`.`movie` (`idMovie`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `tag_fk_user_tags_movie_rating`
    FOREIGN KEY (`idTag`)
    REFERENCES `Dione`.`tag` (`idtag`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `user_fk_user_tags_movie_rating`
    FOREIGN KEY (`idUser`)
    REFERENCES `Dione`.`users` (`idUsers`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
