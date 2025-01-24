package es.udc.ws.app.model.Curso;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A partial implementation of
 * <code>SQLCursoDAO</code> that leaves
 * <code>create(Connection, Curso)</code> as abstract.
 */


public abstract class Abstract_SQL_Curso_DAO implements SQL_Curso_DAO {

    protected Abstract_SQL_Curso_DAO() {}

    @Override
    public void update (Connection connection, Curso curso) throws InstanceNotFoundException {

        String queryString = "UPDATE Curso"
                + " SET nombre = ?, ciudad = ?, maxPlazas = ?, plazasLibres = ?, precio = ?, fecha_inicio = ?, fecha_creacion = ?"
                + " WHERE cursoId = ? ";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString)) {

            int i = 1;
            preparedStatement.setString(i++, curso.getNombre());
            preparedStatement.setString(i++, curso.getCiudad());
            preparedStatement.setInt(i++, curso.getMaxPlazas());
            preparedStatement.setInt(i++, curso.getPlazasLibres());
            preparedStatement.setFloat(i++, curso.getPrecio());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(curso.getFecha_inicio()));
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(curso.getFecha_creacion()));
            preparedStatement.setLong(i++, curso.getCursoId());

            int changedRows = preparedStatement.executeUpdate();

            if (changedRows == 0) {
                throw new InstanceNotFoundException(curso.getCursoId(),
                        Curso.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Curso find (Connection connection, Long cursoId) throws InstanceNotFoundException {
        //encontrar un curso por su ID
        String queryString = "SELECT nombre, ciudad, maxPlazas, plazasLibres, precio, fecha_inicio, fecha_creacion FROM Curso WHERE cursoId = ?"; //select ; queremos todos los datos del curso ; es mejor poner los nombres de las columnas y no un * ya que el dia de mañana si añaden columna nuevsa entre 2 por ej el codigo peta por que lo quiere por orden y de esta forma no ya que devuelve en orden de lo q aparece en el select
        //el id ya lo tenemos , no no hace falta ya quitarlo
        //es una cosnulta parametrizada
        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            int i = 1; //cnt
            preparedStatement.setLong(i++, cursoId); //le indicamos el valor de ?; aunque sea un Long y haya q pasarlo al long no hace falta .valueLong ; ya lo hace java solo recordar las clases envoltorio y el box y unboxing (automatico)
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()) {
                //si no hay resultados pues excepcion
                throw new InstanceNotFoundException(cursoId, Curso.class.getName());
            }
            //si hay resultados los sacamos
            i = 1;
            //el id no nos hace falta ya quitarlo
            String nombre = resultSet.getString(i++);
            String ciudad = resultSet.getString(i++);
            int maxPlazas = resultSet.getInt(i++);
            int plazasLibres = resultSet.getInt(i++);
            float precio = resultSet.getFloat(i++);
            LocalDateTime fechaInicio = resultSet.getTimestamp(i++).toLocalDateTime();
            LocalDateTime fechaCreacion = resultSet.getTimestamp(i++).toLocalDateTime();

            //devolvemos el Curso

            return new Curso(cursoId, nombre, ciudad, maxPlazas, plazasLibres, precio, fechaInicio, fechaCreacion); //crear el objeto curso y devolverlo

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Curso> findByCity (Connection connection, String ciudadBusqueda, LocalDateTime date){
        //encontrar curso por ciudad filtrado por fecha y ordenado por fecha
        String queryString = "SELECT cursoId, nombre, ciudad, maxPlazas, plazasLibres, precio, fecha_inicio, fecha_creacion " +
                "FROM Curso WHERE fecha_inicio > ? AND LOWER(ciudad) LIKE LOWER(?) ORDER BY fecha_inicio"; //por si alguno pone como ciudad CORUÑA otro Coruña etc y al buscar coruña (ya se entiende)

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
            List<Curso> cursosList = new ArrayList<>();
            int i = 1;
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(date));
            preparedStatement.setString(i++, ciudadBusqueda);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                i = 1;
                Long cursoId = resultSet.getLong(i++);
                String nombre = resultSet.getString(i++);
                String ciudad = resultSet.getString(i++);
                int maxPlazas = resultSet.getInt(i++);
                int plazasLibres = resultSet.getInt(i++);
                float precio = resultSet.getFloat(i++);
                LocalDateTime fechaInicio = resultSet.getTimestamp(i++).toLocalDateTime();
                LocalDateTime fechaCreacion = resultSet.getTimestamp(i++).toLocalDateTime();

                cursosList.add(new Curso(cursoId, nombre, ciudad, maxPlazas, plazasLibres, precio, fechaInicio, fechaCreacion));
            }
            return cursosList;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove (Connection connection, Long cursoId) throws InstanceNotFoundException{
        String queryString = "DELETE FROM Curso WHERE cursoId = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
            int i = 1;
            preparedStatement.setLong(i++, cursoId);
            int columnasEliminadas = preparedStatement.executeUpdate();
            if (columnasEliminadas == 0){
                //hubo un error
                throw new InstanceNotFoundException(cursoId, Curso.class.getName());
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
