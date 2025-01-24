package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientCursoDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientCursoDtoConversor {

    public static ObjectNode toObjectNode(ClientCursoDto curso) throws IOException {

        ObjectNode cursoObject = JsonNodeFactory.instance.objectNode();

        if (curso.getCursoId() != null) {
            cursoObject.put("cursoId", curso.getCursoId());
        }
        cursoObject.put("nombre", curso.getNombre())
                   .put("ciudad", curso.getCiudad())
                   .put("maxPlazas", curso.getMaxPlazas())
                   .put("plazasReservadas", curso.getPlazasReservadas())
                   .put("precio", curso.getPrecio())
                   .put("fecha_inicio", curso.getFecha_inicio().toString());


        return cursoObject;
    }

    public static ClientCursoDto toClientCursoDto (InputStream jsonCurso) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonCurso);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientCursoDto(rootNode);
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientCursoDto> toClientCursoDtos (InputStream jsonCursos) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonCursos);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode cursosArray = (ArrayNode) rootNode;
                List<ClientCursoDto> cursoDtos = new ArrayList<>(cursosArray.size());
                for(JsonNode curso : cursosArray) {
                    cursoDtos.add(toClientCursoDto(curso));
                }
                return cursoDtos;
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientCursoDto toClientCursoDto (JsonNode cursoNode) throws ParsingException {
        if (cursoNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode cursoObject = (ObjectNode) cursoNode;

            JsonNode cursoIdNode = cursoObject.get("cursoId");
            Long cursoId = (cursoIdNode != null) ? cursoIdNode.longValue() : null;

            String nombre = cursoObject.get("nombre").textValue().trim();
            String ciudad = cursoObject.get("ciudad").textValue().trim();
            int maxPlazas = cursoObject.get("maxPlazas").shortValue();
            int plazasLibres = cursoObject.get("plazasLibres").shortValue();
            float precio = cursoObject.get("precio").floatValue();
            LocalDateTime fecha_inicio = LocalDateTime.parse(cursoObject.get("fecha_inicio").textValue().trim());

            return new ClientCursoDto(cursoId, nombre, ciudad, maxPlazas, plazasLibres, precio, fecha_inicio);
        }
    }
}
