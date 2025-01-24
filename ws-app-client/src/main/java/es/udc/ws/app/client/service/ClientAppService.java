package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.dto.ClientCursoDto;
import es.udc.ws.app.client.service.dto.ClientInscripcionDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;


import java.util.List;

public interface ClientAppService {

    public Long addCurso (ClientCursoDto curso) throws InputValidationException;

    public Long inscribirse (Long cursoId, String email, String iban) throws InputValidationException, InstanceNotFoundException, ClientCursoAlreadyStartException, ClientFullCursoException;

    public ClientCursoDto findCurso (Long id) throws InputValidationException, InstanceNotFoundException;

    public List<ClientCursoDto> findCursos(String City) throws InputValidationException;

    public List<ClientInscripcionDto> findInscripciones (String email) throws InputValidationException;

    public void cancelInscripcion (Long inscripcionId, String email) throws InputValidationException, InstanceNotFoundException, ClientTooLateCancelException, ClientAlreadyInscripcionCanceledException, ClientInscripcionOtherUserException;
}
