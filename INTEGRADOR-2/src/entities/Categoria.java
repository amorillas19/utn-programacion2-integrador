package entities;

import java.util.ArrayList;
import java.util.Collections;
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
            producto.setCategoria(this);
            listaProductos.add(producto);
            System.out.println("Producto agregado exitosamente.");
        }
    }
     /* HABRIA QUE REVISAR ESTE MOSTRAR PRODUCTOS */
    public List<Producto> mostrarProductos() {
        return Collections.unmodifiableList(listaProductos);
    }

    public void deleteProducto (Producto producto) {
        /* TO DO */
        /* asegurar que no sea null */
        /* y que contains producto */
    }

    @Override
    public String toString() {
        return "Categoria [nombre=" + nombre + ", descripcion=" + descripcion + ", listaProductos=" + listaProductos
                + "]";
    }

    
}
