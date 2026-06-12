package entities;

import java.util.ArrayList;
import java.util.List;

public class Categoria extends Base{
    private String nombre;
    private String descripcion;
    private List<Producto> listaProductos;

    public Categoria(){}

    public Categoria(String nombre, String descripcion) {
        super();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.listaProductos = new ArrayList<>();
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
            listaProductos.add(producto);
            System.out.println("Producto agregado exitosamente.");
        }
    }

    public void mostrarProductos() {
        toString();
    }

    public void deleteProducto() {
        /*TO DO */
        /*TO DO */
        /*TO DO */
    }

    @Override
    public String toString() {
        return "Categoria [nombre=" + nombre + ", descripcion=" + descripcion + ", listaProductos=" + listaProductos
                + "]";
    }

    
}
