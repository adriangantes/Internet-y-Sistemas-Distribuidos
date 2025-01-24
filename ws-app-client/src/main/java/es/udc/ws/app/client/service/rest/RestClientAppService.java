package es.udc.ws.app.client.service.rest;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.ws.app.client.service.ClientAppService;
import es.udc.ws.app.client.service.dto.ClientCursoDto;
import es.udc.ws.app.client.service.dto.ClientInscripcionDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.app.client.service.rest.json.JsonToClientCursoDtoConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientExceptionConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientInscripcionDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import org.apache.hc.client5.http.fluent.Form;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpStatus;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class RestClientAppService implements ClientAppService {

    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientAppService.endpointAddress";
    private String endpointAddress;


    @Override
    public Long addCurso(ClientCursoDto curso) throws InputValidationException {

        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() + "cursos").
                    bodyStream(toInputStream(curso), ContentType.create("application/json")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientCursoDtoConversor.toClientCursoDto(response.getEntity().getContent()).getCursoId();

        } catch (InputValidationException e){
            throw e;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientCursoDto> findCursos(String City) throws InputValidationException{
        try{
            String encodedCity = URLEncoder.encode(City, StandardCharsets.UTF_8);
            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(getEndpointAddress() + "cursos?city=" + encodedCity).execute().returnResponse();
            validateStatusCode(HttpStatus.SC_OK, response);
            return JsonToClientCursoDtoConversor.toClientCursoDtos(response.getEntity().getContent());
        }catch (InputValidationException e){
            throw e;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long inscribirse(Long cursoId, String email, String iban) throws InputValidationException, ClientCursoAlreadyStartException, InstanceNotFoundException, ClientFullCursoException {
        try{
            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() + "inscripciones").
                    bodyForm(
                            Form.form().
                                    add("cursoId", Long.toString(cursoId)).
                                    add("email", email).
                                    add("iban", iban).build()).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientInscripcionDtoConversor.toClientInscripcionDto(response.getEntity().getContent()).getInscripcionId();

        }catch(InputValidationException | InstanceNotFoundException | ClientFullCursoException | ClientCursoAlreadyStartException e){
            throw e;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientCursoDto findCurso (Long id) throws InputValidationException, InstanceNotFoundException {

        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(getEndpointAddress() + "cursos/" + id).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientCursoDtoConversor.toClientCursoDto(response.getEntity().getContent());

        } catch (InputValidationException | InstanceNotFoundException e){
            throw e;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<ClientInscripcionDto> findInscripciones (String email) throws InputValidationException{
        try{
            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(getEndpointAddress() + "inscripciones?email=" + email).execute().returnResponse();
            validateStatusCode(HttpStatus.SC_OK, response);
            return JsonToClientInscripcionDtoConversor.toClientInscripcionDtos(response.getEntity().getContent());
        }catch(InputValidationException e){
            throw e;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private synchronized String getEndpointAddress() {
        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager
                    .getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }

    private InputStream toInputStream(ClientCursoDto curso) {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
                    JsonToClientCursoDtoConversor.toObjectNode(curso));

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void validateStatusCode(int successCode, ClassicHttpResponse response) throws Exception {
        try {

            int statusCode = response.getCode();

            if (statusCode == successCode) {
                return;
            }

            switch (statusCode) {
                case HttpStatus.SC_NOT_FOUND -> throw JsonToClientExceptionConversor.fromNotFoundErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_BAD_REQUEST -> throw JsonToClientExceptionConversor.fromBadRequestErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_FORBIDDEN -> throw JsonToClientExceptionConversor.fromForbiddenErrorCode(
                        response.getEntity().getContent());
                /*case HttpStatus.SC_GONE -> throw JsonToClientExceptionConversor.fromGoneErrorCode(
                        response.getEntity().getContent());*/
                default -> throw new RuntimeException("HTTP error; status code = "
                        + statusCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void cancelInscripcion(Long inscripcionId, String email) throws InputValidationException, InstanceNotFoundException, ClientTooLateCancelException, ClientAlreadyInscripcionCanceledException, ClientInscripcionOtherUserException {
        try{
            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() + "inscripciones/" + inscripcionId+"/cancel").bodyForm(
                    Form.form().add("email", email).build()).execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);
            //no deevuelve nada mas que la respuesta, se valida el codigo y si no casca
        }catch(InputValidationException | InstanceNotFoundException | ClientAlreadyInscripcionCanceledException | ClientInscripcionOtherUserException | ClientTooLateCancelException e){
            throw e;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
