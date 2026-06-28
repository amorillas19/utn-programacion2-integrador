package main.java.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import main.java.config.DatabaseConnection;
import main.java.entities.Categoria;
import main.java.entities.DetallePedido;
import main.java.entities.Producto;
import main.java.exception.DAOException;

public class DetallePedidoDAO implements GenericDAO<DetallePedido> {

    @Override
    public void create(DetallePedido d) {
        throw new UnsupportedOperationException("Usar PedidoDAO para crear detalles dentro de una transacción.");
    }

    @Override
    public void update(DetallePedido d) {
        throw new UnsupportedOperationException("Actualización de detalles no soportada.");
    }

    @Override
    public void delete(Long id) {
        String sql = "UPDATE detalles_pedido SET eliminado=true WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al eliminar detalle: " + e.getMessage(), e);
        }
    }

    @Override
    public DetallePedido findById(Long id) {
        String sql = buildSelectBase() + "WHERE dp.id=? AND dp.eliminado=false";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DAOException("Error al buscar detalle: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<DetallePedido> findAll() {
        throw new UnsupportedOperationException("Usar findByPedidoId.");
    }

    public List<DetallePedido> findByPedidoId(Long pedidoId) {
        String sql = buildSelectBase() + "WHERE dp.pedido_id=? AND dp.eliminado=false";
        List<DetallePedido> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, pedidoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Error al listar detalles: " + e.getMessage(), e);
        }
        return list;
    }

    /** Método de inserción usado por PedidoDAO dentro de una transacción existente. */
    public void insertEnTransaccion(DetallePedido d, Long pedidoId, Connection conn) throws SQLException {
        String sql = "INSERT INTO detalles_pedido (cantidad, precio_unitario, subtotal, producto_id, pedido_id, eliminado, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, false, NOW())";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, d.getCantidad());
            stmt.setDouble(2, d.getPrecioUnitario());
            stmt.setDouble(3, d.getSubtotal());
            stmt.setLong(4, d.getProducto().getId());
            stmt.setLong(5, pedidoId);
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) d.setId(keys.getLong(1));
            }
        }
    }

    private String buildSelectBase() {
        return "SELECT dp.*, p.nombre AS prod_nombre, p.precio AS prod_precio, p.descripcion AS prod_desc, " +
               "p.stock AS prod_stock, p.imagen AS prod_imagen, p.disponible AS prod_disponible, " +
               "p.eliminado AS prod_eliminado, p.created_at AS prod_created, p.categoria_id, " +
               "c.nombre AS cat_nombre, c.descripcion AS cat_desc, c.eliminado AS cat_eliminado, c.created_at AS cat_created " +
               "FROM detalles_pedido dp " +
               "JOIN productos p ON dp.producto_id = p.id " +
               "JOIN categorias c ON p.categoria_id = c.id ";
    }

    private DetallePedido mapRow(ResultSet rs) throws SQLException {
        Categoria cat = new Categoria(
                rs.getLong("categoria_id"),
                rs.getString("cat_nombre"),
                rs.getString("cat_desc"),
                rs.getBoolean("cat_eliminado"),
                rs.getTimestamp("cat_created").toLocalDateTime()
        );
        Producto prod = new Producto(
                rs.getLong("producto_id"),
                rs.getString("prod_nombre"),
                rs.getDouble("prod_precio"),
                rs.getString("prod_desc"),
                rs.getInt("prod_stock"),
                rs.getString("prod_imagen"),
                rs.getBoolean("prod_disponible"),
                rs.getBoolean("prod_eliminado"),
                rs.getTimestamp("prod_created").toLocalDateTime(),
                cat
        );
        return new DetallePedido(
                rs.getLong("id"),
                rs.getInt("cantidad"),
                rs.getDouble("precio_unitario"),
                rs.getDouble("subtotal"),
                prod,
                rs.getLong("pedido_id"),
                rs.getBoolean("eliminado"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}

