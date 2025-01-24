package es.udc.ws.app.model.Curso;

import java.time.LocalDateTime;
import java.util.Objects;

public class Curso {
    private Long cursoId;
    private String nombre;
    private String ciudad;
    private int maxPlazas;
    private int plazasLibres;
    private float precio;
    private LocalDateTime fecha_inicio;
    private LocalDateTime fecha_creacion;

    public Curso (String nombre, String ciudad, int maxPlazas,
                  float precio, LocalDateTime fecha_inicio) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.maxPlazas = maxPlazas;
        this.plazasLibres = maxPlazas;
        this.precio = precio;
        this.fecha_inicio = fecha_inicio.withNano(0);
    }

    public Curso (Long cursoId, String nombre, String ciudad, int maxPlazas,
                  float precio, LocalDateTime fecha_inicio) {
        this(nombre, ciudad, maxPlazas, precio, fecha_inicio);
        this.cursoId = cursoId;
    }

    public Curso (Long cursoId, String nombre, String ciudad, int maxPlazas,
                  float precio, LocalDateTime fecha_inicio, LocalDateTime fecha_creacion) {

        this(cursoId, nombre, ciudad, maxPlazas, precio, fecha_inicio);
        this.fecha_creacion = (fecha_creacion != null) ? fecha_creacion.withNano(0) : null;
    }

    public Curso (Long cursoId, String nombre, String ciudad, int maxPlazas, int plazasLibres, float precio, LocalDateTime fecha_inicio, LocalDateTime fecha_creacion) {

        this(cursoId, nombre, ciudad, maxPlazas, precio, fecha_inicio);
        this.plazasLibres = plazasLibres;
        this.fecha_creacion = (fecha_creacion != null) ? fecha_creacion.withNano(0) : null;
    }

    public Curso (Long cursoId, String nombre, String ciudad, int maxPlazas, int plazasLibres, float precio, LocalDateTime fecha_inicio) {

        this(cursoId, nombre, ciudad, maxPlazas, precio, fecha_inicio);
        this.plazasLibres = plazasLibres;
    }

    public Long getCursoId() {
        return cursoId;
    }

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

    public int getPlazasLibres() {
        return plazasLibres;
    }

    public void setPlazasLibres(int plazasLibres) {
        this.plazasLibres = plazasLibres;
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

    public LocalDateTime getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(LocalDateTime fecha_creacion) {
        this.fecha_creacion = (fecha_creacion != null) ? fecha_creacion.withNano(0) : null;
    }
    public void setFecha_inicio(LocalDateTime fecha){
        this.fecha_inicio = fecha.withNano(0);
    }

    @Override
    public int hashCode(){
        //return ((Objects.hash(nombre) + 3*Objects.hash(ciudad) + 5*Objects.hash(maxPlazas) * Objects.hash(precio)) / (7*Objects.hash(fecha_inicio)));
        return Objects.hash(this.cursoId, this.nombre, this.ciudad, this.fecha_creacion, this.fecha_inicio, this.maxPlazas, this.plazasLibres, this.precio);
    }

    @Override
    public boolean equals(Object o){
        if (this == o)
            return true; //si es igual al objeto
        if(o == null || this.getClass() != o.getClass()) //validar primero q no sea null antes de comprobar las clases
            return false;
        Curso c = (Curso) o; //castear el objeto para comparar
        //comparar los valores de los atributos
        return (c.cursoId == this.cursoId && Float.compare(this.precio, c.precio) == 0 && this.ciudad.equals(c.ciudad) && c.nombre.equals(this.nombre) && c.plazasLibres == this.plazasLibres && c.maxPlazas == this.maxPlazas && c.fecha_inicio.equals(this.fecha_inicio)); //si tienen el mismo id (q es unico es q son iguales)
    }

}
