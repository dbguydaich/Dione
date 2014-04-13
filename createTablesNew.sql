SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `dbmysql05` DEFAULT CHARACTER SET utf8 ;
USE `dbmysql05` ;

-- -----------------------------------------------------
-- Table `dbmysql05`.`actor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`actor` (
  `idActor` INT(11) NOT NULL,
  `actorName` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idActor`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dbmysql05`.`director`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`director` (
  `idDirector` INT(11) NOT NULL,
  `directorName` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idDirector`))
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
-- Table `dbmysql05`.`actormovie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`actormovie` (
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
-- Table `dbmysql05`.`category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`category` (
  `idcategory` INT(11) NOT NULL AUTO_INCREMENT,
  `categorycol` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idcategory`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'general Tag Categories';


-- -----------------------------------------------------
-- Table `dbmysql05`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`users` (
  `idUsers` INT(11) NOT NULL,
  `userName` VARCHAR(45) NOT NULL,
  `userPassword` VARCHAR(45) NOT NULL,
  `hashPassword` INT(11) NOT NULL,
  PRIMARY KEY (`idUsers`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dbmysql05`.`friend-relations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`friend-relations` (
  `user` INT(11) NOT NULL,
  `friend` INT(11) NOT NULL,
  PRIMARY KEY (`user`, `friend`),
  INDEX `friend_fk_idx` (`friend` ASC),
  CONSTRAINT `friend_fk`
    FOREIGN KEY (`friend`)
    REFERENCES `dbmysql05`.`users` (`idUsers`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `user-fk`
    FOREIGN KEY (`user`)
    REFERENCES `dbmysql05`.`users` (`idUsers`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
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
-- Table `dbmysql05`.`genremovie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`genremovie` (
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
-- Table `dbmysql05`.`moviesgrades`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`moviesgrades` (
  `idMovie` INT(11) NOT NULL,
  `grade` DOUBLE NULL DEFAULT NULL,
  `numberOfRankers` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`idMovie`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dbmysql05`.`tags`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`tags` (
  `idtags` INT(11) NOT NULL,
  `tagscol` VARCHAR(45) NOT NULL,
  `idcategory` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`idtags`),
  INDEX `idcategory_idx` (`idcategory` ASC),
  CONSTRAINT `fk_idcategory`
    FOREIGN KEY (`idcategory`)
    REFERENCES `dbmysql05`.`category` (`idcategory`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dbmysql05`.`movietags`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`movietags` (
  `idmovie` INT(11) NOT NULL,
  `idtag` INT(11) NOT NULL,
  PRIMARY KEY (`idmovie`, `idtag`),
  INDEX `fk_tag_idx` (`idtag` ASC),
  CONSTRAINT `fk_movie`
    FOREIGN KEY (`idmovie`)
    REFERENCES `dbmysql05`.`movie` (`idMovie`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tag`
    FOREIGN KEY (`idtag`)
    REFERENCES `dbmysql05`.`tags` (`idtags`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dbmysql05`.`updates`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`updates` (
  `tableName` VARCHAR(45) NULL DEFAULT NULL,
  `columnName` VARCHAR(45) NULL DEFAULT NULL,
  `newVal` INT(11) NULL DEFAULT NULL,
  `firstKey` INT(11) NULL DEFAULT NULL,
  `secondKey` INT(11) NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dbmysql05`.`user_prefences`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`user_prefences` (
  `user_id` INT(11) NOT NULL,
  `tag_id` INT(11) NOT NULL,
  `tag_user_rate` INT(11) NOT NULL,
  PRIMARY KEY (`user_id`, `tag_id`),
  INDEX `tag_id_fk_user_prefences_idx` (`tag_id` ASC),
  CONSTRAINT `tag_id_fk_user_prefences`
    FOREIGN KEY (`tag_id`)
    REFERENCES `dbmysql05`.`tags` (`idtags`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `user_id_fk_user_prefences`
    FOREIGN KEY (`user_id`)
    REFERENCES `dbmysql05`.`users` (`idUsers`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dbmysql05`.`user_ranks`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`user_ranks` (
  `user_id` INT(11) NOT NULL,
  ` movie_id` INT(11) NOT NULL,
  `rank` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`, ` movie_id`),
  INDEX `movie_fk_idx` (` movie_id` ASC),
  CONSTRAINT `movie_fk`
    FOREIGN KEY (` movie_id`)
    REFERENCES `dbmysql05`.`movie` (`idMovie`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `user_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `dbmysql05`.`users` (`idUsers`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dbmysql05`.`user_tags_movie_rating`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`user_tags_movie_rating` (
  `user_id` INT(11) NOT NULL,
  `tag_id` INT(11) NOT NULL,
  `movie_id` INT(11) NOT NULL,
  `rate` INT(11) NOT NULL,
  PRIMARY KEY (`user_id`, `tag_id`, `movie_id`),
  INDEX `movie_fk_user_tags_movie_rating_idx` (`movie_id` ASC),
  INDEX `tag_fk_user_tags_movie_rating_idx` (`tag_id` ASC),
  CONSTRAINT `movie_fk_user_tags_movie_rating`
    FOREIGN KEY (`movie_id`)
    REFERENCES `dbmysql05`.`movie` (`idMovie`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `tag_fk_user_tags_movie_rating`
    FOREIGN KEY (`tag_id`)
    REFERENCES `dbmysql05`.`tags` (`idtags`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `user_fk_user_tags_movie_rating`
    FOREIGN KEY (`user_id`)
    REFERENCES `dbmysql05`.`users` (`idUsers`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dbmysql05`.`usersmovies`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbmysql05`.`usersmovies` (
  `idUser` INT(11) NOT NULL,
  `idMovie` INT(11) NOT NULL,
  `rank` INT(11) NOT NULL,
  PRIMARY KEY (`idUser`, `idMovie`),
  INDEX `idMovie_idx` (`idMovie` ASC),
  INDEX `idUser_idx` (`idUser` ASC),
  CONSTRAINT `idUser`
    FOREIGN KEY (`idUser`)
    REFERENCES `dbmysql05`.`users` (`idUsers`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
