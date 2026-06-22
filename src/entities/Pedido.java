package entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import enums.*;

public class Pedido extends Base implements Calculable {
    private LocalDate fecha;
    private Estado estado;
    private Double total;
    private FormaPago formaPago;
    private Usuario usuario;
    private List<DetallePedido> listaDetallesPedidos;

    /* OJO QUE LA LOGICA DE USUARIO ESTA PENDIENTE */

    public Pedido() {
    }

    public Pedido(LocalDate fecha, Estado estado, Double total, FormaPago formaPago) {
        super();
        this.fecha = LocalDate.now();
        this.estado = estado;
        this.total = total;
        this.formaPago = formaPago;
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

    public void addDetallePedido(int cantidad, Producto producto) {
        DetallePedido detallePedido = new DetallePedido(cantidad, producto);
        if (detallePedido != null) {
            listaDetallesPedidos.add(detallePedido);
        }
    }

    public void findDetallePedidoByProducto(Producto producto) {
        DetallePedido aux = null;
        for (DetallePedido detallePedido : listaDetallesPedidos) {
            if (detallePedido.getProducto().equals(producto)) {
                System.out.println("Producto encontrado");
                aux = detallePedido;
            }
        }
        System.out.println("El detalle pedido es: ");
        System.out.println(aux);
    }

    public void deleteDetallePedidoByProducto(Producto producto) {
        DetallePedido aux = null;
        for (DetallePedido detallePedido : listaDetallesPedidos) {
            if (detallePedido.getProducto().equals(producto)) {
                System.out.println("Producto encontrado");
                aux = detallePedido;
            }
        }
        listaDetallesPedidos.remove(aux);
        System.out.println("Producto removido exitosamente");
    }

    public void calcularTotal() {
        Double totalAux = 0.0;
        for (DetallePedido detallePedido : listaDetallesPedidos) {
            totalAux += detallePedido.getSubtotal();
        }
        System.out.println("El total es de: " + totalAux);
    }

    @Override
    public String toString() {
        return "Pedido [fecha=" + fecha + ", estado=" + estado + ", total=" + total + ", formaPago=" + formaPago
                + ", usuario=" + usuario + ", listaDetallesPedidos=" + listaDetallesPedidos + "]";
    };

    
}
