package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import config.DatabaseConnection;
import entities.Usuario;
import enums.Rol;
import exception.DAOException;

public class UsuarioDAO implements GenericDAO<Usuario> {

    @Override
    public void create(Usuario u) {
        String sql = "INSERT INTO usuarios (nombre, apellido, mail, celular, contrasena, rol, eliminado, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, false, NOW())";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getMail());
            ps.setString(4, u.getCelular());
            ps.setString(5, u.getPassword());
            ps.setString(6, u.getRol().name());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) u.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new DAOException("Error al crear usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Usuario u) {
        String sql = "UPDATE usuarios SET nombre=?, apellido=?, mail=?, celular=?, contrasena=?, rol=? " +
                     "WHERE id=? AND eliminado=false";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getMail());
            ps.setString(4, u.getCelular());
            ps.setString(5, u.getPassword());
            ps.setString(6, u.getRol().name());
            ps.setLong(7, u.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al actualizar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "UPDATE usuarios SET eliminado=true WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al eliminar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public Usuario findById(Long id) {
        String sql = "SELECT * FROM usuarios WHERE id=? AND eliminado=false";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DAOException("Error al buscar usuario: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Usuario> findAll() {
        String sql = "SELECT * FROM usuarios WHERE eliminado=false ORDER BY apellido, nombre";
        List<Usuario> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new DAOException("Error al listar usuarios: " + e.getMessage(), e);
        }
        return list;
    }

    private Usuario mapRow(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getLong("id"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("mail"),
                rs.getString("celular"),
                rs.getString("contrasena"),
                Rol.valueOf(rs.getString("rol")),
                rs.getBoolean("eliminado"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}

