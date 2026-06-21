package entities;

import java.time.LocalDate;
import enums.*;

public class Pedido extends Base implements Calculable{
    private LocalDate fecha;
    private Estado estado;
    private Double total;
    private FormaPago formaPago;
    private Usuario usuario;

    /* OJO QUE LA LOGICA DE USUARIO ESTA PENDIENTE */

    public Pedido() {
    }

    

    public Pedido(LocalDate fecha, Estado estado, Double total, FormaPago formaPago) {
        this.fecha = LocalDate.now();
        this.estado = estado;
        this.total = total;
        this.formaPago = formaPago;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public void addDetallePedido (int cantidad, Double subtotal, Producto producto) {}

    public void findDetallePedidoByProducto (Producto producto) {}

    public void deleteDetallePedidoByProducto (Producto producto) {}

    public void calcularTotal(){};
}
