package entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Categoria extends Base{
    private String nombre;
    private String descripcion;
    private List<Producto> listaProductos;

    public Categoria() {
    }

    public Categoria(String nombre, String descripcion) {
        super();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.listaProductos = new ArrayList<>();
    }

    public Categoria(Long id, String nombre, String descripcion, boolean eliminado, LocalDateTime createdAt) {
        super(id, eliminado, createdAt);
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Producto> getListaProductos() {
        return listaProductos;
    }

    public void addProducto (Producto producto){
        if (producto != null) {
            producto.setCategoria(this);
            listaProductos.add(producto);
            System.out.println("Producto agregado exitosamente.");
        }
    }

    public List<Producto> mostrarProductos() {
        return Collections.unmodifiableList(listaProductos);
    }

    public void deleteProducto (Producto producto) {
        Producto aux = null;
        if (producto != null) {
            producto.setCategoria(null);
            for (Producto pro : listaProductos) {
                if (pro.equals(producto)) {
                    aux = pro;
                    System.out.println("Producto encontrado");
                }
            }
        }
        listaProductos.remove(aux);
        System.out.println("Producto " + aux.getNombre() + " borrado exitosamente");
    }

    @Override
    public String toString() {
        return "Categoria [id=" + getId() + ", nombre=" + nombre + ", descripcion=" + descripcion + ", listaProductos=" + listaProductos
                + "]";
    }

    
}
