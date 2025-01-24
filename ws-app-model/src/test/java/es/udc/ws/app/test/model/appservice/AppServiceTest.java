package es.udc.ws.app.test.model.appservice;

import es.udc.ws.app.model.Curso.Curso;
import es.udc.ws.app.model.Curso.SQL_Curso_DAO_Factory;
import es.udc.ws.app.model.Curso.SQL_Curso_DAO;
import es.udc.ws.app.model.util.Exceptions.*;
import es.udc.ws.app.model.CursoService.fictrainingService;
import es.udc.ws.app.model.CursoService.fictrainingServiceFactory;
import es.udc.ws.app.model.Inscripcion.Inscripcion;
import es.udc.ws.app.model.Inscripcion.SQL_Inscripcion_DAO;
import es.udc.ws.app.model.Inscripcion.SQL_Inscripcion_DAO_Factory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static es.udc.ws.app.model.util.ModelConstants.*;
import static org.junit.jupiter.api.Assertions.*;

public class AppServiceTest {

    private static fictrainingService service = null;
    private static DataSource dataSource;
    private static SQL_Curso_DAO cursoDao = null;
    private static SQL_Inscripcion_DAO inscripcionDao = null;
    private final long NON_EXISTENT_CURSO_ID = -1;

    @BeforeAll
    public static void init() {
        dataSource = new SimpleDataSource();

        DataSourceLocator.addDataSource(APP_DATA_SOURCE, dataSource);

        service = fictrainingServiceFactory.getService();
        cursoDao = SQL_Curso_DAO_Factory.getDao();
        inscripcionDao = SQL_Inscripcion_DAO_Factory.getDao();
    }

    private Curso cursoValido() {
        return new Curso("curso","coruña", 20, 20, LocalDateTime.now().plusMonths(1));
    } //mas q poner una fecha arbitraria q cumpla ahora; mejor coger y establecer la fecha actual +20 dias para que siempre cumpla

    private Curso cursoValidoToFindByCity() {
        return new Curso("curso","MMM", 20, 20, LocalDateTime.now().plusMonths(1).withNano(0));
    }


    private Curso cursoValido2ToFindByCity() {
        return new Curso("curso2", "JJJ", 20, 30, LocalDateTime.now().plusMonths(3).withNano(0));
    }

    private Curso cursoValido3ToFindByCity() {
        return new Curso("curso2", "JJJ", 20, 30, LocalDateTime.now().plusMonths(1).withNano(0));
    }

    private Inscripcion inscripcionValidaSearchEmail(Long cursoId, LocalDateTime date) {
        return new Inscripcion(cursoId, "test@udc.es", "1234567891234567", date);
    }

    private List<Curso> cursosProbarOrdenaFecha(int i, List<Curso> referencia){
        //i = 0 lista desornadena; i = 1 lista Ordenada
        List<Curso> lista = new ArrayList<Curso>();
        if(i == 0) {
            lista.add(new Curso("cursoOrdenado1", "LLL", 20, 30, LocalDateTime.now().plusMonths(5).withNano(0)));
            lista.add(new Curso("cursoOrdenado2", "LLL", 20, 30, LocalDateTime.now().plusMonths(4).withNano(0)));
            lista.add(new Curso("cursoOrdenado3", "LLL", 20, 30, LocalDateTime.now().plusMonths(6).withNano(0)));
            lista.add(new Curso("cursoOrdenado4", "LLL", 20, 30, LocalDateTime.now().plusMonths(2).withNano(0)));
            lista.add(new Curso("cursoOrdenado5", "LLL", 20, 30, LocalDateTime.now().plusMonths(3).withNano(0)));
        }
        if(i == 1){
            lista.add(new Curso(referencia.get(3).getCursoId(), "cursoOrdenado4", "LLL", 20, 30, referencia.get(3).getFecha_inicio()));
            lista.add(new Curso(referencia.get(4).getCursoId(), "cursoOrdenado5", "LLL", 20, 30, referencia.get(4).getFecha_inicio()));
            lista.add(new Curso(referencia.get(1).getCursoId(), "cursoOrdenado2", "LLL", 20, 30, referencia.get(1).getFecha_inicio()));
            lista.add(new Curso(referencia.get(0).getCursoId(), "cursoOrdenado1", "LLL", 20, 30, referencia.get(0).getFecha_inicio()));
            lista.add(new Curso(referencia.get(2).getCursoId(), "cursoOrdenado3", "LLL", 20, 30, referencia.get(2).getFecha_inicio()));


        }
        return lista;
    }

    private Curso cursoCompleto() {
        return new Curso("cursoCompleto","coruña", 1, 20, LocalDateTime.now().plusMonths(1));
    }

    private Curso cursoComenzado() {
        return new Curso("cursoComenzado","coruña", 1, 20, LocalDateTime.now().minusMonths(1));
    }

    private void removeCurso(Long cursoId){
        try(Connection connection = dataSource.getConnection()){
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);
                cursoDao.remove(connection, cursoId);
                connection.commit();
            }catch (InstanceNotFoundException e){
                connection.commit();
                throw new RuntimeException(e);
            }catch (SQLException e){
                connection.rollback();
                throw new RuntimeException(e);
            }catch (RuntimeException | Error e){
                connection.rollback();
                throw e;
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    private void removeInscripcion(Long inscripcionId){
        try(Connection connection = dataSource.getConnection()){
            try{
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);
                inscripcionDao.remove(connection, inscripcionId);
                connection.commit();
            }catch (InstanceNotFoundException e){
                connection.commit();
                throw new RuntimeException(e);
            }catch (SQLException e){
                connection.rollback();
                throw new RuntimeException(e);
            }catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    private List<Inscripcion> inscripcionListSorter(List<Inscripcion> inscripciones){
        return inscripciones.stream()
                .sorted((i1, i2) -> i2.getFecha_inscripcion().compareTo(i1.getFecha_inscripcion()))
                .collect(Collectors.toList());
    }

    private Curso anadirCursoDao(Curso curso){

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

    private Inscripcion anadirInscripcionDao(Inscripcion inscripcion) throws InstanceNotFoundException{
        try (Connection connection = dataSource.getConnection()) {

            try {

                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Curso curso = cursoDao.find(connection, inscripcion.getCursoId());

                if (curso.getPlazasLibres() == 0){
                    throw new FullCursoException(curso.getCursoId(),curso.getPlazasLibres());
                }else if (curso.getFecha_inicio().isBefore(LocalDateTime.now())){
                    throw new CursoAlreadyStartException(curso.getCursoId(),curso.getFecha_inicio());
                }
                Inscripcion inscripcionLocal = inscripcionDao.create(connection, new Inscripcion(inscripcion.getCursoId(), inscripcion.getEmail(), inscripcion.getIban(), inscripcion.getFecha_inscripcion()));

                curso.setPlazasLibres(curso.getPlazasLibres() - 1);
                cursoDao.update(connection, curso);

                connection.commit();

                return inscripcionLocal;

            } catch (InstanceNotFoundException | FullCursoException | CursoAlreadyStartException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException | FullCursoException | CursoAlreadyStartException e) {
            throw new RuntimeException(e);
        }
    }

    public Inscripcion findInscripcionDao(Long inscripcionId) throws InstanceNotFoundException{
        //es para las pruebas no vamos a validar nada, son casos de prueba que se sabe que estan bien
        //el nivel de transacion y el autoccomit lo dejamos por defecto pues solo vamos a leer un dato; y SOLO VAMOS A LEER UN DATO; NO VARIAOS A LA VEZ
        try (Connection connection = dataSource.getConnection()) { //obtener el dataSource, la conexion con la BD
            return inscripcionDao.find(connection, inscripcionId); //de encontrar un curso se encarga el dao
        } catch (SQLException e) {
            throw new RuntimeException(e); //si hay alguna excepcion por el sql
        }
    }

    private Inscripcion inscribirseDao(Long cursoId, String email, String iban) throws InstanceNotFoundException {
        try (Connection connection = dataSource.getConnection()) {

            try {

                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Curso curso = cursoDao.find(connection, cursoId);

                Inscripcion inscripcion = inscripcionDao.create(connection, new Inscripcion(cursoId, email, iban, LocalDateTime.now()));

                curso.setPlazasLibres(curso.getPlazasLibres() - 1);
                cursoDao.update(connection, curso);

                connection.commit();

                return inscripcion;

            } catch (InstanceNotFoundException e) {
                connection.commit();
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



    @Test
    public void testCrearCursoFindCurso() throws InputValidationException, InstanceNotFoundException{

        Curso curso = cursoValido();
        Curso addCurso = null;

        try{
            LocalDateTime beforeCreationDate = LocalDateTime.now().withNano(0);

            addCurso = service.addCurso(curso);

            LocalDateTime afterCreationDate = LocalDateTime.now().withNano(0);

            Curso foundcurso = service.findCurso(addCurso.getCursoId());

            //comprobar que lo busque bien y que añada bien

            assertEquals(addCurso, foundcurso);
            assertEquals(addCurso.getNombre(),foundcurso.getNombre());
            assertEquals(addCurso.getCiudad(), foundcurso.getCiudad());
            assertEquals(addCurso.getMaxPlazas(), foundcurso.getMaxPlazas());
            assertEquals(addCurso.getPlazasLibres(), foundcurso.getPlazasLibres());
            assertEquals(addCurso.getPrecio(), foundcurso.getPrecio());
            assertTrue((!addCurso.getFecha_creacion().isBefore(beforeCreationDate))
                    && (!addCurso.getFecha_creacion().isAfter(afterCreationDate)));


        } finally {
            //eliminar el curso para dejar la BD igual q antes
            if(addCurso != null){
                removeCurso(addCurso.getCursoId());
            }
        }
    }

    @Test
    public void testFindCursoByCity() throws InputValidationException{
        List<Curso> listaCursosReferencia = new ArrayList<>();
        List<Curso> listaCursosSinOrdenar = new ArrayList<>();
        List<Curso> listaCursosOrdenada = new ArrayList<>();
        List<Curso> auxiliar = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            Curso curso = cursoValidoToFindByCity();
            listaCursosReferencia.add(service.addCurso(curso));
        }

        auxiliar = cursosProbarOrdenaFecha(0, null);
        for(Curso c : auxiliar){
            listaCursosSinOrdenar.add(service.addCurso(c));
        }

        listaCursosOrdenada = cursosProbarOrdenaFecha(1, listaCursosSinOrdenar);

        //toca probar que cursos salgan ordenadors
        List<Curso> listaCursosDevueltosOrdenados = new ArrayList<Curso>();
        listaCursosDevueltosOrdenados = service.findCursos("LLL", LocalDateTime.now().minusMonths(1));

        for (int i = 0; i < listaCursosDevueltosOrdenados.size(); i++) {
            Curso c = listaCursosDevueltosOrdenados.get(i);
            Curso c2 = listaCursosOrdenada.get(i);
            if(c != null) {
                removeCurso(c.getCursoId());
            }
            assertEquals(c, c2);
        }

        Curso curso2 = service.addCurso(cursoValidoToFindByCity());
        Curso curso3 = service.addCurso(cursoValido2ToFindByCity());
        Curso curso4 = service.addCurso(cursoValido3ToFindByCity());


        List<Curso> listaCursosReturned = new ArrayList<Curso>();
        List<Curso> listaCursosReturned2 = new ArrayList<Curso>();
        listaCursosReturned = service.findCursos("MMM", LocalDateTime.now());
        listaCursosReturned2 = service.findCursos("JJJ", LocalDateTime.now().plusMonths(2));

        //solo deberia haber 1 curso
        assertEquals(listaCursosReturned2.size(), 1);
        //deberia salir solo un Curso JJJ exactamente el que tiene nombre curso2
        assertEquals(listaCursosReturned2.getFirst(),curso3);
        if(curso2 != null)
            removeCurso(curso2.getCursoId());
        if(curso3 != null)
            removeCurso(curso3.getCursoId());
        if(curso4 != null)
            removeCurso(curso4.getCursoId());

        for(int i = 0; i < 5; i++){
            if(listaCursosReferencia.get(i) != null)
                removeCurso(listaCursosReferencia.get(i).getCursoId());
            assertEquals(listaCursosReferencia.get(i), listaCursosReturned.get(i));
        }
    }

    @Test
    public void testCrearCursoInvalido(){
        //las comprobaciones de addedCurso != null para eliminar serian redundandes ya q debe saltar primero la excepcion pero no estan de mas para asegurar
        //comprobar que campo ciudad no es null
        assertThrows(InputValidationException.class, () -> {
            Curso curso = cursoValido();
            curso.setCiudad(null);
            Curso addedCurso = service.addCurso(curso);
            if(addedCurso != null)
                removeCurso(addedCurso.getCursoId());
        });

        //comprobar que campo ciudad no es vacio
        assertThrows(InputValidationException.class, () -> {
            Curso curso = cursoValido();
            curso.setCiudad("");
            Curso addedCurso = service.addCurso(curso);
            if(addedCurso != null)
                removeCurso(addedCurso.getCursoId());
        });


        //comprobar campo nombre no null
        assertThrows(InputValidationException.class, () -> {
            Curso curso = cursoValido();
            curso.setNombre(null);
            Curso addedCurso = service.addCurso(curso);
            if(addedCurso != null)
                removeCurso(addedCurso.getCursoId());
        });

        //comprobar campo nombre no vacio
        assertThrows(InputValidationException.class, () -> {
            Curso curso = cursoValido();
            curso.setNombre("");
            Curso addedCurso = service.addCurso(curso);
            if(addedCurso != null)
                removeCurso(addedCurso.getCursoId());
        });

        //comprobar maxPlazas es esta entre 1 y MAXPLAZAS
        assertThrows(InputValidationException.class, () -> {
            Curso curso = cursoValido();
            curso.setMaxPlazas(0);
            Curso addedCurso = service.addCurso(curso);
            if(addedCurso != null)
                removeCurso(addedCurso.getCursoId());
        });

        assertThrows(InputValidationException.class, () -> {
            Curso curso = cursoValido();
            curso.setMaxPlazas(MAX_PLAZAS+5);
            Curso addedCurso = service.addCurso(curso);
            if(addedCurso != null)
                removeCurso(addedCurso.getCursoId());
        });


        //comprobar que fecha inicio tiene que ser al menos 15 dias a la fecha actual (de creacion)
        assertThrows(InputValidationException.class, () -> {
            Curso curso = cursoValido();
            curso.setFecha_inicio(LocalDateTime.now().plusDays(2));
            Curso addedCurso = service.addCurso(curso);
            if(addedCurso != null)
                removeCurso(addedCurso.getCursoId());
        });

        //comprobar precio esta entre 0 y MAXPRECIO
        assertThrows(InputValidationException.class, () -> {
            Curso curso = cursoValido();
            curso.setPrecio(-5);
            Curso addedCurso = service.addCurso(curso);
            if(addedCurso != null)
                removeCurso(addedCurso.getCursoId());
        });

        assertThrows(InputValidationException.class, () -> {
            Curso curso = cursoValido();
            curso.setPrecio(MAX_PRECIO+100);
            Curso addedCurso = service.addCurso(curso);
            if(addedCurso != null)
                removeCurso(addedCurso.getCursoId());
        });
    }

    @Test
    public void testFindCursoByIdExceptions() throws InstanceNotFoundException, InputValidationException {
        //probar que salten las excepciones al buscar un cursoq ue no deberia existir

        assertThrows(InputValidationException.class, () -> service.findCurso(NON_EXISTENT_CURSO_ID));
        assertThrows(InputValidationException.class, () -> service.findCurso(null));
        assertThrows(InputValidationException.class, () -> service.findCurso(Long.getLong(String.valueOf(-5))));
    }

    @Test
    public void testFindCursoByIdExceptionInstanceNotFound() throws InstanceNotFoundException{
        assertThrows(InstanceNotFoundException.class, () -> service.findCurso(Long.valueOf(15)));
    }

    @Test
    public void testFindCursoByCityExceptions() throws InputValidationException{
        assertThrows(InputValidationException.class, () -> service.findCursos(null, null));
        assertThrows(InputValidationException.class, () -> service.findCursos("A Coruña", null));
        assertThrows(InputValidationException.class, () -> service.findCursos("", null));
        assertThrows(InputValidationException.class, () -> service.findCursos(null, LocalDateTime.now()));
    }

    @Test
    public void testInscribirseCursoExistente() throws InstanceNotFoundException, InputValidationException {
        Curso curso = cursoValido();

        Inscripcion inscripcion = null;

        try {
            curso = service.addCurso(curso);

            inscripcion = service.inscribirse(curso.getCursoId(), "abc@email.com", "1234567891234567");
            LocalDateTime fechaAntes = LocalDateTime.now().withNano(0);
            Curso cursoInscripcion = service.findCurso(curso.getCursoId());
            LocalDateTime fechaDespues = LocalDateTime.now().withNano(0);
            Inscripcion inscripcionFinal = service.findInscripciones("abc@email.com").getFirst();

            assertNotNull(inscripcion);
            assertEquals(inscripcionFinal.getEmail(), inscripcion.getEmail());
            assertEquals(inscripcionFinal.getIban(), inscripcion.getIban());
            assertEquals(inscripcionFinal.hashCode(), inscripcion.hashCode());
            assertEquals(inscripcionFinal.getCursoId(), inscripcion.getCursoId());
            assertNull(inscripcionFinal.getFecha_cancelacion());
            assertTrue((!fechaAntes.isAfter(inscripcionFinal.getFecha_inscripcion())) && (!fechaDespues.isBefore(inscripcionFinal.getFecha_inscripcion())) );

            assertEquals(19, cursoInscripcion.getPlazasLibres());

        } catch (CursoAlreadyStartException e) {
            throw new RuntimeException(e);
        } catch (FullCursoException e) {
            throw new RuntimeException(e);
        } finally{
            if (inscripcion != null){
                removeInscripcion(inscripcion.getInscripcionId());
            }
            removeCurso(curso.getCursoId());
        }
    }

    @Test
    public void testInscribirseCursoExistenteCompleto() throws InputValidationException, InstanceNotFoundException, CursoAlreadyStartException, FullCursoException {
        Curso curso = cursoCompleto();

        Curso finalCurso = service.addCurso(curso);
        Inscripcion inscripcion = service.inscribirse(finalCurso.getCursoId(), "abc@email.com", "1234567891234567");
        assertThrows(FullCursoException.class, () -> service.inscribirse(finalCurso.getCursoId(), "abc@email.com", "1234567891234567"));

        if(inscripcion != null)
            removeInscripcion(inscripcion.getInscripcionId());
        removeCurso(finalCurso.getCursoId()); //no hace falta ya se saca arriba el cursoId y ya se ve q no es null
    }

    @Test
    public void testInscribirseCursoNoExistente(){
        assertThrows(InstanceNotFoundException.class, () -> service.inscribirse((long)1, "abc@email.com", "1234567891234567"));
    }

    @Test
    public void testInscribirseCursoComenzado() throws InputValidationException {
        Curso curso = cursoComenzado();

        Curso finalCurso = anadirCursoDao(curso);

        assertThrows(CursoAlreadyStartException.class, () -> service.inscribirse(finalCurso.getCursoId(), "abc@email.com", "1234567891234567"));

        removeCurso(finalCurso.getCursoId());
    }

    @Test
    public void testInscribirseCursoDatosInvalidos() throws InputValidationException {
        Curso cursoValido = cursoValido();

        Curso curso = service.addCurso(cursoValido);
        assertThrows(InputValidationException.class, () -> service.inscribirse(null, "email@abc.com", "1234567891234567"));
        assertThrows(InputValidationException.class, () -> service.inscribirse(curso.getCursoId(), "emailsinarroba.com", "1234567891234567"));
        assertThrows(InputValidationException.class, () -> service.inscribirse(curso.getCursoId(), "email@sinpuntocom", "1234567891234567"));
        assertThrows(InputValidationException.class, () -> service.inscribirse(curso.getCursoId(), "email@abc.com", "ibansinnumeros"));
        assertThrows(InputValidationException.class, () -> service.inscribirse(curso.getCursoId(), "email@abc.com", "5010184616184461316816846114416561"));

        removeCurso(curso.getCursoId());
    }

    @Test
    public void testExcepcionFindByEmail() throws InputValidationException {
        assertThrows(InputValidationException.class, () -> service.findInscripciones(null));
        assertThrows(InputValidationException.class, () -> service.findInscripciones(""));
        assertThrows(InputValidationException.class, () -> service.findInscripciones("ab@c@email.c%&%%om"));
    }

    @Test
    public void testFindByEmail()throws InputValidationException, InstanceNotFoundException{
        List<Inscripcion> listaReferencia = new ArrayList<Inscripcion>();
        List<Inscripcion> listaObtenida = new ArrayList<Inscripcion>();

        Curso cursoReferencia = cursoValido();
        Curso cursoObtenido = service.addCurso(cursoReferencia);
        Long idCursoReferencia = cursoObtenido.getCursoId();
        Inscripcion i1 = inscripcionValidaSearchEmail(idCursoReferencia, LocalDateTime.now().minusMonths(1).withNano(0));
        Inscripcion i2 = inscripcionValidaSearchEmail(idCursoReferencia, LocalDateTime.now().minusMonths(5).withNano(0));
        Inscripcion i3 = inscripcionValidaSearchEmail(idCursoReferencia, LocalDateTime.now().minusMonths(7).withNano(0));
        Inscripcion i4 = inscripcionValidaSearchEmail(idCursoReferencia, LocalDateTime.now().minusMonths(4).withNano(0));
        Inscripcion i5 = inscripcionValidaSearchEmail(idCursoReferencia, LocalDateTime.now().minusMonths(2).withNano(0));

        //como pide mas reciente el orden ordendas deberia ser i1 i5 i4 i2 i3

        listaReferencia.add(i1);
        listaReferencia.add(i2);
        listaReferencia.add(i3);
        listaReferencia.add(i4);
        listaReferencia.add(i5);

        for(Inscripcion i : listaReferencia){
            i = anadirInscripcionDao(i);
        }

        listaObtenida = service.findInscripciones("test@udc.es");

        List<Inscripcion> listaOrdenada= new ArrayList<Inscripcion>();
        listaOrdenada = inscripcionListSorter(listaObtenida);

        for (int j = 0; j < listaObtenida.size(); j++){
            assertEquals(listaOrdenada.get(j), listaObtenida.get(j));
        }
        for(Inscripcion i : listaObtenida){
            if(i != null) removeInscripcion(i.getInscripcionId());
        }
        removeCurso(cursoObtenido.getCursoId());
    }


    @Test
    public void testCancelInscripcion() throws InstanceNotFoundException, InputValidationException, CursoAlreadyStartException, FullCursoException, TooLateCancelException, AlreadyInscripcionCanceledException, InscripcionOtherUserException {
        Curso cursoValido = cursoValido(); //crear un curso
        //inscribirse
        Curso curso = service.addCurso(cursoValido);
        Inscripcion inscripcion = service.inscribirse(curso.getCursoId(), "s.vcamba@udc.es","1234567891234567");

        Curso cursoInscrito = service.findCurso(curso.getCursoId());
        assertEquals(cursoInscrito.getPlazasLibres(), cursoValido.getPlazasLibres()-1);

        //cancelo inscripcion
        LocalDateTime fechaAntes = LocalDateTime.now().withNano(0);
        service.cancelInscripcion(inscripcion.getInscripcionId(), "s.vcamba@udc.es"); //cancelar la inscripcion
        LocalDateTime fechaDespues = LocalDateTime.now().withNano(0);
        //ahora a buscar la inscripcion y ver que su fecha sea correcta
        Inscripcion inscripcionReturned = findInscripcionDao(inscripcion.getInscripcionId());
        //comprobar que es la misma inscripcion
        assertEquals(inscripcion.getInscripcionId(), inscripcionReturned.getInscripcionId());
        //comprobar que la fecha de cancelacion no sea null
        assertNotNull(inscripcionReturned.getFecha_cancelacion());
        //comprobar que la fecha de cancelacion es la de hoy
        assertTrue((fechaAntes.compareTo(inscripcionReturned.getFecha_cancelacion()) <=0 ) && (fechaDespues.compareTo(inscripcionReturned.getFecha_cancelacion()) >=0) );

        cursoInscrito = service.findCurso(curso.getCursoId());
        assertEquals(cursoInscrito.getPlazasLibres(), cursoValido.getPlazasLibres());

        //toca borrar curso e inscripcion
        removeInscripcion(inscripcionReturned.getInscripcionId());
        removeCurso(curso.getCursoId());
    }

    @Test
    public void testCancelInscripcionInputValidationException() throws InstanceNotFoundException, InputValidationException, CursoAlreadyStartException, FullCursoException {
        Curso curso = cursoValido(); //curso
        curso = service.addCurso(curso);
        Inscripcion inscripcion = service.inscribirse(curso.getCursoId(), "s.vcamba@udc.es","1234567891234567");
        Inscripcion inscripcionReturned = findInscripcionDao(inscripcion.getInscripcionId());
        assertThrows(InputValidationException.class, () -> service.cancelInscripcion(inscripcionReturned.getInscripcionId(), null));
        assertThrows(InputValidationException.class, () -> service.cancelInscripcion(inscripcionReturned.getInscripcionId(), ""));
        assertThrows(InputValidationException.class, () -> service.cancelInscripcion(null, null));
        assertThrows(InputValidationException.class, () -> service.cancelInscripcion((long) -1, null));

        //eliminar curso e inscripcion
        removeInscripcion(inscripcionReturned.getInscripcionId());
        removeCurso(curso.getCursoId());
    }

    @Test
    public void testCancelInscripcionOtherUser() throws InputValidationException, InstanceNotFoundException, InscripcionOtherUserException, CursoAlreadyStartException, FullCursoException {
        Curso curso = cursoValido();
        curso = service.addCurso(curso);
        Inscripcion inscripcion = service.inscribirse(curso.getCursoId(), "s.vcamba@udc.es","1234567891234567");
        Inscripcion inscripcionReturned = findInscripcionDao(inscripcion.getInscripcionId());

        assertThrows(InscripcionOtherUserException.class, () -> service.cancelInscripcion(inscripcionReturned.getInscripcionId(), "adrian.gantes@udc.es"));

        //eliminar curso e inscripcion
        removeInscripcion(inscripcionReturned.getInscripcionId());
        removeCurso(curso.getCursoId());
    }

    @Test
    public void testCancelInscripcionTooLate() throws InstanceNotFoundException, TooLateCancelException{
        Curso curso = cursoComenzado(); //asi ya emepzo y deberia fallas TooLateToCancel
        curso = anadirCursoDao(curso); //para saltarse las reglas de la fecha del curso
        Inscripcion inscripcion = inscribirseDao(curso.getCursoId(), "s.vcamba@udc.es","1234567891234567");
        Inscripcion inscripcionReturned = findInscripcionDao(inscripcion.getInscripcionId());

        assertThrows(TooLateCancelException.class, () -> service.cancelInscripcion(inscripcionReturned.getInscripcionId(), "adrian.gantes@udc.es"));

        //eliminar curso e inscripcion
        removeInscripcion(inscripcionReturned.getInscripcionId());
        removeCurso(curso.getCursoId());
    }

    @Test
    public void testCancelInscripcionAlreadyCancel() throws InputValidationException, InstanceNotFoundException, TooLateCancelException, AlreadyInscripcionCanceledException, InscripcionOtherUserException, CursoAlreadyStartException, FullCursoException {
        Curso curso = cursoValido(); //crear un curso
        //inscribirse
        curso = service.addCurso(curso);
        Inscripcion inscripcion = service.inscribirse(curso.getCursoId(), "s.vcamba@udc.es","1234567891234567");
        //cancelo inscripcion
        service.cancelInscripcion(inscripcion.getInscripcionId(), "s.vcamba@udc.es"); //cancelar la inscripcion
        //ahora a buscar la inscripcion
        Inscripcion inscripcionReturned = findInscripcionDao(inscripcion.getInscripcionId());
        //comprobar que es la misma inscripcion
        assertEquals(inscripcion.getInscripcionId(), inscripcionReturned.getInscripcionId());

        //comprobar que salta la excepcion si ya la cancele
        assertThrows(AlreadyInscripcionCanceledException.class, () -> service.cancelInscripcion(inscripcionReturned.getInscripcionId(), "s.vcamba@udc.es"));

        //toca borrar curso e inscripcion
        removeInscripcion(inscripcionReturned.getInscripcionId());
        removeCurso(curso.getCursoId());
    }

}
