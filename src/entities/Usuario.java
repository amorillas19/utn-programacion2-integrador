package entities;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import enums.Rol;

public class Usuario extends Base {
    private String nombre;
    private String apellido;
    private String mail;
    private String celular;
    private String password;
    private Rol rol;
    private List<Pedido> listaPedidos;

    public Usuario() {
        super();
    }

    public Usuario(String nombre, String apellido, String mail, String celular, String password, Rol rol) {
        super();
        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.celular = celular;
        this.password = password;
        this.rol = rol;
    }

    public Usuario(Long id, String nombre, String apellido, String mail,
            String celular, String password, Rol rol,
            boolean eliminado, LocalDateTime createdAt) {
        super(id, eliminado, createdAt);
        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.celular = celular;
        this.password = password;
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public void addPedido(Pedido pedido) {
        if (pedido != null) {
            pedido.setUsuario(this);
            listaPedidos.add(pedido);
            System.out.println("Pedido agregado exitosamente.");
        }
    }

    /* HABRIA QUE REVISAR ESTE MOSTRAR PRODUCTOS */
    public List<Pedido> mostrarPedidos() {
        return Collections.unmodifiableList(listaPedidos);
    }

    public void deletePedido(Pedido pedido) {
        Pedido aux = null;
        if (pedido != null) {
            for (Pedido ped : listaPedidos) {
                if (ped.equals(pedido)) {
                    aux = ped;
                    System.out.println("Pedido encontrado");
                }
            }
        }
        pedido.setUsuario(null);
        System.out.println("Pedido: " + pedido);
        System.out.println("Borrado exitosamente");
        listaPedidos.remove(aux);
    }

    @Override
    public String toString() {
        return "Usuario [nombre=" + nombre + ", apellido=" + apellido + ", mail=" + mail + ", celular=" + celular
                + ", password=" + password + ", rol=" + rol + "]";
    }

}
