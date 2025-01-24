package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.CursoService.fictrainingServiceFactory;
import es.udc.ws.app.model.Inscripcion.Inscripcion;
import es.udc.ws.app.model.util.Exceptions.*;
import es.udc.ws.app.restservice.dto.InscripcionToRestInscripcionDtoConversor;
import es.udc.ws.app.restservice.dto.RestInscripcionDto;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.JsonToRestInscripcionDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InscripcionesServlet extends RestHttpServletTemplate {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp)throws IOException,
            InputValidationException, InstanceNotFoundException { //cancelar inscripcion
        String path = req.getPathInfo();

        if(path == null || path.equals("/")){
            processPostInscripcion(req, resp);
        }else{
            //comprobar que lleve el cancel
            if(!path.matches("^/\\d+/cancel$")) { //tiene q coincidir con un /id/cancel donde id es cualquier numero entero ya q si no no va a cancel
                throw new InputValidationException("Invalid Request: invalid URL");
            }
            String idString = path.substring(1,path.indexOf("/cancel"));
            Long inscripcionId;
            if (idString != null && idString.length() != 0) {
                try {
                    inscripcionId = Long.valueOf(idString);
                } catch (NumberFormatException var6) {
                    throw new InputValidationException("Invalid Request: invalid " + " inscripcion id '" + idString + "'");
                }
            } else {
                throw new InputValidationException("Invalid Request: invalid " + " inscripcion id");
            }

            String email = ServletUtils.getMandatoryParameter(req,"email");

            try{
                fictrainingServiceFactory.getService().cancelInscripcion(inscripcionId, email);
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK, null, null);
            }catch (TooLateCancelException e){
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toInscripcionTooLateToCancel(e), null);
            }catch (AlreadyInscripcionCanceledException e){
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toInscripcionAlreadyInscripcionCanceledException(e), null);
            }catch (InscripcionOtherUserException e){
                //implementar excepcion
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toInscripcionInscripcionOtherUserException(e), null);
            }
        }
    }

    protected void processPostInscripcion(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {
        ServletUtils.checkEmptyPath(req);

        Long cursoId = ServletUtils.getMandatoryParameterAsLong(req,"cursoId");
        String email = ServletUtils.getMandatoryParameter(req,"email");
        String iban = ServletUtils.getMandatoryParameter(req,"iban");

        Inscripcion inscripcion;
        try{
            inscripcion = fictrainingServiceFactory.getService().inscribirse(cursoId, email, iban);
        } catch (FullCursoException e){
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                    AppExceptionToJsonConversor.toFullCursoException(e),
                    null);
            return;
        } catch (CursoAlreadyStartException e){
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                    AppExceptionToJsonConversor.toCursoAlreadyStartException(e),
                    null);
            return;
        }

        RestInscripcionDto inscripcionDto = InscripcionToRestInscripcionDtoConversor.toRestInscripcionDto(inscripcion);
        String inscripcionURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + inscripcion.getInscripcionId();
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Location", inscripcionURL);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                JsonToRestInscripcionDtoConversor.toObjectNode(inscripcionDto), headers);
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, InputValidationException{
        //comprobar peticion correcta al recurso colecion
        String path = req.getPathInfo();
        if(path != null && !path.equals("/")){
            throw new InputValidationException("Invalid path");
        }
        //si no es que es buena la peticion, los parametros vienen en la URL (en este caso el email)
        String email = ServletUtils.getMandatoryParameter(req,"email");
        //ahora
        List<Inscripcion> inscripciones = fictrainingServiceFactory.getService().findInscripciones(email);
        //convertirlas
        List<RestInscripcionDto> inscripcionesEnviar = InscripcionToRestInscripcionDtoConversor.toRestInscripcionDtos(inscripciones);
        //enviar
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK, JsonToRestInscripcionDtoConversor.toArrayNode(inscripcionesEnviar), null);
    }
}
