package entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DetallePedido extends Base{
    private int cantidad;
    private double subtotal;
    private Producto producto;
    private Double precioUnitario;
    private Long pedidoId;

    public DetallePedido() {
        super();
    }

    public DetallePedido(int cantidad, Producto producto) {
        super();
        this.cantidad = cantidad;
        this.producto = producto;
        this.subtotal = calcular();
    }

    public DetallePedido(Long id, int cantidad, Double precioUnitario, Double subtotal,
            Producto producto, Long pedidoId,
            boolean eliminado, LocalDateTime createdAt) {
        super(id, eliminado, createdAt);
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.producto = producto;
        this.pedidoId = pedidoId;
    }

    private Double calcular() {
        return cantidad * precioUnitario;
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



    @Override
    public String toString() {
        return "DetallePedido [cantidad=" + cantidad + ", subtotal=" + subtotal + ", producto=" + producto.getNombre() + "]";
    }





    



    

    
}
