package es.udc.ws.app.model.CursoService;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class fictrainingServiceFactory {
    private final static String CLASS_NAME_PARAMETER = "fictrainingServiceFactory.className";
    private static fictrainingService service = null;

    private fictrainingServiceFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static fictrainingService getInstance() {
        try {
            String serviceClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class serviceClass = Class.forName(serviceClassName);
            return (fictrainingService) serviceClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static fictrainingService getService() {

        if (service == null) {
            service = getInstance();
        }
        return service;

    }
}
