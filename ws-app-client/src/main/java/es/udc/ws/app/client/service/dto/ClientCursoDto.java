package es.udc.ws.app.client.service.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class ClientCursoDto {

    private Long cursoId;
    private String nombre;
    private String ciudad;
    private int maxPlazas;
    private int plazasReservadas;
    private float precio;
    private LocalDateTime fecha_inicio;

    public ClientCursoDto (Long cursoId, String nombre, String ciudad, int maxPlazas, int plazasLibres,
                  float precio, LocalDateTime fecha_inicio) {
        this.cursoId = cursoId;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.maxPlazas = maxPlazas;
        this.plazasReservadas = maxPlazas - plazasLibres;
        this.precio = precio;
        this.fecha_inicio = fecha_inicio.withNano(0);
    }

    public Long getCursoId() {
        return cursoId;
    }

    public void setCursoId(Long cursoId) {this.cursoId = cursoId;}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String name){
        this.nombre = name;
    }

    public void setCiudad(String ciudad){
        this.ciudad = ciudad;
    }

    public String getCiudad() {
        return ciudad;
    }

    public int getMaxPlazas() {
        return maxPlazas;
    }

    public void setMaxPlazas(int plazas){this.maxPlazas = plazas;}

    public int getPlazasReservadas() {
        return plazasReservadas;
    }

    public void setPlazasReservadas(int plazasReservadas) {
        this.plazasReservadas = plazasReservadas;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio){
        this.precio = precio;
    }

    public LocalDateTime getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(LocalDateTime fecha){
        this.fecha_inicio = fecha.withNano(0);
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.cursoId, this.nombre, this.ciudad, this.fecha_inicio, this.maxPlazas, this.plazasReservadas, this.precio);
    }

    @Override
    public boolean equals(Object o){
        if (this == o)
            return true;
        if(o == null || this.getClass() != o.getClass())
            return false;
        ClientCursoDto c = (ClientCursoDto) o;
        return (c.cursoId == this.cursoId && Float.compare(this.precio, c.precio) == 0 && this.ciudad.equals(c.ciudad) && c.nombre.equals(this.nombre) && c.plazasReservadas == this.plazasReservadas && c.maxPlazas == this.maxPlazas && c.fecha_inicio.equals(this.fecha_inicio)); //si tienen el mismo id (q es unico es q son iguales)
    }

    @Override
    public String toString(){
        return "CursoDto [cursoId=" + cursoId + ", nombre=" + nombre + ", ciudad=" + ciudad +
                ", maxPlazas=" + maxPlazas + ", plazasReservadas=" + plazasReservadas + ", precio=" + precio +
                ", fecha_inicio=" + fecha_inicio + "]";
    }
}
