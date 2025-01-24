package es.udc.ws.app.client.service.exceptions;

public class ClientInscripcionOtherUserException extends Exception {
    private Long cursoId;
    private Long inscripcionId;

    public Long getCursoId() {return cursoId;}
    public Long getInscripcionId(){return inscripcionId;}
    public ClientInscripcionOtherUserException(Long cursoId, Long inscripcionId) {
        super("Other User Inscription \", curso with id= \"" +cursoId + "\" inscripcionId: " + inscripcionId);
        this.cursoId = cursoId;
        this.inscripcionId = inscripcionId;
    }
}
