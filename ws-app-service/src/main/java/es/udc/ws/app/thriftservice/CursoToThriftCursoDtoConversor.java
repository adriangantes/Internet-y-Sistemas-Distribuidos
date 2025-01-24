package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.Curso.Curso;
import es.udc.ws.app.thrift.ThriftCursoDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CursoToThriftCursoDtoConversor {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm[:ss]");

    public static Curso toCurso (ThriftCursoDto curso){
        return new Curso (curso.getCursoId(), curso.getNombre(), curso.getCiudad(), curso.getMaxPlazas(),
                curso.getPlazasLibres(), Double.valueOf(curso.getPrecio()).floatValue(), LocalDateTime.parse(curso.getFecha_inicio(), formatter));
    }

    public static ThriftCursoDto toThriftCursoDto (Curso curso){
        return new ThriftCursoDto(curso.getCursoId(), curso.getNombre(), curso.getCiudad(), curso.getMaxPlazas(),
                curso.getPlazasLibres(), curso.getPrecio(), curso.getFecha_inicio().toString());
    }

    public static List<ThriftCursoDto> thriftCursoDtoList (List<Curso> cursos){
        List<ThriftCursoDto> cursosDtos = new ArrayList<>(cursos.size());
        for (Curso curso : cursos) {
            cursosDtos.add(toThriftCursoDto(curso));
        }
        return cursosDtos;
    }
}
