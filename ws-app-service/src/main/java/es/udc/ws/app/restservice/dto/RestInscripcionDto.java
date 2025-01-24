package es.udc.ws.app.restservice.dto;
import java.util.Objects;

public class RestInscripcionDto {
    private final Long inscripcionId;
    private final Long cursoId;
    private final String email;
    private final String iban; //solo ultimos 4 digitos
    private String fecha_inscripcion;
    private String fecha_cancelacion;

    public RestInscripcionDto(Long inscripcionId, Long cursoId, String email, String iban, String fecha_inscripcion) {
        this.inscripcionId = inscripcionId;
        this.cursoId = cursoId;
        this.email = email;
        this.iban = extractLast4digits(iban);
        this.fecha_inscripcion = fecha_inscripcion;
    }

    public RestInscripcionDto(Long inscripcionId, Long cursoId, String email, String iban, String fecha_inscripcion, String fecha_cancelacion) {
        this(inscripcionId, cursoId, email, iban, fecha_inscripcion);
        this.fecha_cancelacion = fecha_cancelacion;
    }

    private String extractLast4digits(String iban){
        return iban.substring(iban.length() - 4);
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
        RestInscripcionDto i = (RestInscripcionDto) o;
        return (this.inscripcionId.equals(i.inscripcionId) && this.cursoId.equals(i.cursoId) && this.email.equals(i.email) && this.iban.equals(i.iban) && this.fecha_inscripcion.equals(i.fecha_inscripcion));
    }

    public String getFecha_cancelacion() {
        return fecha_cancelacion;
    }

    public void setFecha_cancelacion(String fecha_cancelacion) {
        this.fecha_cancelacion = fecha_cancelacion;
    }

    public String getFecha_inscripcion() {
        return fecha_inscripcion;
    }

    public void setFecha_inscripcion(String fecha_inscripcion) {
        this.fecha_inscripcion = fecha_inscripcion;
    }
}
