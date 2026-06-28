package services;

import java.util.List;

import dao.PedidoDAO;
import dao.ProductoDAO;
import dao.UsuarioDAO;
import entities.Pedido;
import entities.Producto;
import entities.Usuario;
import enums.Estado;
import enums.FormaPago;
import exception.*;

public class PedidoService {

    private final PedidoDAO   dao;
    private final UsuarioDAO  usuarioDAO;
    private final ProductoDAO productoDAO;

    public PedidoService(PedidoDAO dao, UsuarioDAO usuarioDAO, ProductoDAO productoDAO) {
        this.dao        = dao;
        this.usuarioDAO = usuarioDAO;
        this.productoDAO = productoDAO;
    }

    public List<Pedido> listar() { return dao.findAll(); }

    public List<Pedido> listarPorUsuario(Long usuarioId) {
        validarUsuario(usuarioId);
        return dao.findByUsuarioId(usuarioId);
    }

    public Pedido buscarPorId(Long id) {
        Pedido p = dao.findById(id);
        if (p == null) throw new ValidationException("No existe un pedido activo con id: " + id);
        return p;
    }

    public Pedido iniciarPedido(Long usuarioId, FormaPago formaPago) {
        Usuario usuario = validarUsuario(usuarioId);
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setFormaPago(formaPago);
        return pedido;
    }

    public void agregarDetalle(Pedido pedido, Long productoId, int cantidad) {
        if (cantidad <= 0) throw new ValidationException("La cantidad debe ser mayor a 0.");
        Producto prod = productoDAO.findById(productoId);
        if (prod == null) throw new ValidationException("No existe un producto activo con id: " + productoId);
        if (!prod.isDisponible()) throw new ValidationException("El producto '" + prod.getNombre() + "' no está disponible.");
        pedido.addDetallePedido(cantidad, prod.getPrecio(), prod);
    }

    public void confirmarPedido(Pedido pedido) {
        if (pedido.getListaDetallesPedidos().isEmpty())
            throw new ValidationException("El pedido debe tener al menos un detalle.");
        pedido.calcularTotal();
        dao.create(pedido);
    }

    public void actualizarEstadoYPago(Long id, Estado estado, FormaPago formaPago) {
        Pedido pedido = buscarPorId(id);
        pedido.setEstado(estado);
        pedido.setFormaPago(formaPago);
        dao.update(pedido);
    }

    public void eliminar(Long id) {
        buscarPorId(id);
        dao.delete(id);
    }

    private Usuario validarUsuario(Long usuarioId) {
        Usuario u = usuarioDAO.findById(usuarioId);
        if (u == null) throw new ValidationException("No existe un usuario activo con id: " + usuarioId);
        return u;
    }
}
