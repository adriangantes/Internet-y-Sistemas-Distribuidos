package es.udc.ws.app.model.Inscripcion;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

public abstract class Abstract_SQL_Inscripcion_DAO implements SQL_Inscripcion_DAO {

    protected Abstract_SQL_Inscripcion_DAO() {}

    @Override
    public void update(Connection connection, Inscripcion inscripcion) throws InstanceNotFoundException {

        String queryString = "UPDATE Inscripcion"
                + " SET cursoId = ?, email = ?, iban = ?, fecha_inscripcion = ?, fecha_cancelacion = ?"
                + " WHERE inscripcionId = ? ";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString)) {

            int i = 1;
            preparedStatement.setLong(i++, inscripcion.getCursoId());
            preparedStatement.setString(i++, inscripcion.getEmail());
            preparedStatement.setString(i++, inscripcion.getIban());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(inscripcion.getFecha_inscripcion()));
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(inscripcion.getFecha_cancelacion()));
            preparedStatement.setLong(i++, inscripcion.getInscripcionId());

            int changedRows = preparedStatement.executeUpdate();

            if (changedRows == 0) {
                throw new InstanceNotFoundException(inscripcion.getInscripcionId(),
                        Inscripcion.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Inscripcion find(Connection connection, Long inscripcionId) throws InstanceNotFoundException {
        //encontrar un curso por su ID
        String queryString = "SELECT cursoId, email, iban, fecha_inscripcion, fecha_cancelacion FROM Inscripcion WHERE inscripcionId = ?"; //select ; queremos todos los datos de la inscripcion ; es mejor poner los nombres de las columnas y no un * ya que el dia de mañana si añaden columna nuevsa entre 2 por ej el codigo peta por que lo quiere por orden y de esta forma no ya que devuelve en orden de lo q aparece en el select
        //el id ya lo tenemos , no no hace falta ya quitarlo
        //es una cosnulta parametrizada
        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            int i = 1; //cnt
            preparedStatement.setLong(i++, inscripcionId); //le indicamos el valor de ?; aunque sea un Long y haya q pasarlo al long no hace falta .valueLong ; ya lo hace java solo recordar las clases envoltorio y el box y unboxing (automatico)
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()) {
                //si no hay resultados pues excepcion
                throw new InstanceNotFoundException(inscripcionId, Inscripcion.class.getName());
            }
            //si hay resultados los sacamos
            i = 1;
            //el id no nos hace falta ya quitarlo
            Long cursoId = resultSet.getLong(i++);
            String email = resultSet.getString(i++);
            String iban = resultSet.getString(i++);
            LocalDateTime fechaInscripcion = resultSet.getTimestamp(i++).toLocalDateTime();

            LocalDateTime fechaCancelacion = null;

            if(resultSet.getTimestamp(i) != null) {
                fechaCancelacion = resultSet.getTimestamp(i++).toLocalDateTime();
            }
            //devolvemos el Curso

            return new Inscripcion(inscripcionId, cursoId, email, iban, fechaInscripcion, fechaCancelacion); //crear el objeto inscripcion y devolverlo

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Inscripcion> findByEmail(Connection connection, String email) {

        String queryString = "SELECT inscripcionId, cursoId, email, iban, fecha_inscripcion, fecha_cancelacion " +
                "FROM Inscripcion WHERE email = ? ORDER BY fecha_inscripcion DESC";

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
            List<Inscripcion> inscripcionList = new ArrayList<Inscripcion>();
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                int i = 1;
                Long inscripcionId = resultSet.getLong(i++);
                Long cursoId = resultSet.getLong(i++);
                String emailConsultado = resultSet.getString(i++);
                String iban = resultSet.getString(i++);
                LocalDateTime fechaInscripcion = resultSet.getTimestamp(i++).toLocalDateTime();
                Timestamp fechaCancelacionTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime fechaCancelacion = (fechaCancelacionTimestamp != null)
                        ? fechaCancelacionTimestamp.toLocalDateTime()
                        : null;

                inscripcionList.add(new Inscripcion(inscripcionId, cursoId, emailConsultado, iban, fechaInscripcion, fechaCancelacion));
            }

            return inscripcionList;
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long inscripcionId) throws InstanceNotFoundException {
        String queryString = "DELETE FROM Inscripcion WHERE inscripcionId = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
            int i = 1;
            preparedStatement.setLong(i++, inscripcionId);
            int columnasEliminadas = preparedStatement.executeUpdate();
            if(columnasEliminadas == 0) {
                throw new InstanceNotFoundException(inscripcionId, Inscripcion.class.getName());
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
