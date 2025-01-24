package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.ClientAppService;
import es.udc.ws.app.client.service.dto.ClientCursoDto;
import es.udc.ws.app.client.service.dto.ClientInscripcionDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.app.thrift.*;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.time.LocalDateTime;
import java.util.List;

public class ThriftClientAppService implements ClientAppService {

    private final static String ENDPOINT_ADDRESS_PARAMETER = "ThriftClientAppService.endpointAddress";
    private final static String endpointAddress = ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);

    @Override
    public Long addCurso(ClientCursoDto curso) throws InputValidationException {

        ThriftAppService.Client client = getClient();


        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return client.addCurso(ClientCursoDtoToThriftCursoDtoConversor.toThriftCursoDto(curso)).getCursoId();

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long inscribirse(Long cursoId, String email, String iban) throws InputValidationException, InstanceNotFoundException, ClientCursoAlreadyStartException, ClientFullCursoException {
        ThriftAppService.Client client = getClient();


        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return ClientInscripcionDtoToThriftInscripcionDtoConversor.toClientInscripcionDto(client.inscribirse(cursoId, email, iban)).getInscripcionId();

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (ThriftCursoAlreadyStartException e) {
            throw new ClientCursoAlreadyStartException(e.getCursoId(), LocalDateTime.parse(e.getFechaInicio()));
        } catch (ThriftFullCursoException e) {
            throw new ClientFullCursoException(e.getCursoId(), e.getPlazasLibres());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientCursoDto findCurso(Long id) throws InputValidationException, InstanceNotFoundException {

        ThriftAppService.Client client = getClient();

        try(TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();

            return ClientCursoDtoToThriftCursoDtoConversor.toClientCursoDto(client.findCurso(id));
        }catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        }catch (ThriftInstanceNotFoundException e){
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientCursoDto> findCursos(String ciudad) throws InputValidationException {

        ThriftAppService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return ClientCursoDtoToThriftCursoDtoConversor.toClientCursoDtos(client.findCursos(ciudad));

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientInscripcionDto> findInscripciones(String email) throws InputValidationException {
        ThriftAppService.Client client = getClient();


        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return ClientInscripcionDtoToThriftInscripcionDtoConversor.toClientInscripcionDtos(client.findInscripciones(email));

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancelInscripcion(Long inscripcionId, String email) throws InputValidationException, InstanceNotFoundException, ClientTooLateCancelException, ClientAlreadyInscripcionCanceledException, ClientInscripcionOtherUserException {
        ThriftAppService.Client client = getClient();


        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            client.cancelarInscripcion(inscripcionId, email);

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch(ThriftInstanceNotFoundException e){
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        }catch(ThriftTooLateCancelException e){
            throw new ClientTooLateCancelException(e.getCursoId(), e.getInscripcionId(), LocalDateTime.parse(e.getFecha_inicio()));
        } catch(ThriftAlreadyInscripcionCanceledException e){
            throw new ClientAlreadyInscripcionCanceledException(e.getCursoId(), e.getInscripcionId(), LocalDateTime.parse(e.getFecha_cancelacion()));
        }catch(ThriftInscripcionOtherUserException e){
            throw new ClientInscripcionOtherUserException(e.getCursoId(), e.getInscripcionId());
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ThriftAppService.Client getClient() {

        try {

            TTransport transport = new THttpClient(endpointAddress);
            TProtocol protocol = new TBinaryProtocol(transport);

            return new ThriftAppService.Client(protocol);

        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }

    }
}
