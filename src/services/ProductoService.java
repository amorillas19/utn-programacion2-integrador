package services;

import java.util.List;

import dao.CategoriaDAO;
import dao.ProductoDAO;
import entities.Categoria;
import entities.Producto;
import exception.*;

public class ProductoService {

    private final ProductoDAO  dao;
    private final CategoriaDAO categoriaDAO;

    public ProductoService(ProductoDAO dao, CategoriaDAO categoriaDAO) {
        this.dao          = dao;
        this.categoriaDAO = categoriaDAO;
    }

    public List<Producto> listar() { return dao.findAll(); }

    public Producto buscarPorId(Long id) {
        Producto p = dao.findById(id);
        if (p == null) throw new ValidationException("No existe un producto activo con id: " + id);
        return p;
    }

    public void crear(String nombre, Double precio, String descripcion,
                      int stock, String imagen, boolean disponible, Long categoriaId) {
        validarCampos(nombre, precio, stock);
        Categoria cat = validarCategoria(categoriaId);
        dao.create(new Producto(nombre.trim(), precio, descripcion, stock, imagen, disponible, cat));
    }

    public void actualizar(Long id, String nombre, Double precio, String descripcion,
                           int stock, String imagen, boolean disponible, Long categoriaId) {
        Producto p = buscarPorId(id);
        validarCampos(nombre, precio, stock);
        Categoria cat = validarCategoria(categoriaId);
        p.setNombre(nombre.trim());
        p.setPrecio(precio);
        p.setDescripcion(descripcion);
        p.setStock(stock);
        p.setImagen(imagen);
        p.setDisponible(disponible);
        p.setCategoria(cat);
        dao.update(p);
    }

    public void eliminar(Long id) {
        buscarPorId(id);
        dao.delete(id);
    }

    private void validarCampos(String nombre, Double precio, int stock) {
        if (nombre == null || nombre.isBlank())
            throw new ValidationException("El nombre no puede estar vacío.");
        if (precio == null || precio < 0)
            throw new ValidationException("El precio debe ser >= 0.");
        if (stock < 0)
            throw new ValidationException("El stock debe ser >= 0.");
    }

    private Categoria validarCategoria(Long categoriaId) {
        Categoria cat = categoriaDAO.findById(categoriaId);
        if (cat == null)
            throw new ValidationException("No existe una categoría activa con id: " + categoriaId);
        return cat;
    }
}
