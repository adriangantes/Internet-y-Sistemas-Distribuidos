package es.udc.ws.app.model.util.Exceptions;

import java.time.LocalDateTime;

public class TooLateCancelException extends Exception {

    private Long cursoId;
    private Long inscripcionId;
    private LocalDateTime fechaInicio;

    public Long getCursoId() {return cursoId;}
    public Long getInscripcionId() {return inscripcionId;}
    public LocalDateTime getFechaInicio() {return fechaInicio;}

    public TooLateCancelException(Long cursoId, Long inscripcionId, LocalDateTime fechaInicio) {
        super("Too late to cancel, curso with id= \"" +cursoId + "\" starts on date: " + fechaInicio.toString());
        this.cursoId = cursoId;
        this.inscripcionId = inscripcionId;
        this.fechaInicio = fechaInicio;
    }
}
