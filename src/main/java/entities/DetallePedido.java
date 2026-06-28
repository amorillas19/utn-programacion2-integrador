package main.java.entities;

import java.time.LocalDateTime;

public class DetallePedido extends Base{

    private int cantidad;
    private double subtotal;
    private Producto producto;
    private Double precioUnitario;
    private Long pedidoId;

    public DetallePedido() {
        super();
    }

    public DetallePedido(int cantidad, Double precioUnitario, Producto producto) {
        super();
        this.cantidad = cantidad;
        this.producto = producto;
        this.precioUnitario =  precioUnitario;
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
        this.producto       = producto;
        this.precioUnitario = producto != null ? producto.getPrecio() : this.precioUnitario;
        this.subtotal       = calcular();
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double p) {
        this.precioUnitario = p;
        this.subtotal = calcular();
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    @Override
    public String toString() {
        String prod = producto != null ? producto.getNombre() : "-";
        return String.format("  %-20s x%-4d $%-10.2f => $%.2f", prod, cantidad, precioUnitario, subtotal);
    }

    





    



    

    
}
