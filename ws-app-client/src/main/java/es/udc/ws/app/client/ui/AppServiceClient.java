package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.ClientAppService;
import es.udc.ws.app.client.service.ClientAppServiceFactory;
import es.udc.ws.app.client.service.dto.ClientCursoDto;
import es.udc.ws.app.client.service.dto.ClientInscripcionDto;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;


public class AppServiceClient {

    public static void printUsage() {
        System.err.println("Usage:\n" +
                "    [add]    AppServiceClient -addCurso <nombre> <ciudad> <maxPlazas> <precio> <fecha_inicio>\n" +
                "    [buscarCursos]    AppServiceClient -findCursos <ciudad>\n" +
                "    [buscarCurso]    AppServiceClient -findCurso <cursoId>\n" +
                "    [inscribirse]    AppServiceClient -inscribirse <cursoId> <email> <numeroTarjeta>\n" +
                "    [cancelar]    AppServiceClient -cancel <inscripcionId> <email>\n" +
                "    [buscarInscripciones]    AppServiceClient -findInscripciones <email>\n");
    }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void validateArgs(String[] args, int expectedArgs, int[] numericArguments) {
        if(expectedArgs != args.length) {
            printUsageAndExit();
        }
        for(int i = 0 ; i< numericArguments.length ; i++) {
            int position = numericArguments[i];
            try {
                Double.parseDouble(args[position]);
            } catch(NumberFormatException n) {
                printUsageAndExit();
            }
        }
    }

    public static void main(String[] args) {

        if (args.length == 0) {
            printUsageAndExit();
        }

        ClientAppService clientAppService = ClientAppServiceFactory.getService();

        if ("-addCurso".equals(args[0])) {
            validateArgs(args, 6, new int[] {3, 4});

            try {
                Long cursoId = clientAppService.addCurso(new ClientCursoDto(null, args[1], args[2],
                        Short.parseShort(args[3]), Short.parseShort(args[3]), Float.parseFloat(args[4]),
                        LocalDateTime.parse(args[5])));

                System.out.println("Curso " + cursoId + " created sucessfully");

            } catch (Exception ex) {
                ex.printStackTrace(System.err);
                System.out.println("Excepcion: "+ex);
            }
        } else if ("-inscribirse".equals(args[0])) {
            validateArgs(args, 4, new int[] {1});

            Long inscripcionId;
            try{
                inscripcionId = clientAppService.inscribirse(Long.parseLong(args[1]), args[2], args[3]);
                System.out.println("Inscripcion " + inscripcionId + " created sucessfully");
            }catch (NumberFormatException e){
                e.printStackTrace(System.err);
            }catch (Exception e) {
                e.printStackTrace(System.err);
                System.out.println("Excepcion: "+e);
            }

        } else if ("-findCurso".equals(args[0])) {
            validateArgs(args, 2, new int[] {1});

            try {
                ClientCursoDto cursoDto = clientAppService.findCurso(Long.parseLong(args[1]));

                System.out.println("CursoId: " + cursoDto.getCursoId() +
                        "\nNombre: " + cursoDto.getNombre() +
                        "\nCiudad: " + cursoDto.getCiudad() +
                        "\nMax Plazas: " + cursoDto.getMaxPlazas() +
                        "\nPlazas Reservadas: " + cursoDto.getPlazasReservadas() +
                        "\nPrecio: " + cursoDto.getPrecio() +
                        "\nFecha Inicio: " + cursoDto.getFecha_inicio().toString());

            } catch (Exception ex) {
                ex.printStackTrace(System.err);
                System.out.println("Excepcion: "+ex);
            }
        }else if ("-findCursos".equals(args[0])) {
            validateArgs(args, 2, new int[] {});
            try{
                List<ClientCursoDto> cursos = clientAppService.findCursos(args[1]);
                System.out.println("Found "+ cursos.size()+ " cursos with City " + args[1]);
                for(ClientCursoDto c : cursos) {
                    System.out.println("CursoId: " + c.getCursoId() +
                            "\nNombre: " + c.getNombre() +
                            "\nCiudad: " + c.getCiudad() +
                            "\nMax Plazas: " + c.getMaxPlazas() +
                            "\nPlazas Reservadas: " + c.getPlazasReservadas() +
                            "\nPrecio: " + c.getPrecio() +
                            "\nFecha Inicio: " + c.getFecha_inicio().toString());
                }

            } catch(Exception e){
                e.printStackTrace(System.err);
                System.out.println("Excepcion: "+ e);
            }
        }else if("-findInscripciones".equals(args[0])) {
            validateArgs(args, 2, new int[] {});
            try{
                List<ClientInscripcionDto> inscripciones = clientAppService.findInscripciones(args[1]);
                System.out.println("Found "+ inscripciones.size() + " inscripciones with email " + args[1]);
                for(ClientInscripcionDto i : inscripciones) {
                    System.out.println("InscripcionId: " + i.getInscripcionId() +
                            "\nCursoId: " + i.getCursoId() +
                            "\nEmail: " + i.getEmail() +
                            "\nIBAN: " + i.getIban() +
                            "\nFecha Inscripcion: " + i.getFechaInscripcion() +
                            "\nFecha Cancelacion: " +  (i.getFechaCancelacion() != null ? i.getFechaCancelacion() : "Sin Cancelar"));
                }
            } catch (Exception e){
                e.printStackTrace(System.err);
                System.out.println("Excepcion: "+e);
            }
        }else if("-cancel".equals(args[0])) {
            validateArgs(args, 3, new int[] {1});
            try{
                Long id = Long.parseLong(args[1]);
                clientAppService.cancelInscripcion(id, args[2]);
                System.out.println("Inscripcion " + id + " cancelled sucessfully");
            }catch (Exception e){
                e.printStackTrace(System.err);
                System.out.println("Excepcion: "+e);
            }
        }else{
            //sacar pantalla help
            printUsageAndExit();
        }
    }
}