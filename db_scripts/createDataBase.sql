SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `dbmysql05` DEFAULT CHARACTER SET utf8 ;
USE `dbmysql05` ;

-- -----------------------------------------------------
-- Table `dbmysql05`.`person`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`person` (
  `idPerson` INT(11) NOT NULL,
  `personName` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idPerson`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `dbmysql05`.`actor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`actor` (
  `idActor` INT(11) NOT NULL,
  `idPerson` INT(11) NOT NULL,
  PRIMARY KEY (`idActor`),
  CONSTRAINT `idPersonActor`
    FOREIGN KEY (`idPerson`)
    REFERENCES `dbmysql05`.`person` (`idPerson`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `dbmysql05`.`director`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`director` (
  `idDirector` INT(11) NOT NULL,
  `idPerson` INT(11) NOT NULL,
  PRIMARY KEY (`idDirector`),
  CONSTRAINT `idPersonDirector`
    FOREIGN KEY (`idPerson`)
    REFERENCES `dbmysql05`.`person` (`idPerson`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `dbmysql05`.`language`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`language` (
  `idLanguage` INT(11) NOT NULL,
  `LanguageName` TEXT NOT NULL,
  PRIMARY KEY (`idLanguage`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `dbmysql05`.`movie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`movie` (
  `idMovie` INT(11) NOT NULL,
  `idLanguage` INT(11) NULL DEFAULT NULL,
  `idDirector` INT(11) NULL DEFAULT NULL,
  `movieName` TEXT NOT NULL,
  `year` VARCHAR(45) NULL DEFAULT NULL,
  `wiki` TEXT NULL DEFAULT NULL,
  `duration` VARCHAR(45) NULL DEFAULT NULL,
  `plot` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`idMovie`),
  INDEX `idLanguage_idx` (`idLanguage` ASC),
  INDEX `idDirector_idx` (`idDirector` ASC),
  CONSTRAINT `idDirector`
    FOREIGN KEY (`idDirector`)
    REFERENCES `dbmysql05`.`director` (`idDirector`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `idLanguage`
    FOREIGN KEY (`idLanguage`)
    REFERENCES `dbmysql05`.`language` (`idLanguage`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `dbmysql05`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`users` (
  `idUsers` INT NOT NULL AUTO_INCREMENT,
  `userName` VARCHAR(45) NOT NULL,
  `hashPassword` INT(11) NOT NULL,
  PRIMARY KEY (`idUsers`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `dbmysql05`.`genre`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`genre` (
  `idGenre` INT(11) NOT NULL,
  `genreName` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idGenre`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `dbmysql05`.`tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`tag` (
  `idTag` INT(11) NOT NULL,
  `tagName` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idtag`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `dbmysql05`.`actor_movie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`actor_movie` (
  `idMovie` INT(11) NOT NULL,
  `idActor` INT(11) NOT NULL,
  INDEX `idMovie_idx` (`idMovie` ASC),
  INDEX `idActor_idx` (`idActor` ASC),
  CONSTRAINT `idActor`
    FOREIGN KEY (`idActor`)
    REFERENCES `dbmysql05`.`actor` (`idActor`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `idMovie`
    FOREIGN KEY (`idMovie`)
    REFERENCES `dbmysql05`.`movie` (`idMovie`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dbmysql05`.`friend_relation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`friend_relation` (
  `friend1` INT(11) NOT NULL,
  `friend2` INT(11) NOT NULL,
  PRIMARY KEY (`friend1`, `friend2`),
  CONSTRAINT `friend1_fk`
    FOREIGN KEY (`friend1`)
    REFERENCES `dbmysql05`.`users` (`idUsers`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `friend2_fk`
    FOREIGN KEY (`friend2`)
    REFERENCES `dbmysql05`.`users` (`idUsers`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `dbmysql05`.`genre_movie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`genre_movie` (
  `idMovie` INT(11) NOT NULL,
  `idGenre` INT(11) NOT NULL,
  INDEX `idGenre_idx` (`idGenre` ASC),
  INDEX `idMovie_idx` (`idMovie` ASC),
  CONSTRAINT `idGenre`
    FOREIGN KEY (`idGenre`)
    REFERENCES `dbmysql05`.`genre` (`idGenre`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `idMovie1`
    FOREIGN KEY (`idMovie`)
    REFERENCES `dbmysql05`.`movie` (`idMovie`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `dbmysql05`.`movie_tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`movie_tag` (
  `idMovie` INT(11) NOT NULL,
  `idTag` INT(11) NOT NULL,
  PRIMARY KEY (`idmovie`, `idtag`),
  INDEX `fk_tag_idx` (`idtag` ASC),
  CONSTRAINT `fk_movie`
    FOREIGN KEY (`idMovie`)
    REFERENCES `dbmysql05`.`movie` (`idMovie`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tag`
    FOREIGN KEY (`idTag`)
    REFERENCES `dbmysql05`.`tag` (`idTag`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `dbmysql05`.`user_prefence`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`user_prefence` (
  `idUser` INT(11) NOT NULL,
  `idTag` INT(11) NOT NULL,
  `tag_user_rate` INT(11) NOT NULL,
  PRIMARY KEY (`idUser`, `idTag`),
  INDEX `tag_id_fk_user_prefences_idx` (`idTag` ASC),
  CONSTRAINT `tag_id_fk_user_prefences`
    FOREIGN KEY (`idTag`)
    REFERENCES `dbmysql05`.`tag` (`idtag`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `user_id_fk_user_prefences`
    FOREIGN KEY (`idUser`)
    REFERENCES `dbmysql05`.`users` (`idUsers`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `dbmysql05`.`user_rank`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`user_rank` (
  `idUser` INT(11) NOT NULL,
  `idMovie` INT(11) NOT NULL,
  `rank` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`idUser`, `idMovie`),
  INDEX `movie_fk_idx` (`idMovie` ASC),
  CONSTRAINT `movie_fk`
    FOREIGN KEY (`idMovie`)
    REFERENCES `dbmysql05`.`movie` (`idMovie`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `user_fk`
    FOREIGN KEY (`idUser`)
    REFERENCES `dbmysql05`.`users` (`idUsers`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `dbmysql05`.`user_tag_movie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`user_tag_movie` (
  `idUser` INT(11) NOT NULL,
  `idTag` INT(11) NOT NULL,
  `idMovie` INT(11) NOT NULL,
  `rate` INT(11) NOT NULL,
  PRIMARY KEY (`idUser`, `idTag`, `idMovie`),
  INDEX `movie_fk_user_tags_movie_rating_idx` (`idMovie` ASC),
  INDEX `tag_fk_user_tags_movie_rating_idx` (`idTag` ASC),
  CONSTRAINT `movie_fk_user_tags_movie_rating`
    FOREIGN KEY (`idMovie`)
    REFERENCES `dbmysql05`.`movie` (`idMovie`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `tag_fk_user_tags_movie_rating`
    FOREIGN KEY (`idTag`)
    REFERENCES `dbmysql05`.`tag` (`idtag`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `user_fk_user_tags_movie_rating`
    FOREIGN KEY (`idUser`)
    REFERENCES `dbmysql05`.`users` (`idUsers`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;