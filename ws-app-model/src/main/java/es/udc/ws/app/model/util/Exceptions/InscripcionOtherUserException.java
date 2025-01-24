package es.udc.ws.app.model.util.Exceptions;

public class InscripcionOtherUserException extends Exception {
    private final Long cursoId;
    private final Long inscripcionId;

    public Long getCursoId() {return cursoId;}
    public Long getInscripcionId(){return inscripcionId;}
    public InscripcionOtherUserException(Long cursoId, Long inscripcionId) {
        super("Other User Inscription \", curso with id= \"" +cursoId + "\" inscripcionId: " + inscripcionId);
        this.cursoId = cursoId;
        this.inscripcionId = inscripcionId;
    }
}
