package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestInscripcionDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.util.List;

public class JsonToRestInscripcionDtoConversor {

    public static ObjectNode toObjectNode (RestInscripcionDto inscripcion){
        ObjectNode inscripcionObj = JsonNodeFactory.instance.objectNode();

        inscripcionObj.put("inscripcionId", inscripcion.getInscripcionId())
                .put("cursoId", inscripcion.getCursoId())
                .put("email", inscripcion.getEmail())
                .put("iban", inscripcion.getIban())
                .put("fecha_inscripcion", inscripcion.getFecha_inscripcion())
                .put("fecha_cancelacion", inscripcion.getFecha_cancelacion());

        return inscripcionObj;
    }

    public static ArrayNode toArrayNode (List<RestInscripcionDto> inscripciones){

        ArrayNode inscripcionesNode = JsonNodeFactory.instance.arrayNode();
        for (RestInscripcionDto inscripcionDto : inscripciones) {
            ObjectNode inscripcionObj = toObjectNode(inscripcionDto);
            inscripcionesNode.add(inscripcionObj);
        }

        return inscripcionesNode;
    }

    public static RestInscripcionDto toRestInscripcionDto (InputStream jsonInscripcion) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonInscripcion);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode inscripcionObject = (ObjectNode) rootNode;

                JsonNode inscripcionIdNode = inscripcionObject.get("inscripcionId");
                JsonNode cursoIdNode = inscripcionObject.get("cursoId");
                Long inscripcionId = (inscripcionIdNode != null) ? inscripcionIdNode.longValue() : null;
                Long cursoId = (cursoIdNode != null) ? cursoIdNode.longValue() : null;

                String email = inscripcionObject.get("email").textValue().trim();
                String iban = inscripcionObject.get("iban").textValue().trim();
                String fecha_inscripcion = inscripcionObject.get("fecha_inscripcion").textValue();
                String fecha_cancelacion = inscripcionObject.get("fecha_cancelacion").textValue();

                return new RestInscripcionDto(inscripcionId, cursoId, email, iban, fecha_inscripcion, fecha_cancelacion);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}
