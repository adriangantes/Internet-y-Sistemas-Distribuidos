package es.udc.ws.app.model.Inscripcion;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.util.List;

public interface SQL_Inscripcion_DAO {
    public Inscripcion create(Connection connection, Inscripcion inscripcion);

    public void update(Connection connection, Inscripcion inscripcion) throws InstanceNotFoundException;

    public Inscripcion find(Connection connection, Long inscripcionId) throws InstanceNotFoundException;

    public List<Inscripcion> findByEmail (Connection connection, String email);

    public void remove (Connection connection, Long inscripcionId) throws InstanceNotFoundException;
}
