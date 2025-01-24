package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.model.util.Exceptions.*;

public class AppExceptionToJsonConversor {

    public static ObjectNode toFullCursoException(FullCursoException e) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "fullCurso");
        exceptionObject.put("cursoId", (e.getCursoId() != null) ? e.getCursoId() : null);
        exceptionObject.put("plazasLibres", e.getPlazasLibres());

        return exceptionObject;
    }

    public static ObjectNode toCursoAlreadyStartException(CursoAlreadyStartException e) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "cursoAlreadyStart");
        exceptionObject.put("cursoId", (e.getCursoId() != null) ? e.getCursoId() : null);
        exceptionObject.put("fechaInicio", e.getFechaInicio().toString());

        return exceptionObject;
    }

    public static ObjectNode toInscripcionTooLateToCancel(TooLateCancelException e){
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "inscripcionTooLateToCancel");
        exceptionObject.put("inscripcionId", (e.getInscripcionId() != null) ? e.getInscripcionId() : null);
        exceptionObject.put("cursoId", (e.getCursoId() != null) ? e.getCursoId() : null);
        exceptionObject.put("fechaInicio", e.getFechaInicio().toString());

        return exceptionObject;
    }

    public static ObjectNode toInscripcionAlreadyInscripcionCanceledException(AlreadyInscripcionCanceledException e) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "AlreadyInscripcionCanceled");
        exceptionObject.put("inscripcionId", (e.getInscripcionId() != null) ? e.getInscripcionId() : null);
        exceptionObject.put("cursoId", (e.getCursoId() != null) ? e.getCursoId() : null);
        exceptionObject.put("fechaCancelacion", e.getFechaCancelacion().toString());

        return exceptionObject;
    }

    public static ObjectNode toInscripcionInscripcionOtherUserException(InscripcionOtherUserException e) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "inscripcionInscripcionOtherUser");
        exceptionObject.put("inscripcionId", (e.getInscripcionId() != null) ? e.getInscripcionId() : null);
        exceptionObject.put("cursoId", (e.getCursoId() != null) ? e.getCursoId() : null);

        return exceptionObject;
    }
}
