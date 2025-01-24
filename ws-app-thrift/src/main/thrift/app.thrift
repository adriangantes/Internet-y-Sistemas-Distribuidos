namespace java es.udc.ws.app.thrift

struct ThriftCursoDto {
    1: i64 cursoId
    2: string nombre
    3: string ciudad
    4: i32 maxPlazas
    5: i32 plazasLibres
    6: double precio
    7: string fecha_inicio
}

struct ThriftInscripcionDto {
    1: i64 inscripcionId
    2: i64 cursoId
    3: string email
    4: string iban
    5: string fecha_inscripcion
    6: string fecha_cancelacion
}


exception ThriftInputValidationException {
    1: string message
}

exception ThriftInstanceNotFoundException {
    1: string instanceId
    2: string instanceType
}

exception ThriftTooLateCancelException {
    1: i64 cursoId
    2: i64 inscripcionId
    3: string fecha_inicio
}

exception ThriftAlreadyInscripcionCanceledException{
    1: i64 cursoId
    2: i64 inscripcionId
    3: string fecha_cancelacion
}

exception ThriftInscripcionOtherUserException{
    1: i64 cursoId
    2: i64 inscripcionId
}

exception ThriftCursoAlreadyStartException{
    1: i64 cursoId
    2: string fechaInicio
}

exception ThriftFullCursoException{
    1: i64 cursoId
    2: i32 plazasLibres
}


service ThriftAppService {

    ThriftCursoDto addCurso(1: ThriftCursoDto cursoDto) throws (1: ThriftInputValidationException e)

    list<ThriftCursoDto> findCursos(1: string ciudad) throws (1: ThriftInputValidationException e)

    void cancelarInscripcion(1: i64 inscripcionId, 2: string email) throws (ThriftInputValidationException e, ThriftInstanceNotFoundException ee, ThriftTooLateCancelException eee, ThriftAlreadyInscripcionCanceledException eeee, ThriftInscripcionOtherUserException eeeee)

    list<ThriftInscripcionDto> findInscripciones(1: string email) throws (ThriftInputValidationException e)

    ThriftCursoDto findCurso(1: i64 idCurso) throws (ThriftInstanceNotFoundException e, ThriftInputValidationException ee)

    ThriftInscripcionDto inscribirse(1: i64 idCurso, 2: string email, 3: string iban) throws(ThriftInstanceNotFoundException e, ThriftInputValidationException ee, ThriftCursoAlreadyStartException eee, ThriftFullCursoException eeee)

}