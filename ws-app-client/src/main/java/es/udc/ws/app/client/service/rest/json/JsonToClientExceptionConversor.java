package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;
import es.udc.ws.app.client.service.exceptions.*;

import java.io.InputStream;
import java.time.LocalDateTime;

public class JsonToClientExceptionConversor {

    public static Exception fromBadRequestErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InputValidation")) {
                    return toInputValidationException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InputValidationException toInputValidationException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new InputValidationException(message);
    }

    public static Exception fromNotFoundErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InstanceNotFound")) {
                    return toInstanceNotFoundException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InstanceNotFoundException toInstanceNotFoundException(JsonNode rootNode) {
        String instanceId = rootNode.get("instanceId").textValue();
        String instanceType = rootNode.get("instanceType").textValue();
        return new InstanceNotFoundException(instanceId, instanceType);
    }

    private static ClientTooLateCancelException toClientTooLateCancelException(JsonNode rootNode) {
        Long cursoId = rootNode.get("cursoId").longValue();
        Long inscriptionId = rootNode.get("inscripcionId").longValue();
        String dateString = rootNode.get("fechaInicio").textValue();
        LocalDateTime date = LocalDateTime.parse(dateString);
        return new ClientTooLateCancelException(cursoId, inscriptionId, date);
    }

    private static ClientInscripcionOtherUserException toClientInscripcionOtherUserException(JsonNode rootNode) {
        Long cursoId = rootNode.get("cursoId").longValue();
        Long inscriptionId = rootNode.get("inscripcionId").longValue();

        return new ClientInscripcionOtherUserException(cursoId, inscriptionId);
    }

    private static ClientFullCursoException toClientFullCursoException(JsonNode rootNode) {
        Long cursoId = rootNode.get("cursoId").longValue();
        int plazasLibres = rootNode.get("plazasLibres").intValue();

        return new ClientFullCursoException(cursoId, plazasLibres);
    }

    private static ClientCursoAlreadyStartException toClientCursoAlreadyStartException(JsonNode rootNode) {
        Long cursoId = rootNode.get("cursoId").longValue();
        LocalDateTime fechaInicio = LocalDateTime.parse(rootNode.get("fechaInicio").textValue());

        return new ClientCursoAlreadyStartException(cursoId, fechaInicio);
    }

    private static ClientAlreadyInscripcionCanceledException toClientAlreadyInscripcionCanceledException(JsonNode rootNode) {
        Long cursoId = rootNode.get("cursoId").longValue();
        Long inscriptionId = rootNode.get("inscripcionId").longValue();
        LocalDateTime fechaCancelacion = LocalDateTime.parse(rootNode.get("fechaCancelacion").textValue());

        return new ClientAlreadyInscripcionCanceledException(cursoId, inscriptionId, fechaCancelacion);
    }

    public static Exception fromForbiddenErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("inscripcionTooLateToCancel")) {
                    return toClientTooLateCancelException(rootNode);
                }else if (errorType.equals("inscripcionInscripcionOtherUser")) {
                    return toClientInscripcionOtherUserException(rootNode);
                } else if (errorType.equals("fullCurso")) {
                    return toClientFullCursoException(rootNode);
                }else if (errorType.equals("cursoAlreadyStart")) {
                    return toClientCursoAlreadyStartException(rootNode);
                }else if (errorType.equals("AlreadyInscripcionCanceled")) {
                    return toClientAlreadyInscripcionCanceledException(rootNode);
                }else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }




}
