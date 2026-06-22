package ui;

import java.util.Scanner;

import services.*;

public class Menu {

    private final Scanner        scanner;
    private final CategoriaMenu  categoriaMenu;
    private final ProductoMenu   productoMenu;
    private final UsuarioMenu    usuarioMenu;
    private final PedidoMenu     pedidoMenu;

    public Menu(Scanner scanner,
                CategoriaService  categoriaService,
                ProductoService   productoService,
                UsuarioService    usuarioService,
                PedidoService     pedidoService) {
        this.scanner       = scanner;
        this.categoriaMenu = new CategoriaMenu(scanner, categoriaService);
        this.productoMenu  = new ProductoMenu(scanner, productoService, categoriaService);
        this.usuarioMenu   = new UsuarioMenu(scanner, usuarioService);
        this.pedidoMenu    = new PedidoMenu(scanner, pedidoService, usuarioService, productoService);
    }

    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n=== SISTEMA DE PEDIDOS (FOOD STORE) ===");
            System.out.println("1. Categorías");
            System.out.println("2. Productos");
            System.out.println("3. Usuarios");
            System.out.println("4. Pedidos");
            System.out.println("0. Salir");
            opcion = leerOpcion();
            switch (opcion) {
                case 1 -> categoriaMenu.mostrar();
                case 2 -> productoMenu.mostrar();
                case 3 -> usuarioMenu.mostrar();
                case 4 -> pedidoMenu.mostrar();
                case 0 -> System.out.println("Hasta luego.");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private int leerOpcion() {
        System.out.print("Seleccione: ");
        try { return Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }
}

