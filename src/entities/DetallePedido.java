package entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DetallePedido extends Base{
    private int cantidad;
    private double subtotal;
    private Producto producto;

    public DetallePedido() {
    }

    public DetallePedido(int cantidad, Producto producto) {
        super();
        this.cantidad = cantidad;
        this.producto = producto;
        this.subtotal = calcular();
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.subtotal = calcular();
    }

    public double getSubtotal() {
        return subtotal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        this.subtotal = calcular();
    }

    private double calcular () {
        return producto.getPrecio() * cantidad;
    }

    @Override
    public String toString() {
        return "DetallePedido [cantidad=" + cantidad + ", subtotal=" + subtotal + ", producto=" + producto.getNombre() + "]";
    }





    



    

    
}
