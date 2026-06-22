package ui;

import java.util.List;
import java.util.Scanner;

import entities.*;
import exception.*;
import services.*;

public class ProductoMenu {

    private final Scanner         scanner;
    private final ProductoService  service;
    private final CategoriaService categoriaService;

    public ProductoMenu(Scanner scanner, ProductoService service, CategoriaService categoriaService) {
        this.scanner          = scanner;
        this.service          = service;
        this.categoriaService = categoriaService;
    }

    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n--- PRODUCTOS ---");
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
        List<Producto> lista = service.listar();
        if (lista.isEmpty()) { System.out.println("No hay productos cargados."); return; }
        System.out.println("\nID   Nombre               Precio       Stock  Categoría");
        System.out.println("─".repeat(65));
        lista.forEach(System.out::println);
    }

    private void crear() {
        System.out.println("\n-- Crear producto --");
        mostrarCategorias();
        try {
            String  nombre      = leerTexto("Nombre: ");
            double  precio      = leerDecimal("Precio: ");
            String  descripcion = leerTextoOpcional("Descripción: ");
            int     stock       = leerEntero("Stock: ");
            String  imagen      = leerTextoOpcional("Imagen (URL/ruta): ");
            boolean disponible  = leerSiNo("¿Disponible?");
            long    catId       = leerLong("ID de categoría: ");
            service.crear(nombre, precio, descripcion, stock, imagen, disponible, catId);
            System.out.println("✓ Producto creado.");
        } catch (ValidationException | DAOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void editar() {
        System.out.println("\n-- Editar producto --");
        long id = leerLong("ID de producto: ");
        try {
            Producto actual = service.buscarPorId(id);
            System.out.println("Actual: " + actual);
            mostrarCategorias();
            String  nombre      = leerTextoOpcional("Nuevo nombre (Enter = mantener): ");
            String  precioStr   = leerTextoOpcional("Nuevo precio (Enter = mantener): ");
            String  descripcion = leerTextoOpcional("Nueva descripción (Enter = mantener): ");
            String  stockStr    = leerTextoOpcional("Nuevo stock (Enter = mantener): ");
            String  imagen      = leerTextoOpcional("Nueva imagen (Enter = mantener): ");
            String  dispStr     = leerTextoOpcional("¿Disponible? S/N (Enter = mantener): ");
            String  catStr      = leerTextoOpcional("Nuevo ID categoría (Enter = mantener): ");

            String  nom  = nombre.isBlank()      ? actual.getNombre()          : nombre;
            double  prec = precioStr.isBlank()   ? actual.getPrecio()          : Double.parseDouble(precioStr);
            String  desc = descripcion.isBlank()  ? actual.getDescripcion()    : descripcion;
            int     stk  = stockStr.isBlank()    ? actual.getStock()           : Integer.parseInt(stockStr);
            String  img  = imagen.isBlank()      ? actual.getImagen()          : imagen;
            boolean disp = dispStr.isBlank()     ? actual.isDisponible()       : dispStr.equalsIgnoreCase("S");
            long    cat  = catStr.isBlank()      ? actual.getCategoria().getId(): Long.parseLong(catStr);

            service.actualizar(id, nom, prec, desc, stk, img, disp, cat);
            System.out.println("✓ Producto actualizado.");
        } catch (ValidationException | DAOException | NumberFormatException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminar() {
        System.out.println("\n-- Eliminar producto --");
        long id = leerLong("ID de producto: ");
        try {
            Producto p = service.buscarPorId(id);
            System.out.print("¿Eliminar '" + p.getNombre() + "'? (S/N): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("S")) {
                System.out.println("Cancelado.");
                return;
            }
            service.eliminar(id);
            System.out.println("✓ Producto eliminado.");
        } catch (ValidationException | DAOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void mostrarCategorias() {
        System.out.println("Categorías disponibles:");
        categoriaService.listar().forEach(c -> System.out.println("  " + c));
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

    private double leerDecimal(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Double.parseDouble(scanner.nextLine().trim()); }
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

    private boolean leerSiNo(String prompt) {
        System.out.print(prompt + " (S/N): ");
        return scanner.nextLine().trim().equalsIgnoreCase("S");
    }
}
