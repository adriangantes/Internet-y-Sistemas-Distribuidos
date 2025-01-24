package es.udc.ws.app.client.service.exceptions;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ClientCursoAlreadyStartException extends Exception {
   private final Long cursoId;
   private final LocalDateTime fechaInicio;

   public ClientCursoAlreadyStartException(Long cursoId, LocalDateTime fechaInicio) {
       super("Curso with id: " + cursoId + " started on date " + fechaInicio.toString());
       this.cursoId = cursoId;
       this.fechaInicio = fechaInicio;
   }

   public Long getCursoId() {
       return cursoId;
   }

   public LocalDateTime getFechaInicio() {
       return fechaInicio;
   }
}
