package es.udc.ws.app.client.service.exceptions;

import java.time.LocalDateTime;

public class ClientAlreadyInscripcionCanceledException extends RuntimeException {
    private final Long cursoId;
    private final Long inscripcionId;
    private final LocalDateTime fechaCancelacion;

    public Long getCursoId() {
        return cursoId;
    }

    public Long getInscripcionId() {
        return inscripcionId;
    }
    public LocalDateTime getFechaCancelacion() {
        return fechaCancelacion;
    }

    public ClientAlreadyInscripcionCanceledException(Long cursoId, Long inscripcionId, LocalDateTime fechaCancelacion) {
        super("The curso with id " + cursoId + " and inscripcion id " + inscripcionId + " has been canceled on "+fechaCancelacion.toString());
        this.cursoId = cursoId;
        this.inscripcionId = inscripcionId;
        this.fechaCancelacion = fechaCancelacion;
    }
}
