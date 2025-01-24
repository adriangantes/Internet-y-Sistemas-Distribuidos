package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientCursoDto;
import es.udc.ws.app.thrift.ThriftCursoDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientCursoDtoToThriftCursoDtoConversor {

    public static ThriftCursoDto toThriftCursoDto(ClientCursoDto clientCursoDto) {
        Long cursoId = clientCursoDto.getCursoId();

        return new ThriftCursoDto(
                cursoId == null ? -1 : cursoId.longValue(),
                clientCursoDto.getNombre(),
                clientCursoDto.getCiudad(),
                clientCursoDto.getMaxPlazas(),
                clientCursoDto.getMaxPlazas() - clientCursoDto.getPlazasReservadas(),
                clientCursoDto.getPrecio(),
                clientCursoDto.getFecha_inicio().toString());
    }

    public static ClientCursoDto toClientCursoDto(ThriftCursoDto curso) {
        return new ClientCursoDto(curso.getCursoId(), curso.getNombre(), curso.getCiudad(), curso.getMaxPlazas(), curso.getPlazasLibres(), (float) curso.getPrecio(), LocalDateTime.parse(curso.getFecha_inicio()));
    }

    public static List<ClientCursoDto> toClientCursoDtos(List<ThriftCursoDto> cursos) {

        List<ClientCursoDto> clientCursoDtos = new ArrayList<>(cursos.size());

        for (ThriftCursoDto curso : cursos) {
            clientCursoDtos.add(toClientCursoDto(curso));
        }
        return clientCursoDtos;
    }
}
