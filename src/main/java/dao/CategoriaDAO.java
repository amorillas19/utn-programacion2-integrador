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

public class CategoriaDAO implements GenericDAO<Categoria> {

    @Override
    public void create(Categoria c) {
        String sql = "INSERT INTO categorias (nombre, descripcion, eliminado, created_at) VALUES (?, ?, false, NOW())";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, c.getNombre());
            stmt.setString(2, c.getDescripcion());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) c.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new DAOException("Error al crear categoría: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Categoria c) {
        String sql = "UPDATE categorias SET nombre=?, descripcion=? WHERE id=? AND eliminado=false";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, c.getNombre());
            stmt.setString(2, c.getDescripcion());
            stmt.setLong(3, c.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al actualizar categoría: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "UPDATE categorias SET eliminado=true WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al eliminar categoría: " + e.getMessage(), e);
        }
    }

    @Override
    public Categoria findById(Long id) {
        String sql = "SELECT * FROM categorias WHERE id=? AND eliminado=false";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs, conn);
            }
        } catch (SQLException e) {
            throw new DAOException("Error al buscar categoría: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Categoria> findAll() {
        String sql = "SELECT * FROM categorias WHERE eliminado=false ORDER BY nombre";
        List<Categoria> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs, conn));
        } catch (SQLException e) {
            throw new DAOException("Error al listar categorías: " + e.getMessage(), e);
        }
        return list;
    }

    // recibe la conexión para reutilizarla en la segunda query
    private Categoria mapRow(ResultSet rs, Connection conn) throws SQLException {
        Categoria categoria = new Categoria(
                rs.getLong("id"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getBoolean("eliminado"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
        cargarProductos(categoria, conn);
        return categoria;
    }

    private void cargarProductos(Categoria categoria, Connection conn) throws SQLException {
        String sql = "SELECT * FROM productos WHERE categoria_id=? AND eliminado=false";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, categoria.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto(
                            rs.getLong("id"),
                            rs.getString("nombre"),
                            rs.getDouble("precio"),
                            rs.getString("descripcion"),
                            rs.getInt("stock"),
                            rs.getString("imagen"),
                            rs.getBoolean("disponible"),
                            rs.getBoolean("eliminado"),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            categoria
                    );
                    categoria.getListaProductos().add(p);
                }
            }
        }
    }
}
