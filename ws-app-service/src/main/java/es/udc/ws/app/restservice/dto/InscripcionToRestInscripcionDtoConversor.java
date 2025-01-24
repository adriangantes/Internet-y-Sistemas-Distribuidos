package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.Inscripcion.Inscripcion;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class InscripcionToRestInscripcionDtoConversor {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static List<RestInscripcionDto> toRestInscripcionDtos(List<Inscripcion> inscripciones) {
        List<RestInscripcionDto> inscripcionesDtos = new ArrayList<>(inscripciones.size());
        for (Inscripcion inscripcion : inscripciones) {
            inscripcionesDtos.add(toRestInscripcionDto(inscripcion));
        }
        return inscripcionesDtos;
    }

    public static RestInscripcionDto toRestInscripcionDto(Inscripcion inscripcion) {
        LocalDateTime date = inscripcion.getFecha_cancelacion();
        String fecha_cancelacion = null;
        if(date != null) {
            fecha_cancelacion = date.toString();
        }
        return new RestInscripcionDto(inscripcion.getInscripcionId(), inscripcion.getCursoId(), inscripcion.getEmail(),
                inscripcion.getIban(), inscripcion.getFecha_inscripcion().toString(), fecha_cancelacion );
    }

    public static Inscripcion toInscripcionInscribirse(RestInscripcionDto inscripcion) {
        String date = inscripcion.getFecha_cancelacion();
        LocalDateTime fecha_cancelacion = null;
        if(date != null) {
            fecha_cancelacion = LocalDateTime.parse(date, formatter);
        }
        Inscripcion inscripcionFinal = new Inscripcion(inscripcion.getCursoId(), inscripcion.getEmail(),
                inscripcion.getIban(), LocalDateTime.parse(inscripcion.getFecha_inscripcion(), formatter));
        inscripcionFinal.setFecha_cancelacion(fecha_cancelacion);
        return inscripcionFinal;
    }

}
