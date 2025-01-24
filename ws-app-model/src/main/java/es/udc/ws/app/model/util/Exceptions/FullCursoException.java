package es.udc.ws.app.model.util.Exceptions;

public class FullCursoException extends Exception {

    private final Long cursoId;
    private final int plazasLibres;

    public FullCursoException(Long cursoId, int plazasLibres) {
        super("Curso with id= \"" +cursoId + "\" is out of places");
        this.cursoId = cursoId;
        this.plazasLibres = plazasLibres;
    }

    public Long getCursoId() {return cursoId;}
    public int getPlazasLibres() {return plazasLibres;}
}
