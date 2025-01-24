package es.udc.ws.app.client.service.dto;

import java.util.Objects;

public class ClientInscripcionDto {
    private final Long inscripcionId;
    private final Long cursoId;
    private final String email;
    private final String iban;
    private String fecha_inscripcion;
    private String fecha_cancelacion;

    public ClientInscripcionDto(Long inscripcionId, Long cursoId, String email, String iban, String fecha_inscripcion) {
        this.inscripcionId = inscripcionId;
        this.cursoId = cursoId;
        this.email = email;
        this.iban = iban;
        this.fecha_inscripcion = fecha_inscripcion;
    }

    public ClientInscripcionDto(Long inscripcionId, Long cursoId, String email, String iban, String fecha_inscripcion, String fecha_cancelacion) {
        this(inscripcionId, cursoId, email, iban, fecha_inscripcion);
        this.fecha_cancelacion = fecha_cancelacion;
    }

    public Long getInscripcionId() {return inscripcionId;}

    public Long getCursoId() {return cursoId;}

    public String getIban(){return iban;}

    public String getEmail(){return email;}

    public String getFechaInscripcion(){return fecha_inscripcion;}

    public String getFechaCancelacion(){return fecha_cancelacion;}

    @Override
    public int hashCode() {
        return Objects.hash(inscripcionId, cursoId, email, iban, fecha_inscripcion);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj == null || this.getClass() != obj.getClass()){
            return false;
        }
        ClientInscripcionDto i = (ClientInscripcionDto) obj;
        return (this.inscripcionId.equals(i.inscripcionId) && this.cursoId.equals(i.cursoId) && this.email.equals(i.email) && this.iban.equals(i.iban) && this.fecha_inscripcion.equals(i.fecha_inscripcion));
    }

}
