-- ----------------------------------------------------------------------------
-- Model
-------------------------------------------------------------------------------

DROP TABLE Inscripcion;
DROP TABLE Curso;

CREATE TABLE Curso (cursoId BIGINT NOT NULL AUTO_INCREMENT,
                    nombre VARCHAR (255) COLLATE latin1_bin NOT NULL,
                    ciudad VARCHAR(255) COLLATE latin1_bin NOT NULL,
                    maxPlazas SMALLINT NOT NULL,
                    plazasLibres SMALLINT NOT NULL,
                    precio FLOAT NOT NULL,
                    fecha_inicio DATETIME NOT NULL,
                    fecha_creacion DATETIME NOT NULL,
                    CONSTRAINT CursoPK PRIMARY KEY (cursoId),
                    CONSTRAINT precioValido CHECK (precio >= 0)) ENGINE = InnoDB;

    CREATE TABLE Inscripcion (inscripcionId BIGINT NOT NULL AUTO_INCREMENT,
                          cursoId BIGINT NOT NULL,
                          email VARCHAR (255) COLLATE latin1_bin NOT NULL,
                          iban VARCHAR (255) COLLATE latin1_bin NOT NULL,
                          fecha_inscripcion DATETIME NOT NULL,
                          fecha_cancelacion DATETIME,
                          CONSTRAINT InscripcionPK PRIMARY KEY (inscripcionId),
                          CONSTRAINT InscripcionCursoIdFK FOREIGN KEY(cursoId)
                              REFERENCES Curso(cursoId) ON DELETE CASCADE) ENGINE = InnoDB;