package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientInscripcionDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientInscripcionDtoConversor {

    public static ObjectNode toObjectNode(ClientInscripcionDto inscripcion) throws IOException {

        ObjectNode inscripcionObject = JsonNodeFactory.instance.objectNode();

        if (inscripcion.getInscripcionId() != null) {
            inscripcionObject.put("inscripcionId", inscripcion.getInscripcionId());
        }

        inscripcionObject.put("cursoId", inscripcion.getCursoId())
                .put("email", inscripcion.getEmail())
                .put("iban", inscripcion.getIban())
                .put("fecha_inscripcion", inscripcion.getFechaInscripcion())
                .put("fecha_cancelacion", inscripcion.getFechaCancelacion());


        return inscripcionObject;
    }


    private static ClientInscripcionDto toClientInscripcionDto (JsonNode inscripcionNode) throws ParsingException {
        if (inscripcionNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode inscripcionObject = (ObjectNode) inscripcionNode;

            JsonNode inscripcionIdNode = inscripcionObject.get("inscripcionId");
            Long inscripcionId = (inscripcionIdNode != null) ? inscripcionIdNode.longValue() : null;

            Long cursoId = inscripcionObject.get("cursoId").longValue();
            String email = inscripcionObject.get("email").textValue().trim();
            String iban = inscripcionObject.get("iban").textValue().trim();
            LocalDateTime fecha_inscripcion = LocalDateTime.parse(inscripcionObject.get("fecha_inscripcion").textValue().trim());
            JsonNode fecha_cancelacionNode = inscripcionObject.get("fecha_cancelacion");
            String fecha_cancelacion = null;
            if(fecha_cancelacionNode.textValue() != null) {
                fecha_cancelacion = LocalDateTime.parse(inscripcionObject.get("fecha_cancelacion").textValue().trim()).toString();
            }
            return new ClientInscripcionDto(inscripcionId, cursoId, email, iban, fecha_inscripcion.toString(), fecha_cancelacion);
        }
    }
    public static ClientInscripcionDto toClientInscripcionDto (InputStream jsonInscripcion) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonInscripcion);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientInscripcionDto(rootNode);
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientInscripcionDto> toClientInscripcionDtos (InputStream jsonInscripciones) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonInscripciones);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode inscripcionesArray = (ArrayNode) rootNode;
                List<ClientInscripcionDto> inscripcionDtos = new ArrayList<>(inscripcionesArray.size());
                for(JsonNode inscripcion : inscripcionesArray) {
                    inscripcionDtos.add(toClientInscripcionDto(inscripcion));
                }
                return inscripcionDtos;
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}
