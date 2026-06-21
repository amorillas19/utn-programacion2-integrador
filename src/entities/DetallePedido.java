package entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DetallePedido extends Base{
    private int cantidad;
    private double subtotal;
    private List<Producto> listaProductos;

    public DetallePedido() {
    }

    public DetallePedido(int cantidad, double subtotal) {
        super();
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.listaProductos = new ArrayList<>();
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public List<Producto> getListaProductos() {
        return listaProductos;
    }

    public void addProducto (Producto producto) {
        if (producto != null) {
            listaProductos.add(producto);
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
        return "DetallePedido [cantidad=" + cantidad + ", subtotal=" + subtotal + ", listaProductos=" + listaProductos
                + "]";
    }

    



    

    
}
