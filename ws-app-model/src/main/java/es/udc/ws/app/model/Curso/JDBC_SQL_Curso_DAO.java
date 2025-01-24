package es.udc.ws.app.model.Curso;

import java.sql.*;


public class JDBC_SQL_Curso_DAO extends Abstract_SQL_Curso_DAO {

    @Override
    public Curso create(Connection connection, Curso curso) {

        String queryString = "INSERT INTO Curso"
                + " (nombre,ciudad, maxPlazas, plazasLibres,precio,fecha_inicio,fecha_creacion) "
                + " VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            int i = 1;
            preparedStatement.setString(i++, curso.getNombre());
            preparedStatement.setString(i++, curso.getCiudad());
            preparedStatement.setInt(i++, curso.getMaxPlazas());
            preparedStatement.setInt(i++, curso.getMaxPlazas());
            preparedStatement.setFloat(i++, curso.getPrecio());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(curso.getFecha_inicio()));
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(curso.getFecha_creacion()));

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            long cursoId = resultSet.getLong(1);

            return new Curso(cursoId, curso.getNombre(), curso.getCiudad(),
                    curso.getMaxPlazas(), curso.getPrecio(),
                    curso.getFecha_inicio(), curso.getFecha_creacion());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
