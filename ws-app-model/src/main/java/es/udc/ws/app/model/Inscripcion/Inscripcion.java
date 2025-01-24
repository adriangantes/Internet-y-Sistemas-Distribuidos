package es.udc.ws.app.model.Inscripcion;

import java.time.LocalDateTime;
import java.util.Objects;

public class Inscripcion {
    private Long inscripcionId;
    private final Long cursoId;
    private final String email;
    private final String iban;
    private LocalDateTime fecha_inscripcion;
    private LocalDateTime fecha_cancelacion;

    public Inscripcion (Long cursoId, String email, String iban, LocalDateTime fecha_inscripcion) {
        this.cursoId = cursoId;
        this.email = email;
        this.iban = iban;
        this.fecha_inscripcion = (fecha_inscripcion != null) ? fecha_inscripcion.withNano(0) : null;
        this.fecha_cancelacion = null;
    }

    public Inscripcion (Long inscripcionId, Long cursoId, String email, String iban, LocalDateTime fecha_inscripcion){
        this(cursoId, email, iban, fecha_inscripcion);
        this.inscripcionId = inscripcionId;
    }


    public Inscripcion(Long inscripcionId, Long cursoId, String email, String iban, LocalDateTime fecha_inscripcion, LocalDateTime fecha_cancelacion) {
        this(inscripcionId, cursoId, email, iban, fecha_inscripcion);
        if(fecha_cancelacion != null) {
            this.fecha_cancelacion = fecha_cancelacion.withNano(0);
        }else{
            this.fecha_cancelacion = null;
        }
    }

    public String getIban() {
        return iban;
    }

    public String getEmail() {
        return email;
    }

    public Long getCursoId() {
        return cursoId;
    }

    public Long getInscripcionId() {
        return inscripcionId;
    }


    @Override
    public int hashCode(){
        //return Objects.hash(incursoId, email, iban);
        return Objects.hash(inscripcionId, cursoId, email, iban, fecha_inscripcion);
    }

    @Override
    public boolean equals(Object o){
        if (this == o){
            return true;
        }
        if (o == null || this.getClass() != o.getClass()){
            return false;
        }
        Inscripcion i = (Inscripcion) o;
        return (this.inscripcionId.equals(i.inscripcionId) && this.cursoId.equals(i.cursoId) && this.email.equals(i.email) && this.iban.equals(i.iban) && this.fecha_inscripcion.equals(i.fecha_inscripcion));
    }

    public LocalDateTime getFecha_cancelacion() {
        return fecha_cancelacion;
    }

    public void setFecha_cancelacion(LocalDateTime fecha_cancelacion) {
        if(fecha_cancelacion != null) {
            this.fecha_cancelacion = fecha_cancelacion.withNano(0);
        }else{
            this.fecha_cancelacion = null;
        }
    }

    public LocalDateTime getFecha_inscripcion() {
        return fecha_inscripcion;
    }

    public void setFecha_inscripcion(LocalDateTime fecha_inscripcion) {
        this.fecha_inscripcion = fecha_inscripcion.withNano(0);
    }
}
