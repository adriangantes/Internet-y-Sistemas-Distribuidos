package es.udc.ws.app.client.service.exceptions;

import java.time.LocalDateTime;

public class ClientTooLateCancelException extends Exception {
    private Long cursoId;
    private Long inscriptionId;
    private LocalDateTime fechaInicio;
    public ClientTooLateCancelException(Long cursoId, Long inscriptionId, LocalDateTime fechaInicio) {
        super("Too late to cancel curso: " + cursoId + ", Inscripcion:  " + inscriptionId + ", Start Date: " + fechaInicio.toString());
        this.cursoId = cursoId;
        this.inscriptionId = inscriptionId;
        this.fechaInicio = fechaInicio;
    }
    public Long getCursoId() {
        return cursoId;
    }
    public Long getInscriptionId() {
        return inscriptionId;
    }
    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }
}
