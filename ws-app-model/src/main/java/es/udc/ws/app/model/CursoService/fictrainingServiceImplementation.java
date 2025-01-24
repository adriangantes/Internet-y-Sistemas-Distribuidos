package es.udc.ws.app.model.CursoService;

import es.udc.ws.app.model.Curso.Curso;
import es.udc.ws.app.model.Curso.SQL_Curso_DAO;
import es.udc.ws.app.model.Curso.SQL_Curso_DAO_Factory;
import es.udc.ws.app.model.util.Exceptions.*;
import es.udc.ws.app.model.Inscripcion.Inscripcion;
import es.udc.ws.app.model.Inscripcion.SQL_Inscripcion_DAO;
import es.udc.ws.app.model.Inscripcion.SQL_Inscripcion_DAO_Factory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;
import java.util.regex.Pattern;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

import static es.udc.ws.app.model.util.ModelConstants.*;

public class fictrainingServiceImplementation implements fictrainingService {

    private final DataSource dataSource;
    private SQL_Curso_DAO cursoDao = null;
    private SQL_Inscripcion_DAO inscripcionDao = null;

    public fictrainingServiceImplementation() {
        dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        cursoDao = SQL_Curso_DAO_Factory.getDao();
        inscripcionDao = SQL_Inscripcion_DAO_Factory.getDao();
    }

    private void validateEmail(String email)throws InputValidationException{
        Pattern EMAIL_PATTERN = Pattern.compile(
                "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE
        );

        if(email == null){
            throw new InputValidationException("email, email can not be a null");
        }
        if(!EMAIL_PATTERN.matcher(email).matches()){
            throw new InputValidationException("email, email must be correct");
        }
    }

    private void validateCurso(Curso curso) throws InputValidationException {

        PropertyValidator.validateMandatoryString("nombre", curso.getNombre());
        PropertyValidator.validateMandatoryString("ciudad", curso.getCiudad());
        PropertyValidator.validateLong("maxPlazas", curso.getMaxPlazas(), 1, MAX_PLAZAS);
        PropertyValidator.validateDouble("Precio", curso.getPrecio(), 0, MAX_PRECIO);

        //comprobar que la fecha de comienzo del curso es al menos 15 dias posterior a la actual
        if(curso.getFecha_inicio() == null){
            throw new InputValidationException("fecha_inicio, fecha_inicio can not be a null");
        }
        LocalDateTime fechaActual = LocalDateTime.now().plusDays(15).withNano(0);
        LocalDateTime fechaComienzo = curso.getFecha_inicio();
        if(fechaComienzo.isEqual(fechaActual) || fechaComienzo.isAfter(fechaActual))
            return;
        throw new InputValidationException("Invalid fechaComienzo, fechaComienzo must be at least 15 days after today");

    }

    private void validateCursoId(Long cursoId) throws InputValidationException{
        //comprueba basicamente que el id no sea nulo, comprueba que sea un long (un numero) y que sea positivo y no negativo
        if(cursoId == null)
            throw new InputValidationException("Invalid cursoId, can not be null");
        PropertyValidator.validateNotNegativeLong("cursoId", cursoId);
    }

    private void validateCityDate(String city, LocalDateTime date) throws InputValidationException {
        if(date == null)
            throw new InputValidationException("Invalid date, date can not be null");
       PropertyValidator.validateMandatoryString("city", city);
    }

    private void validateInscripcion(Long cursoId, String email, String iban) throws InputValidationException {
        // Expresión regular para validar el formato general de un IBAN
        //Pattern IBAN_PATTERN = Pattern.compile("^[A-Z]{2}\\d{2}[A-Z0-9]{10,30}$"); //CODIGO PAIS (ES) 2 digitos de control y entre 12 y 30 caractteres alfanumericos para el numero de cuenta
        // Expresión regular para validar el formato de un email
        Pattern EMAIL_PATTERN = Pattern.compile(
                "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE
        );

        if(cursoId == null)
            throw new InputValidationException("Invalid cursoId, cursoId can not be null");
        PropertyValidator.validateNotNegativeLong("cursoId", cursoId);
        if(iban == null)
            throw new InputValidationException("iban, iban can not be null");
        if(email == null)
            throw new InputValidationException("email, email can not be empty");
        //String ibanComparar = iban.replaceAll("\\s+", ""); //eliminar los espacios en blanco del iban por si los incluye
        // Comprueba si el IBAN coincide con el patrón
         /*if (! IBAN_PATTERN.matcher(iban).matches()){
             throw new InputValidationException("iban, iban format incorrect");
         }*/
        PropertyValidator.validateCreditCard(iban);
         //formato email incorrecto

         if(! EMAIL_PATTERN.matcher(email).matches())
             throw new InputValidationException("email, email format incorrect");

    }

    private void validateCancelInscripcion(Long inscripcionId, String email) throws InputValidationException {
        // Expresión regular para validar el formato de un email
        Pattern EMAIL_PATTERN = Pattern.compile(
                "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE
        );

        if(inscripcionId == null)
            throw new InputValidationException("Invalid cursoId, cursoId can not be null");
        PropertyValidator.validateNotNegativeLong("cursoId", inscripcionId);
        if(email == null)
            throw new InputValidationException("email, email can not be empty");
        //formato email incorrecto
        if(! EMAIL_PATTERN.matcher(email).matches())
            throw new InputValidationException("email, email format incorrect");
    }

    @Override
    public Curso addCurso(Curso curso) throws InputValidationException {

        validateCurso(curso);
        curso.setFecha_creacion(LocalDateTime.now());

        try (Connection connection = dataSource.getConnection()) {

            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Curso cursoCreado = cursoDao.create(connection, curso);

                connection.commit();

                return cursoCreado;

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Curso findCurso(Long cursoId) throws InstanceNotFoundException, InputValidationException{
        //validamos ya antes de pedir la conexion que sea un long correcto para el cursoId
        validateCursoId(cursoId); //metodo que no devuelve nada pero si no es correcto lanza excepcion InputaValidationException
        //el nivel de transacion y el autoccomit lo dejamos por defecto pues solo vamos a leer un dato; y SOLO VAMOS A LEER UN DATO; NO VARIAOS A LA VEZ
        try (Connection connection = dataSource.getConnection()) { //obtener el dataSource, la conexion con la BD
            return cursoDao.find(connection, cursoId); //de encontrar un curso se encarga el dao
        } catch (SQLException e) {
            throw new RuntimeException(e); //si hay alguna excepcion por el sql
        }
    }

    @Override
    public List<Curso> findCursos(String Ciudad, LocalDateTime fecha) throws InputValidationException {
        validateCityDate(Ciudad, fecha);

        try(Connection connection = dataSource.getConnection()){
            return cursoDao.findByCity(connection, Ciudad, fecha);
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Inscripcion inscribirse(Long cursoId, String email, String iban) throws InputValidationException, CursoAlreadyStartException, InstanceNotFoundException, FullCursoException {
        validateInscripcion(cursoId, email, iban);
        try (Connection connection = dataSource.getConnection()) {

            try {

                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Curso curso = cursoDao.find(connection, cursoId);

                if (curso.getPlazasLibres() == 0){
                    throw new FullCursoException(cursoId, curso.getPlazasLibres());
                }else if (curso.getFecha_inicio().isBefore(LocalDateTime.now().withNano(0)) || curso.getFecha_inicio().isEqual(LocalDateTime.now().withNano(0))){
                    throw new CursoAlreadyStartException(cursoId, curso.getFecha_inicio());
                }
                Inscripcion inscripcion = inscripcionDao.create(connection, new Inscripcion(cursoId, email, iban, LocalDateTime.now()));

                curso.setPlazasLibres(curso.getPlazasLibres() - 1);
                cursoDao.update(connection, curso);

                connection.commit();

                return inscripcion;

            } catch (InstanceNotFoundException | FullCursoException | CursoAlreadyStartException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException  | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void cancelInscripcion(Long inscripcionId, String Email) throws InputValidationException,InstanceNotFoundException, TooLateCancelException, AlreadyInscripcionCanceledException, InscripcionOtherUserException {

        validateCancelInscripcion(inscripcionId, Email);

        try (Connection connection = dataSource.getConnection()) {

            try {

                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                //primero sacamos la incripcion
                Inscripcion inscripcion = inscripcionDao.find(connection, inscripcionId); //sacamos la incripcion

                //sacar la fecha de comienzo del curso
                Curso c = cursoDao.find(connection, inscripcion.getCursoId()); //tiene que ir en la misma transacccion tirar del dao

                //comprobar que la fecha de comienzo del curso sea mayor a 7 dias a la actual
                LocalDateTime fecha = c.getFecha_inicio();
                LocalDateTime fechaActual = LocalDateTime.now().withNano(0);

                if (!fecha.isAfter(fechaActual.plusDays(7))){
                    throw new TooLateCancelException(c.getCursoId(), inscripcionId, c.getFecha_inicio());
                }

                //ahora toca mirar si ya se cancelo o no antes
                if(inscripcion.getFecha_cancelacion() != null){
                    //significa que ya se cancelo
                    throw new AlreadyInscripcionCanceledException(c.getCursoId(), inscripcionId, inscripcion.getFecha_cancelacion());
                }

                //toca commprobar que sea el email de la persona para cancelar
                if(!Email.equals(inscripcion.getEmail())){
                    //no se puede cancelar
                    throw new InscripcionOtherUserException(c.getCursoId(), inscripcionId);
                }

                //si no pues toca poner la fecha de cancelacion y hacer un update
                inscripcion.setFecha_cancelacion(fechaActual);

                //actualizar la inscripcion

                inscripcionDao.update(connection, inscripcion);
                c.setPlazasLibres(c.getPlazasLibres()+1);
                cursoDao.update(connection, c);

                connection.commit();

            } catch (InstanceNotFoundException | InscripcionOtherUserException | AlreadyInscripcionCanceledException | TooLateCancelException e) {
                connection.commit(); //como se hizo un find y aun no se encontro pues fuera
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Inscripcion> findInscripciones(String email) throws InputValidationException {
        validateEmail(email);
        try(Connection connection = dataSource.getConnection()){
            return inscripcionDao.findByEmail(connection, email);
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
