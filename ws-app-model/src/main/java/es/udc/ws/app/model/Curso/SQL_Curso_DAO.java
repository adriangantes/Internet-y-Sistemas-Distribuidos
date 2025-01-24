package es.udc.ws.app.model.Curso;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException; //importar la excepcion del codigo del ejemplo

public interface SQL_Curso_DAO {
    public Curso create (Connection connection, Curso curso);

    public void update (Connection connection, Curso curso) throws InstanceNotFoundException;

    public Curso find (Connection connection, Long cursoId) throws InstanceNotFoundException;

    public List<Curso> findByCity (Connection connection, String Ciudad, LocalDateTime date);

    public void remove (Connection connection, Long cursoId) throws InstanceNotFoundException;
}
