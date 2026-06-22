package services;

import java.util.List;

import dao.CategoriaDAO;
import entities.Categoria;
import exception.*;

public class CategoriaService {

    private final CategoriaDAO dao;

    public CategoriaService(CategoriaDAO dao) { this.dao = dao; }

    public List<Categoria> listar() {
        return dao.findAll();
    }

    public Categoria buscarPorId(Long id) {
        Categoria c = dao.findById(id);
        if (c == null) throw new ValidationException("No existe una categoría activa con id: " + id);
        return c;
    }

    public void crear(String nombre, String descripcion) {
        validarNombre(nombre);
        try {
            dao.create(new Categoria(nombre.trim(), descripcion != null ? descripcion.trim() : ""));
        } catch (DAOException e) {
            if (e.getMessage().contains("Duplicate")) {
                throw new ValidationException("Ya existe una categoría con el nombre '" + nombre + "'.");
            }
            throw e;
        }
    }

    public void actualizar(Long id, String nombre, String descripcion) {
        Categoria c = buscarPorId(id);
        if (nombre != null && !nombre.isBlank())       c.setNombre(nombre.trim());
        if (descripcion != null && !descripcion.isBlank()) c.setDescripcion(descripcion.trim());
        try {
            dao.update(c);
        } catch (DAOException e) {
            if (e.getMessage().contains("Duplicate")) {
                throw new ValidationException("Ya existe una categoría con ese nombre.");
            }
            throw e;
        }
    }

    public void eliminar(Long id) {
        buscarPorId(id);
        dao.delete(id);
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank())
            throw new ValidationException("El nombre no puede estar vacío.");
    }
}
