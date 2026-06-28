package main.java.services;

import java.util.List;
import main.java.dao.UsuarioDAO;
import main.java.entities.Usuario;
import main.java.enums.Rol;
import main.java.exception.*;

public class UsuarioService {

    private final UsuarioDAO dao;

    public UsuarioService(UsuarioDAO dao) { this.dao = dao; }

    public List<Usuario> listar() { return dao.findAll(); }

    public Usuario buscarPorId(Long id) {
        Usuario u = dao.findById(id);
        if (u == null) throw new ValidationException("No existe un usuario activo con id: " + id);
        return u;
    }

    public void crear(String nombre, String apellido, String mail,
                      String celular, String contrasena, Rol rol) {
        validarCampos(nombre, apellido, mail, contrasena);
        try {
            dao.create(new Usuario(nombre.trim(), apellido.trim(), mail.trim(),
                                   celular, contrasena, rol));
        } catch (DAOException e) {
            if (e.getMessage().contains("Duplicate")) {
                throw new ValidationException("Ya existe un usuario con el mail '" + mail + "'.");
            }
            throw e;
        }
    }

    public void actualizar(Long id, String nombre, String apellido, String mail,
                           String celular, String contrasena, Rol rol) {
        Usuario u = buscarPorId(id);
        validarCampos(nombre, apellido, mail, contrasena);
        u.setNombre(nombre.trim());
        u.setApellido(apellido.trim());
        u.setMail(mail.trim());
        u.setCelular(celular);
        u.setPassword(contrasena);
        u.setRol(rol);
        try {
            dao.update(u);
        } catch (DAOException e) {
            if (e.getMessage().contains("Duplicate")) {
                throw new ValidationException("Ya existe un usuario con ese mail.");
            }
            throw e;
        }
    }

    public void eliminar(Long id) {
        buscarPorId(id);
        dao.delete(id);
    }

    private void validarCampos(String nombre, String apellido, String mail, String password) {
        if (nombre == null || nombre.isBlank())     throw new ValidationException("El nombre no puede estar vacío.");
        if (apellido == null || apellido.isBlank()) throw new ValidationException("El apellido no puede estar vacío.");
        if (mail == null || mail.isBlank())         throw new ValidationException("El mail no puede estar vacío.");
        if (password == null || password.isBlank()) throw new ValidationException("La contraseña no puede estar vacía.");
    }
}
