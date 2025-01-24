package es.udc.ws.app.model.Inscripcion;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SQL_Inscripcion_DAO_Factory {

    private final static String CLASS_NAME_PARAMETER = "SQL_Inscripcion_DAO_Factory.className";
    private static SQL_Inscripcion_DAO dao = null;

    private SQL_Inscripcion_DAO_Factory() {}

    @SuppressWarnings("rawtypes")
    private static SQL_Inscripcion_DAO getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SQL_Inscripcion_DAO) daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static SQL_Inscripcion_DAO getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }
}
