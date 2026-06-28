package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import config.DatabaseConnection;
import entities.DetallePedido;
import entities.Pedido;
import entities.Usuario;
import enums.Estado;
import enums.FormaPago;
import enums.Rol;
import exception.DAOException;

public class PedidoDAO implements GenericDAO<Pedido> {

    private final DetallePedidoDAO detalleDAO;

    public PedidoDAO(DetallePedidoDAO detalleDAO) {
        this.detalleDAO = detalleDAO;
    }

    /** Inserta el pedido y sus detalles en una sola transacción. */
    @Override
    public void create(Pedido pedido) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            long pedidoId = insertPedido(conn, pedido);
            pedido.setId(pedidoId);

            for (DetallePedido d : pedido.getListaDetallesPedidos()) {
                detalleDAO.insertEnTransaccion(d, pedidoId, conn);
            }

            conn.commit();
        } catch (SQLException e) {
            rollback(conn);
            throw new DAOException("Error al crear pedido (transacción revertida): " + e.getMessage(), e);
        } finally {
            closeConexion(conn);
        }
    }

    @Override
    public void update(Pedido pedido) {
        String sql = "UPDATE pedidos SET estado=?, forma_pago=?, total=? WHERE id=? AND eliminado=false";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pedido.getEstado().name());
            stmt.setString(2, pedido.getFormaPago().name());
            stmt.setDouble(3, pedido.getTotal());
            stmt.setLong(4, pedido.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al actualizar pedido: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "UPDATE pedidos SET eliminado=true WHERE id=?";
        String sqlDetalles = "UPDATE detalles_pedido SET eliminado=true WHERE pedido_id=?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement stmt1 = conn.prepareStatement(sql);
                 PreparedStatement stmt2 = conn.prepareStatement(sqlDetalles)) {
                stmt1.setLong(1, id);
                stmt2.setLong(1, id);
                stmt1.executeUpdate();
                stmt2.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            rollback(conn);
            throw new DAOException("Error al eliminar pedido: " + e.getMessage(), e);
        } finally {
            closeConexion(conn);
        }
    }

    @Override
    public Pedido findById(Long id) {
        String sql = buildSelectBase() + "WHERE p.id=? AND p.eliminado=false";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Pedido pedido = mapRow(rs);
                    pedido.setListaDetallesPedidos(detalleDAO.findByPedidoId(id));
                    return pedido;
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al buscar pedido: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Pedido> findAll() {
        String sql = buildSelectBase() + "WHERE p.eliminado=false ORDER BY p.fecha DESC";
        List<Pedido> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new DAOException("Error al listar pedidos: " + e.getMessage(), e);
        }
        return list;
    }

    public List<Pedido> findByUsuarioId(Long usuarioId) {
        String sql = buildSelectBase() + "WHERE p.eliminado=false AND p.usuario_id=? ORDER BY p.fecha DESC";
        List<Pedido> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Error al listar pedidos por usuario: " + e.getMessage(), e);
        }
        return list;
    }

    // ── helpers ───────────────────────────────────────────────────────────

    private long insertPedido(Connection conn, Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedidos (fecha, estado, total, forma_pago, usuario_id, eliminado, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, false, NOW())";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, Date.valueOf(pedido.getFecha()));
            stmt.setString(2, pedido.getEstado().name());
            stmt.setDouble(3, pedido.getTotal());
            stmt.setString(4, pedido.getFormaPago().name());
            stmt.setLong(5, pedido.getUsuario().getId());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) return keys.getLong(1);
                throw new SQLException("No se obtuvo ID generado para el pedido.");
            }
        }
    }

    private String buildSelectBase() {
        return """
                SELECT p.*,
                       u.nombre     AS usr_nombre,
                       u.apellido   AS usr_apellido,
                       u.mail       AS usr_mail,
                       u.celular    AS usr_celular,
                       u.contrasena AS usr_contrasena,
                       u.rol        AS usr_rol,
                       u.eliminado  AS usr_eliminado,
                       u.created_at AS usr_created
                FROM pedidos p
                JOIN usuarios u ON p.usuario_id = u.id""";
    }

    private Pedido mapRow(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario(
                rs.getLong("usuario_id"),
                rs.getString("usr_nombre"),
                rs.getString("usr_apellido"),
                rs.getString("usr_mail"),
                rs.getString("usr_celular"),
                rs.getString("usr_contrasena"),
                Rol.valueOf(rs.getString("usr_rol")),
                rs.getBoolean("usr_eliminado"),
                rs.getTimestamp("usr_created").toLocalDateTime()
        );
        return new Pedido(
                rs.getLong("id"),
                rs.getDate("fecha").toLocalDate(),
                Estado.valueOf(rs.getString("estado")),
                rs.getDouble("total"),
                FormaPago.valueOf(rs.getString("forma_pago")),
                usuario,
                rs.getBoolean("eliminado"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }

    private void rollback(Connection conn) {
        if (conn != null) {
            try { conn.rollback(); } catch (SQLException ignored) {}
        }
    }

    private void closeConexion(Connection conn) {
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException ignored) {}
        }
    }
}
