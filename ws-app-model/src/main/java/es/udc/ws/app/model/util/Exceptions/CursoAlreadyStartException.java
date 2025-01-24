package es.udc.ws.app.model.util.Exceptions;

import java.time.LocalDateTime;

public class CursoAlreadyStartException extends Exception {

    private final Long cursoId;
    private final LocalDateTime fechaInicio;

    public Long getCursoId() {return cursoId;}
    public LocalDateTime getFechaInicio() {return fechaInicio;}

    public CursoAlreadyStartException(Long cursoId, LocalDateTime fechaInicio) {
        super("Curso with id= \"" +cursoId + "\" started on date: " + fechaInicio.toString());
        this.cursoId = cursoId;
        this.fechaInicio = fechaInicio;
    }
}
