package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.Curso.Curso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CursoToRestCursoDtoConversor {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm[:ss]");

    public static List<RestCursoDto> toRestCursosDtos(List<Curso> Cursos) {
        List<RestCursoDto> CursosDtos = new ArrayList<>(Cursos.size());
        for (Curso curso : Cursos) {
            CursosDtos.add(toRestCursoDto(curso));
        }
        return CursosDtos;
    }

    public static RestCursoDto toRestCursoDto(Curso curso) {
        return new RestCursoDto(curso.getCursoId(), curso.getNombre(), curso.getCiudad(), curso.getMaxPlazas(),
                curso.getPlazasLibres(), curso.getPrecio(), curso.getFecha_inicio().toString());
    }

    public static Curso toCursoAdd (RestCursoDto curso) {
        return new Curso(curso.getNombre(), curso.getCiudad(), curso.getMaxPlazas(),
                curso.getPrecio(), LocalDateTime.parse(curso.getFecha_inicio(), formatter));
    }
}
