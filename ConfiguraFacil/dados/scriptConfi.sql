-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema ConfiguraFacil
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema ConfiguraFacil
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ConfiguraFacil` DEFAULT CHARACTER SET utf8 ;
USE `ConfiguraFacil` ;

-- -----------------------------------------------------
-- Table `ConfiguraFacil`.`Componente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ConfiguraFacil`.`Componente` (
  `nome` VARCHAR(45) NOT NULL,
  `preco` DOUBLE NULL,
  `stock` INT NULL,
  `tipo` VARCHAR(45) NULL,
  PRIMARY KEY (`nome`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ConfiguraFacil`.`ComponentesIncompativeis`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ConfiguraFacil`.`ComponentesIncompativeis` (
  `Componente1` VARCHAR(45) NOT NULL,
  `Componente2` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`Componente1`, `Componente2`),
  INDEX `fk_Componente_has_Componente_Componente1_idx` (`Componente2` ASC) VISIBLE,
  INDEX `fk_Componente_has_Componente_Componente_idx` (`Componente1` ASC) VISIBLE,
  CONSTRAINT `fk_Componente_has_Componente_Componente`
    FOREIGN KEY (`Componente1`)
    REFERENCES `ConfiguraFacil`.`Componente` (`nome`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Componente_has_Componente_Componente1`
    FOREIGN KEY (`Componente2`)
    REFERENCES `ConfiguraFacil`.`Componente` (`nome`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ConfiguraFacil`.`ComponentesNecessarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ConfiguraFacil`.`ComponentesNecessarios` (
  `Componente` VARCHAR(45) NOT NULL,
  `Componente_necessario` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`Componente`, `Componente_necessario`),
  INDEX `fk_Componente_has_Componente_Componente3_idx` (`Componente_necessario` ASC) VISIBLE,
  INDEX `fk_Componente_has_Componente_Componente2_idx` (`Componente` ASC) VISIBLE,
  CONSTRAINT `fk_Componente_has_Componente_Componente2`
    FOREIGN KEY (`Componente`)
    REFERENCES `ConfiguraFacil`.`Componente` (`nome`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Componente_has_Componente_Componente3`
    FOREIGN KEY (`Componente_necessario`)
    REFERENCES `ConfiguraFacil`.`Componente` (`nome`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ConfiguraFacil`.`Pacote`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ConfiguraFacil`.`Pacote` (
  `nome` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`nome`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ConfiguraFacil`.`Pacote_has_Componente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ConfiguraFacil`.`Pacote_has_Componente` (
  `Pacote_nome` VARCHAR(45) NOT NULL,
  `Componente_nome` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`Pacote_nome`, `Componente_nome`),
  INDEX `fk_Pacote_has_Componente_Componente1_idx` (`Componente_nome` ASC) VISIBLE,
  INDEX `fk_Pacote_has_Componente_Pacote1_idx` (`Pacote_nome` ASC) VISIBLE,
  CONSTRAINT `fk_Pacote_has_Componente_Pacote1`
    FOREIGN KEY (`Pacote_nome`)
    REFERENCES `ConfiguraFacil`.`Pacote` (`nome`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Pacote_has_Componente_Componente1`
    FOREIGN KEY (`Componente_nome`)
    REFERENCES `ConfiguraFacil`.`Componente` (`nome`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ConfiguraFacil`.`Modelo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ConfiguraFacil`.`Modelo` (
  `nome` VARCHAR(45) NOT NULL,
  `preco` DOUBLE NULL,
  PRIMARY KEY (`nome`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ConfiguraFacil`.`Configuracao`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ConfiguraFacil`.`Configuracao` (
  `idConfiguracao` INT NOT NULL AUTO_INCREMENT,
  `Modelo_nome` VARCHAR(45) NOT NULL,
  `nif` INT NOT NULL,
  `telefone` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `nome` VARCHAR(45) NOT NULL,
  `morada` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idConfiguracao`),
  INDEX `fk_Configuracao_Modelo1_idx` (`Modelo_nome` ASC) VISIBLE,
  UNIQUE INDEX `idConfiguracao_UNIQUE` (`idConfiguracao` ASC) VISIBLE,
  CONSTRAINT `fk_Configuracao_Modelo1`
    FOREIGN KEY (`Modelo_nome`)
    REFERENCES `ConfiguraFacil`.`Modelo` (`nome`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ConfiguraFacil`.`Configuracao_has_Componente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ConfiguraFacil`.`Configuracao_has_Componente` (
  `Configuracao_idConfiguracao` INT NOT NULL,
  `Componente_nome` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`Configuracao_idConfiguracao`, `Componente_nome`),
  INDEX `fk_Configuracao_has_Componente_Componente1_idx` (`Componente_nome` ASC) VISIBLE,
  INDEX `fk_Configuracao_has_Componente_Configuracao1_idx` (`Configuracao_idConfiguracao` ASC) VISIBLE,
  CONSTRAINT `fk_Configuracao_has_Componente_Configuracao1`
    FOREIGN KEY (`Configuracao_idConfiguracao`)
    REFERENCES `ConfiguraFacil`.`Configuracao` (`idConfiguracao`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Configuracao_has_Componente_Componente1`
    FOREIGN KEY (`Componente_nome`)
    REFERENCES `ConfiguraFacil`.`Componente` (`nome`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ConfiguraFacil`.`Configuracao_has_Pacote`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ConfiguraFacil`.`Configuracao_has_Pacote` (
  `Configuracao_idConfiguracao` INT NOT NULL,
  `Pacote_nome` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`Configuracao_idConfiguracao`, `Pacote_nome`),
  INDEX `fk_Configuracao_has_Pacote_Pacote1_idx` (`Pacote_nome` ASC) VISIBLE,
  INDEX `fk_Configuracao_has_Pacote_Configuracao1_idx` (`Configuracao_idConfiguracao` ASC) VISIBLE,
  CONSTRAINT `fk_Configuracao_has_Pacote_Configuracao1`
    FOREIGN KEY (`Configuracao_idConfiguracao`)
    REFERENCES `ConfiguraFacil`.`Configuracao` (`idConfiguracao`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Configuracao_has_Pacote_Pacote1`
    FOREIGN KEY (`Pacote_nome`)
    REFERENCES `ConfiguraFacil`.`Pacote` (`nome`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ConfiguraFacil`.`Utilizador`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ConfiguraFacil`.`Utilizador` (
  `nome` VARCHAR(45) NOT NULL,
  `pass` VARCHAR(45) NOT NULL,
  `tipo` VARCHAR(45) NULL,
  PRIMARY KEY (`nome`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;





Use configurafacil;



-- Povoamento(script)
Create User 'ola'@'localhost' identified by '123456';

Grant all privileges on configurafacil.* to 'ola'@'localhost';

Insert into utilizador 
	(nome, pass, tipo)
    Values('Joaquim', '123456', 'Vendedor');
    
Insert into Componente
			Values('Pneu1', 20.00, 5, 'Pneu');

Insert into Componente
			Values('Pneu2', 20.00, 5, 'Pneu');

Insert into Componente
			Values('Pneu3', 20.00, 5, 'Pneu');

Insert into Componente
			Values('Pneu4', 20.00, 5, 'Pneu');
            
insert into Componente
			Values('Jantes1', 60.00, 5,'Jantes');
		
insert into Componente
			Values('Jantes2', 70.00, 5,'Jantes');
            
insert into Componente
			Values('Jantes3', 80.00, 5,'Jantes');
            
insert into Componente
			Values('Jantes4',114.00, 5,'Jantes');
		
insert into Componente
			Values('DetalheExterior1',120.00, 5,'DetalheExterior');
            
insert into Componente
			Values('DetalheExterior2',137.00, 5,'DetalheExterior');
	
insert into Componente
			Values('DetalheExterior3',160.00, 5,'DetalheExterior');
            
insert into Componente
			Values('DetalheExterior4',179.60, 5,'DetalheExterior');
            
insert into Componente
			Values('DetalheInterior1',500.00, 5,'DetalheInterior');

insert into Componente
			Values('DetalheInterior2',400.00, 5,'DetalheInterior');
            
insert into Componente
			Values('DetalheInterior3',540.00, 5,'DetalheInterior');
            
insert into Componente
			Values('DetalheInterior4',580.00, 5,'DetalheInterior');
            
insert into Componente
			Values('Pintura-Vermelho',250.00, 5,'Pintura');
            
insert into Componente
			Values('Pintura-Azul',250.00, 5,'Pintura');
	
insert into Componente
			Values('Pintura-Cinza',250.00, 5,'Pintura');
            
insert into Componente
			Values('Pintura-Verde',250.00, 5,'Pintura');
            
insert into Componente
			Values('Pintura-Branco',250.00, 5,'Pintura');
            
insert into Componente
			Values('Motor-Eletrico', 2500.00, 5, 'Motor');
            
insert into Componente
			Values('Motor-Gasolina', 1000.00, 5, 'Motor');
            
insert into Componente
			Values('Motor-Diesel', 1620.00, 5, 'Motor');
            
            
            
insert into componentesincompativeis
			Values('Pneu1','Pneu2');
		
insert into componentesincompativeis
			Values('Pneu1','Pneu3');
            
insert into componentesincompativeis
			Values('Pneu1','Pneu4');
            
insert into componentesincompativeis
			Values('Pneu2','Pneu3');
            
insert into componentesincompativeis
			Values('Pneu2','Pneu4');
            
insert into componentesincompativeis
			Values('Pneu3','Pneu4');
            
insert into componentesincompativeis
			Values('Jantes1','Jantes2');
		
insert into componentesincompativeis
			Values('Jantes1','Jantes3');
            
insert into componentesincompativeis
			Values('Jantes1','Jantes4');
            
insert into componentesincompativeis
			Values('Jantes2','Jantes3');
            
insert into componentesincompativeis
			Values('Jantes2','Jantes4');
            
insert into componentesincompativeis
			Values('Jantes3','Jantes4');
            

            
insert into componentesincompativeis
			Values('Pintura-Vermelho','Pintura-Azul');
		
insert into componentesincompativeis
			Values('Pintura-Vermelho','Pintura-Verde');
            
insert into componentesincompativeis
			Values('Pintura-Vermelho','Pintura-Branco');
            
insert into componentesincompativeis
			Values('Pintura-Vermelho','Pintura-Cinza');
            
insert into componentesincompativeis
			Values('Pintura-Azul','Pintura-Verde');
		
insert into componentesincompativeis
			Values('Pintura-Azul','Pintura-Branco');
            
insert into componentesincompativeis
			Values('Pintura-Azul','Pintura-Cinza');
            
insert into componentesincompativeis
			Values('Pintura-Verde','Pintura-Branco');
            
insert into componentesincompativeis
			Values('Pintura-Verde','Pintura-Cinza');
            
insert into componentesincompativeis
			Values('Pintura-Branco','Pintura-Cinza');
            
insert into componentesincompativeis
			Values('Motor-Eletrico','Motor-Gasolina');
            
insert into componentesincompativeis
			Values('Motor-Eletrico','Motor-Diesel');
            
insert into componentesincompativeis
			Values('Motor-Gasolina','Motor-Diesel');
            
insert into componentesnecessarios
			Values('Motor-Eletrico','Jantes3');
		
insert into componentesnecessarios
			Values('Motor-Gasolina','Jantes1');
            
insert into componentesnecessarios
			Values('Jantes2','Pneu2');

insert into componentesnecessarios
			Values('Jantes3','Pneu3');
            


insert into pacote
			Values('Pacote Desportivo');

insert into pacote
			Values('Pacote Inverno');
            
insert into pacote
			Values('Pacote Conforto');
            
insert into pacote
			Values('Pacote Ambiente');

insert into pacote_has_componente
			Values('Pacote Ambiente','Motor-Eletrico');
            
insert into pacote_has_componente
			Values('Pacote Ambiente','Jantes3');
            
insert into pacote_has_componente
			Values('Pacote Ambiente','Pneu3');
            
insert into pacote_has_componente
			Values('Pacote Desportivo','Motor-Gasolina');
            
insert into pacote_has_componente
			Values('Pacote Desportivo','Jantes1');
            
insert into pacote_has_componente
			Values('Pacote Conforto','Motor-Diesel');
            
insert into pacote_has_componente
			Values('Pacote Conforto','Jantes2');
            
insert into pacote_has_componente
			Values('Pacote Inverno','Motor-Gasolina');
            
insert into pacote_has_componente
			Values('Pacote Inverno','Jantes1');
            
insert into pacote_has_componente
			Values('Pacote Inverno','Pneu1');
            
            
insert into utilizador
	Values('Admin','Admin','Adminstrador');
    
insert into utilizador
	Values('Zeca','123456','Fabricante');
    
    
insert into modelo
	Values('Modelo1', 5000);
insert into modelo
	Values('Modelo2', 10000);
insert into modelo
	Values('Modelo3', 15000);
insert into modelo
	Values('Modelo4', 20000);

