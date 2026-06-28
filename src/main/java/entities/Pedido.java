package main.java.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import main.java.enums.*;

public class Pedido extends Base implements Calculable {
    private LocalDate fecha;
    private Estado estado;
    private Double total;
    private FormaPago formaPago;
    private Usuario usuario;
    private List<DetallePedido> listaDetallesPedidos;

    /* OJO QUE LA LOGICA DE USUARIO ESTA PENDIENTE */

    public Pedido() {
        super();
    }

    public Pedido(Estado estado, Double total, FormaPago formaPago) {
        super();
        this.fecha = LocalDate.now();
        this.estado = Estado.PENDIENTE;
        this.total = 0.0;
        this.formaPago = formaPago;
        this.listaDetallesPedidos = new ArrayList<>();
    }

    public Pedido(Long id, LocalDate fecha, Estado estado, Double total,
            FormaPago formaPago, Usuario usuario,
            boolean eliminado, LocalDateTime createdAt) {
        super(id, eliminado, createdAt);
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
        this.formaPago = formaPago;
        this.usuario = usuario;
        this.listaDetallesPedidos = new ArrayList<>();
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<DetallePedido> getListaDetallesPedidos() {
        return listaDetallesPedidos;
    }

    public void setListaDetallesPedidos(List<DetallePedido> listaDetallesPedidos) {
        this.listaDetallesPedidos = listaDetallesPedidos;
    }

    public void addDetallePedido(int cantidad, Double precioProducto, Producto producto) {
        listaDetallesPedidos.add(new DetallePedido(cantidad, precioProducto, producto));
    }

    public DetallePedido findDetallePedidoByProducto(Producto producto) {
        for (DetallePedido detallePedido : listaDetallesPedidos) {
            if (detallePedido.getProducto().equals(producto)) {
                System.out.println("El detalle pedido ha sido encontrado");
                return detallePedido;
            }
        }
        System.out.println("El detalle pedido NO ha sido encontrado");
        return null;
    }

    public void deleteDetallePedidoByProducto(Producto producto) {
        DetallePedido aux = null;
        for (DetallePedido detallePedido : listaDetallesPedidos) {
            if (detallePedido.getProducto().equals(producto)) {
                aux = detallePedido;
                break;
            }
        }
        if (aux == null) {
            System.out.println("Producto no encontrado");
            return;
        }
        listaDetallesPedidos.remove(aux);
        System.out.println("Producto removido exitosamente");
    }

    public void calcularTotal() {
        Double totalAux = 0.0;
        for (DetallePedido detallePedido : listaDetallesPedidos) {
            totalAux += detallePedido.getSubtotal();
        }
        this.total = totalAux;
    }

    @Override
    public String toString() {
        String usr = usuario != null ? usuario.getNombre() + " " + usuario.getApellido() : "-";
        return String.format("[%d] %s | Estado: %-12s | Pago: %-14s | Total: $%-10.2f | Usuario: %s",
                getId(), fecha, estado, formaPago, total, usr);
    }

}
