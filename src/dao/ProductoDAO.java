package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import config.DatabaseConnection;
import entities.Categoria;
import entities.Producto;
import exception.DAOException;

public class ProductoDAO implements GenericDAO<Producto> {

    private static final String SELECT_BASE =
            "SELECT p.*, c.nombre AS cat_nombre, c.descripcion AS cat_desc, " +
            "c.eliminado AS cat_eliminado, c.created_at AS cat_created " +
            "FROM productos p JOIN categorias c ON p.categoria_id = c.id ";

    @Override
    public void create(Producto p) {
        String sql = "INSERT INTO productos (nombre, precio, descripcion, stock, imagen, disponible, " +
                     "categoria_id, eliminado, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, false, NOW())";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, p.getNombre());
            stmt.setDouble(2, p.getPrecio());
            stmt.setString(3, p.getDescripcion());
            stmt.setInt(4, p.getStock());
            stmt.setString(5, p.getImagen());
            stmt.setBoolean(6, p.isDisponible());
            stmt.setLong(7, p.getCategoria().getId());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) p.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new DAOException("Error al crear producto: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Producto p) {
        String sql = "UPDATE productos SET nombre=?, precio=?, descripcion=?, stock=?, imagen=?, " +
                     "disponible=?, categoria_id=? WHERE id=? AND eliminado=false";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNombre());
            stmt.setDouble(2, p.getPrecio());
            stmt.setString(3, p.getDescripcion());
            stmt.setInt(4, p.getStock());
            stmt.setString(5, p.getImagen());
            stmt.setBoolean(6, p.isDisponible());
            stmt.setLong(7, p.getCategoria().getId());
            stmt.setLong(8, p.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al actualizar producto: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "UPDATE productos SET eliminado=true WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al eliminar producto: " + e.getMessage(), e);
        }
    }

    @Override
    public Producto findById(Long id) {
        String sql = SELECT_BASE + "WHERE p.id=? AND p.eliminado=false";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DAOException("Error al buscar producto: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Producto> findAll() {
        String sql = SELECT_BASE + "WHERE p.eliminado=false ORDER BY p.nombre";
        List<Producto> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new DAOException("Error al listar productos: " + e.getMessage(), e);
        }
        return list;
    }

    private Producto mapRow(ResultSet rs) throws SQLException {
        Categoria cat = new Categoria(
                rs.getLong("categoria_id"),
                rs.getString("cat_nombre"),
                rs.getString("cat_desc"),
                rs.getBoolean("cat_eliminado"),
                rs.getTimestamp("cat_created").toLocalDateTime()
        );
        return new Producto(
                rs.getLong("id"),
                rs.getString("nombre"),
                rs.getDouble("precio"),
                rs.getString("descripcion"),
                rs.getInt("stock"),
                rs.getString("imagen"),
                rs.getBoolean("disponible"),
                rs.getBoolean("eliminado"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                cat
        );
    }
}
