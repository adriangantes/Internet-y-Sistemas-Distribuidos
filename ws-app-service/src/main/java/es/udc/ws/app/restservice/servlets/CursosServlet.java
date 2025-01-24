package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.Curso.Curso;
import es.udc.ws.app.model.CursoService.fictrainingServiceFactory;
import es.udc.ws.app.restservice.dto.CursoToRestCursoDtoConversor;
import es.udc.ws.app.restservice.dto.RestCursoDto;
import es.udc.ws.app.restservice.json.JsonToRestCursoDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import jakarta.servlet.Servlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CursosServlet extends RestHttpServletTemplate {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException {
        ServletUtils.checkEmptyPath(req);

        RestCursoDto cursoDto = JsonToRestCursoDtoConversor.toRestCursoDto(req.getInputStream());
        Curso curso = CursoToRestCursoDtoConversor.toCursoAdd(cursoDto);

        curso = fictrainingServiceFactory.getService().addCurso(curso);

        cursoDto = CursoToRestCursoDtoConversor.toRestCursoDto(curso);
        String cursoURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + curso.getCursoId();
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Location", cursoURL);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                JsonToRestCursoDtoConversor.toObjectNode(cursoDto), headers);
    }

    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, InputValidationException, InstanceNotFoundException{
        //verificar si la peticion es a un curso especifico o no para rederigirla
        String path = req.getPathInfo();
        if(path == null || path.equals("/")) {
            //rederigir (recurso collecion.)
            processGetCollection(req,resp);
        }
        //si no es que es una paticion al id (normal)
        Long id = ServletUtils.getIdFromPath(req, "curso") ; //sacar el id que le pasa para el
        Curso curso = fictrainingServiceFactory.getService().findCurso(id);
        RestCursoDto cursoDto = CursoToRestCursoDtoConversor.toRestCursoDto(curso);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK, JsonToRestCursoDtoConversor.toObjectNode(cursoDto), null);
    }
    protected void processGetCollection(HttpServletRequest req, HttpServletResponse resp) throws IOException, InputValidationException{
        //hay que sacar la ciudad ; cursos que empiecen a partir fecha actual
        LocalDateTime fechaActual = LocalDateTime.now().withNano(0); //sacar la fecha actual
        //toca quitar la ciudad
        String city = ServletUtils.getMandatoryParameter(req, "city"); //sacar la ciudad

        //no hace falta try-catch que las unicas excepciones q devuelve son InputValidationExceptio y RunTimeException
        List<RestCursoDto> cursos = CursoToRestCursoDtoConversor.toRestCursosDtos(fictrainingServiceFactory.getService().findCursos(city, fechaActual)); //sacar los cursos y convertirlos a los DTO
        //pasarlo a jso para enviarlo
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK, JsonToRestCursoDtoConversor.toArrayNode(cursos), null); //no devuelve cabeceras
    }
}
