package es.udc.ws.app.client.service.exceptions;

public class ClientFullCursoException extends RuntimeException {
    private final Long cursoId;
    private final int plazasLibres;

    public ClientFullCursoException(Long cursoId, int plazasLibres) {
        super("Curso with id: " + cursoId + " is already full (plazasLibres): " + plazasLibres);
        this.cursoId = cursoId;
        this.plazasLibres = plazasLibres;
    }

    public Long getCursoId() {
        return cursoId;
    }
    public int getPlazasLibres() {
        return plazasLibres;
    }
}
