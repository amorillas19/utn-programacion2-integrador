package ui;

import java.util.List;
import java.util.Scanner;

import entities.*;
import enums.*;
import exception.*;
import services.*;

public class UsuarioMenu {

    private final Scanner        scanner;
    private final UsuarioService service;

    public UsuarioMenu(Scanner scanner, UsuarioService service) {
        this.scanner = scanner;
        this.service = service;
    }

    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n--- USUARIOS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            opcion = leerEntero("Seleccione: ");
            switch (opcion) {
                case 1 -> listar();
                case 2 -> crear();
                case 3 -> editar();
                case 4 -> eliminar();
                case 0 -> {}
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private void listar() {
        List<Usuario> lista = service.listar();
        if (lista.isEmpty()) { System.out.println("No hay usuarios cargados."); return; }
        System.out.println("\nID   Nombre          Apellido        Mail                           Rol");
        System.out.println("─".repeat(75));
        lista.forEach(System.out::println);
    }

    private void crear() {
        System.out.println("\n-- Crear usuario --");
        try {
            String nombre     = leerTexto("Nombre: ");
            String apellido   = leerTexto("Apellido: ");
            String mail       = leerTexto("Mail: ");
            String celular    = leerTextoOpcional("Celular: ");
            String contrasena = leerTexto("Contraseña: ");
            Rol    rol        = leerRol();
            service.crear(nombre, apellido, mail, celular, contrasena, rol);
            System.out.println("Usuario creado.");
        } catch (ValidationException | DAOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void editar() {
        System.out.println("\n-- Editar usuario --");
        long id = leerLong("ID de usuario: ");
        try {
            Usuario actual = service.buscarPorId(id);
            System.out.println("Actual: " + actual);
            String nombre     = leerTextoOpcional("Nuevo nombre (Enter = mantener): ");
            String apellido   = leerTextoOpcional("Nuevo apellido (Enter = mantener): ");
            String mail       = leerTextoOpcional("Nuevo mail (Enter = mantener): ");
            String celular    = leerTextoOpcional("Nuevo celular (Enter = mantener): ");
            String contrasena = leerTextoOpcional("Nueva contraseña (Enter = mantener): ");
            String rolStr     = leerTextoOpcional("Nuevo rol 1=ADMIN 2=USUARIO (Enter = mantener): ");

            String nom  = nombre.isBlank()     ? actual.getNombre()    : nombre;
            String ape  = apellido.isBlank()   ? actual.getApellido()  : apellido;
            String ml   = mail.isBlank()       ? actual.getMail()      : mail;
            String cel  = celular.isBlank()    ? actual.getCelular()   : celular;
            String pwd  = contrasena.isBlank() ? actual.getPassword(): contrasena;
            Rol    rol  = rolStr.isBlank()     ? actual.getRol()       : (rolStr.equals("1") ? Rol.ADMIN : Rol.USUARIO);

            service.actualizar(id, nom, ape, ml, cel, pwd, rol);
            System.out.println("Usuario actualizado.");
        } catch (ValidationException | DAOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminar() {
        System.out.println("\n-- Eliminar usuario --");
        long id = leerLong("ID de usuario: ");
        try {
            Usuario u = service.buscarPorId(id);
            System.out.print("¿Eliminar a " + u.getNombre() + " " + u.getApellido() + "? (S/N): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("S")) {
                System.out.println("Cancelado.");
                return;
            }
            service.eliminar(id);
            System.out.println("Usuario eliminado.");
        } catch (ValidationException | DAOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private Rol leerRol() {
        while (true) {
            System.out.print("Rol (1=ADMIN, 2=USUARIO): ");
            String v = scanner.nextLine().trim();
            if (v.equals("1"))
                return Rol.ADMIN;
            if (v.equals("2"))
                return Rol.USUARIO;
            System.out.println("Opción inválida.");
        }
    }

    // ── input helpers ────────────────────────────────────────────────────

    private int leerEntero(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Ingrese un número valido."); }
        }
    }

    private long leerLong(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Long.parseLong(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Ingrese un número valido."); }
        }
    }

    private String leerTexto(String prompt) {
        while (true) {
            System.out.print(prompt);
            String v = scanner.nextLine().trim();
            if (!v.isBlank()) return v;
            System.out.println("El campo no puede estar vacio.");
        }
    }

    private String leerTextoOpcional(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
