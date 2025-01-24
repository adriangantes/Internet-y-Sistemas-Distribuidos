package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientInscripcionDto;
import es.udc.ws.app.thrift.ThriftInscripcionDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientInscripcionDtoToThriftInscripcionDtoConversor {
    public static ClientInscripcionDto toClientInscripcionDto(ThriftInscripcionDto dto) {
        return new ClientInscripcionDto(dto.getInscripcionId(), dto.getCursoId(), dto.getEmail(), dto.getIban(), dto.getFecha_inscripcion(), dto.getFecha_cancelacion());
    }

    public static List<ClientInscripcionDto> toClientInscripcionDtos(List<ThriftInscripcionDto> dtos) {
        List<ClientInscripcionDto> inscripciones = new ArrayList<>(dtos.size());
        for (ThriftInscripcionDto dto : dtos) {
            inscripciones.add(toClientInscripcionDto(dto));
        }
        return inscripciones;
    }

}
