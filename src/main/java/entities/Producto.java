package entities;

import java.time.LocalDateTime;

public class Producto extends Base {
    private String nombre;
    private double precio;
    private String descripcion;
    private int stock;
    private String imagen;
    private boolean disponible;
    private Categoria categoria;

    public Producto() {
        super();
    }

    
    public Producto(String nombre, double precio, String descripcion, 
        int stock, String imagen, boolean disponible, Categoria categoria) {
        super();
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.stock = stock;
        this.imagen = imagen;
        this.disponible = disponible;
        this.categoria = categoria;
    }

    public Producto(Long id, String nombre, Double precio, String descripcion,
            int stock, String imagen, boolean disponible,
            boolean eliminado, LocalDateTime createdAt, Categoria categoria) {
        super(id, eliminado, createdAt);
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.stock = stock;
        this.imagen = imagen;
        this.disponible = disponible;
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        if (this.categoria == null) {
            return "Producto [id=" + getId() + ", nombre=" + nombre + ", precio=" + precio + ", descripcion=" + descripcion + ", stock="
                    + stock
                    + ", imagen=" + imagen + ", disponible=" + disponible + ", categoria= null" + "]";
        } else {
            return "Producto [id=" + getId() + ", nombre=" + nombre + ", precio=" + precio + ", descripcion=" + descripcion + ", stock="
                    + stock
                    + ", imagen=" + imagen + ", disponible=" + disponible + ", categoria=" + categoria.getNombre()
                    + "]";
        }

    }

    @Override
    public int hashCode() {
        return nombre.hashCode() * 31 + categoria.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Producto other = (Producto) obj;
        return nombre.equals(other.nombre) && descripcion.equals(other.descripcion);
    }

}
