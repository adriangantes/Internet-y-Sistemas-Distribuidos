package es.udc.ws.app.model.Inscripcion;

import java.sql.*;

public class JDBC_SQL_Inscripcion_DAO extends Abstract_SQL_Inscripcion_DAO {

    @Override
    public Inscripcion create(Connection connection, Inscripcion inscripcion) {

        String queryString = "INSERT INTO Inscripcion"
                + " (cursoId, email, iban, fecha_inscripcion, fecha_cancelacion)"
                + "VALUES (?,?,?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            int i = 1;
            preparedStatement.setLong(i++, inscripcion.getCursoId());
            preparedStatement.setString(i++, inscripcion.getEmail());
            preparedStatement.setString(i++, inscripcion.getIban());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(inscripcion.getFecha_inscripcion()));
            preparedStatement.setTimestamp(i++, null);

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            long inscripcionId = resultSet.getLong(1);

            return new Inscripcion(inscripcionId, inscripcion.getCursoId(),
                    inscripcion.getEmail(),inscripcion.getIban(), inscripcion.getFecha_inscripcion());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
