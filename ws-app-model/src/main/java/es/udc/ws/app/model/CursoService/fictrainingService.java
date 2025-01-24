package es.udc.ws.app.model.CursoService;
import es.udc.ws.app.model.Curso.Curso;
import es.udc.ws.app.model.util.Exceptions.*;
import es.udc.ws.app.model.Inscripcion.Inscripcion;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface fictrainingService {

    public Curso addCurso(Curso curso) throws InputValidationException;

    public Curso findCurso(Long cursoId) throws InstanceNotFoundException, InputValidationException;

    public List<Curso> findCursos(String Ciudad, LocalDateTime fecha)
            throws InputValidationException;

    public Inscripcion inscribirse (Long cursoId, String email, String iban)
            throws InputValidationException, CursoAlreadyStartException, InstanceNotFoundException, FullCursoException;

    public void cancelInscripcion (Long inscripcionId, String Email)
            throws InputValidationException, TooLateCancelException, AlreadyInscripcionCanceledException, InstanceNotFoundException, InscripcionOtherUserException;

    public List<Inscripcion> findInscripciones(String email) throws InputValidationException;
}
