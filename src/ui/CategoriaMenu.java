package ui;

import java.util.List;
import java.util.Scanner;

import entities.Categoria;
import exception.*;
import services.CategoriaService;

public class CategoriaMenu {

    private final Scanner scanner;
    private final CategoriaService service;

    public CategoriaMenu(Scanner scanner, CategoriaService service) {
        this.scanner = scanner;
        this.service = service;
    }

    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n--- CATEGORÍAS ---");
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
        List<Categoria> lista = service.listar();
        if (lista.isEmpty()) {
            System.out.println("No hay categorías cargadas.");
            return;
        }
        System.out.println("\nID   Nombre               Descripción");
        System.out.println("─".repeat(50));
        lista.forEach(System.out::println);
    }

    private void crear() {
        System.out.println("\n-- Crear categoría --");
        String nombre      = leerTexto("Nombre: ");
        String descripcion = leerTexto("Descripción: ");
        try {
            service.crear(nombre, descripcion);
            System.out.println("✓ Categoría creada.");
        } catch (ValidationException | DAOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void editar() {
        System.out.println("\n-- Editar categoría --");
        long id = leerLong("ID de categoría: ");
        try {
            Categoria actual = service.buscarPorId(id);
            System.out.println("Actual: " + actual);
            String nombre      = leerTextoOpcional("Nuevo nombre (Enter para mantener): ");
            String descripcion = leerTextoOpcional("Nueva descripción (Enter para mantener): ");
            service.actualizar(id,
                    nombre.isBlank()      ? actual.getNombre()      : nombre,
                    descripcion.isBlank() ? actual.getDescripcion() : descripcion);
            System.out.println("✓ Categoría actualizada.");
        } catch (ValidationException | DAOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminar() {
        System.out.println("\n-- Eliminar categoría --");
        long id = leerLong("ID de categoría: ");
        try {
            Categoria c = service.buscarPorId(id);
            System.out.println("¿Eliminar '" + c.getNombre() + "'? (S/N): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("S")) {
                System.out.println("Operación cancelada.");
                return;
            }
            service.eliminar(id);
            System.out.println("✓ Categoría eliminada.");
        } catch (ValidationException | DAOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ── input helpers ────────────────────────────────────────────────────

    private int leerEntero(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Ingrese un número válido."); }
        }
    }

    private long leerLong(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Long.parseLong(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Ingrese un número válido."); }
        }
    }

    private String leerTexto(String prompt) {
        while (true) {
            System.out.print(prompt);
            String v = scanner.nextLine().trim();
            if (!v.isBlank()) return v;
            System.out.println("El campo no puede estar vacío.");
        }
    }

    private String leerTextoOpcional(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}

