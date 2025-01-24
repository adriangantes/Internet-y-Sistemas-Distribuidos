package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestCursoDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.util.List;

public class JsonToRestCursoDtoConversor {
    
    public static ObjectNode toObjectNode (RestCursoDto curso){
        ObjectNode cursoObj = JsonNodeFactory.instance.objectNode();
        
        cursoObj.put("cursoId", curso.getCursoId())
                .put("nombre", curso.getNombre())
                .put("ciudad", curso.getCiudad())
                .put("maxPlazas", curso.getMaxPlazas())
                .put("plazasLibres", curso.getPlazasLibres())
                .put("precio", curso.getPrecio())
                .put("fecha_inicio", curso.getFecha_inicio());
        
        return cursoObj;
    }
    
    public static ArrayNode toArrayNode (List<RestCursoDto> cursos){
        
        ArrayNode cursosNode = JsonNodeFactory.instance.arrayNode();
        for (RestCursoDto cursoDto : cursos) {
            ObjectNode cursoObj = toObjectNode(cursoDto);
            cursosNode.add(cursoObj);
        }
        
        return cursosNode;
    }
    
    public static RestCursoDto toRestCursoDto (InputStream jsonCurso) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonCurso);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode cursoObject = (ObjectNode) rootNode;

                JsonNode cursoIdNode = cursoObject.get("cursoId");
                Long cursoId = (cursoIdNode != null) ? cursoIdNode.longValue() : null;

                String nombre = cursoObject.get("nombre").textValue().trim();
                String ciudad = cursoObject.get("ciudad").textValue().trim();
                int maxPlazas = cursoObject.get("maxPlazas").shortValue();
                JsonNode plazasLibresNode = cursoObject.get("plazasLibres");
                int plazasLibres = (plazasLibresNode != null) ? cursoObject.get("plazasLibres").shortValue() : 0;
                float precio = cursoObject.get("precio").floatValue();
                String fecha_inicio = cursoObject.get("fecha_inicio").textValue();

                return new RestCursoDto(cursoId, nombre, ciudad, maxPlazas, plazasLibres, precio, fecha_inicio);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}
