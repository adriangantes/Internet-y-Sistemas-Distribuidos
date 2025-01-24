package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.Inscripcion.Inscripcion;
import es.udc.ws.app.thrift.ThriftInscripcionDto;

import java.util.ArrayList;
import java.util.List;

public class InscripcionToThriftInscripcionDtoConversor {
    public static ThriftInscripcionDto toThriftInscripcionDto (Inscripcion inscripcion) {
        return new ThriftInscripcionDto(inscripcion.getInscripcionId(), inscripcion.getCursoId(),
                inscripcion.getEmail(), inscripcion.getIban().substring(inscripcion.getIban().length() -4), inscripcion.getFecha_inscripcion().toString(),
                inscripcion.getFecha_cancelacion() == null ? "No cancelada " : inscripcion.getFecha_cancelacion().toString());
    }
    public static List<ThriftInscripcionDto> toThriftInscripcionDtos(List<Inscripcion> inscripciones){
        List<ThriftInscripcionDto> inscripcionesDto = new ArrayList<>(inscripciones.size());

        for(Inscripcion inscripcion : inscripciones) {
            inscripcionesDto.add(toThriftInscripcionDto(inscripcion));
        }
        return inscripcionesDto;
    }
}
