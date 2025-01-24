package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.Curso.Curso;
import es.udc.ws.app.model.CursoService.fictrainingServiceFactory;
import es.udc.ws.app.model.Inscripcion.Inscripcion;
import es.udc.ws.app.model.util.Exceptions.*;
import es.udc.ws.app.thrift.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.List;

import java.time.LocalDateTime;
import java.util.List;


public class ThriftAppServiceImpl implements ThriftAppService.Iface{

    @Override
    public ThriftCursoDto addCurso(ThriftCursoDto cursoDto) throws ThriftInputValidationException {

        Curso curso = CursoToThriftCursoDtoConversor.toCurso(cursoDto);

        try {
            Curso newCurso = fictrainingServiceFactory.getService().addCurso(curso);
            return CursoToThriftCursoDtoConversor.toThriftCursoDto(newCurso);
        } catch (InputValidationException e){
            throw new ThriftInputValidationException(e.getMessage());
        }
    }

    @Override
    public List<ThriftCursoDto> findCursos(String ciudad) throws ThriftInputValidationException {

        LocalDateTime fechaActual = LocalDateTime.now().withNano(0);

        try {
            return CursoToThriftCursoDtoConversor.thriftCursoDtoList(fictrainingServiceFactory.getService().findCursos(ciudad, fechaActual));
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }
    }

    @Override
    public ThriftCursoDto findCurso(long idCurso) throws ThriftInstanceNotFoundException, ThriftInputValidationException{
        try{
            return CursoToThriftCursoDtoConversor.toThriftCursoDto(fictrainingServiceFactory.getService().findCurso(idCurso));
        }catch(InstanceNotFoundException e){
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(), e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.')+1));
        }catch(InputValidationException e){
            throw new ThriftInputValidationException(e.getMessage());
        }
    }

    @Override
    public ThriftInscripcionDto inscribirse(long idCurso, String email, String iban)throws ThriftInputValidationException, ThriftCursoAlreadyStartException, ThriftInstanceNotFoundException, ThriftFullCursoException {
        try{
            return InscripcionToThriftInscripcionDtoConversor.toThriftInscripcionDto(fictrainingServiceFactory.getService().inscribirse(idCurso, email, iban));
        }catch(InputValidationException e){
            throw new ThriftInputValidationException(e.getMessage());
        } catch (CursoAlreadyStartException e) {
            throw new ThriftCursoAlreadyStartException(e.getCursoId(), e.getFechaInicio().toString());
        } catch (FullCursoException e) {
            throw new ThriftFullCursoException(e.getCursoId(), e.getPlazasLibres());
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(), e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.')+1));
        }
    }

    @Override
    public void cancelarInscripcion(long inscripcionId, String email) throws ThriftInputValidationException, ThriftInstanceNotFoundException, ThriftTooLateCancelException, ThriftAlreadyInscripcionCanceledException, ThriftInscripcionOtherUserException{
        try{
            fictrainingServiceFactory.getService().cancelInscripcion(inscripcionId, email);
        } catch (TooLateCancelException e) {
            throw new ThriftTooLateCancelException(e.getCursoId(), e.getInscripcionId(), e.getFechaInicio().toString());
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(), e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.')+1));
        } catch (AlreadyInscripcionCanceledException e) {
            throw new ThriftAlreadyInscripcionCanceledException(e.getInscripcionId(), e.getCursoId(), e.getFechaCancelacion().toString());
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        } catch (InscripcionOtherUserException e) {
            throw new ThriftInscripcionOtherUserException(e.getCursoId(), e.getInscripcionId());
        }
    }

    @Override
    public List<ThriftInscripcionDto> findInscripciones(String email) throws ThriftInputValidationException{
        try{
            List<Inscripcion> inscripcions = fictrainingServiceFactory.getService().findInscripciones(email);
            return InscripcionToThriftInscripcionDtoConversor.toThriftInscripcionDtos(inscripcions);
        }catch(InputValidationException e){
            throw new ThriftInputValidationException(e.getMessage());
        }
    }
}
