package es.udc.ws.app.model.util.Exceptions;

import java.time.LocalDateTime;

public class AlreadyInscripcionCanceledException extends Exception {
    private final Long cursoId;
    private final Long inscripcionId;
    private final LocalDateTime fechaCancelacion;

    public Long getCursoId() {return cursoId;}
    public Long getInscripcionId() {return inscripcionId;}
    public LocalDateTime getFechaCancelacion() {return fechaCancelacion;}

    public AlreadyInscripcionCanceledException(Long cursoId, Long inscripcionId, LocalDateTime fechaCancelacion) {
        super("The curso with id= \"" +cursoId + "\" was already canceled " + fechaCancelacion.toString());
        this.cursoId = cursoId;
        this.inscripcionId = inscripcionId;
        this.fechaCancelacion = fechaCancelacion;
    }
}
