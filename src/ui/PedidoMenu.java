package ui;

import java.util.List;
import java.util.Scanner;

import entities.*;
import enums.*;
import exception.*;
import services.*;

public class PedidoMenu {

    private final Scanner scanner;
    private final PedidoService service;
    private final UsuarioService usuarioService;
    private final ProductoService productoService;

    public PedidoMenu(Scanner scanner, PedidoService service,
                      UsuarioService usuarioService, ProductoService productoService) {
        this.scanner         = scanner;
        this.service         = service;
        this.usuarioService  = usuarioService;
        this.productoService = productoService;
    }

    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n--- PEDIDOS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear pedido");
            System.out.println("3. Ver detalle de pedido");
            System.out.println("4. Actualizar estado / forma de pago");
            System.out.println("5. Eliminar");
            System.out.println("0. Volver");
            opcion = leerEntero("Seleccione: ");
            switch (opcion) {
                case 1 -> listar();
                case 2 -> crear();
                case 3 -> verDetalle();
                case 4 -> actualizar();
                case 5 -> eliminar();
                case 0 -> {}
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private void listar() {
        System.out.println("\n¿Filtrar por usuario? (S/N): ");
        List<Pedido> lista;
        if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
            long uid = leerLong("ID de usuario: ");
            try { lista = service.listarPorUsuario(uid); }
            catch (ValidationException e) { System.out.println("Error: " + e.getMessage()); return; }
        } else {
            lista = service.listar();
        }
        if (lista.isEmpty()) { System.out.println("No hay pedidos."); return; }
        System.out.println("\nID   Fecha       Estado       Pago            Total        Usuario");
        System.out.println("─".repeat(80));
        lista.forEach(System.out::println);
    }

    private void crear() {
        System.out.println("\n-- Crear pedido --");
        System.out.println("Usuarios disponibles:");
        usuarioService.listar().forEach(u -> System.out.println("  " + u));

        long usuarioId = leerLong("ID de usuario: ");
        FormaPago formaPago = leerFormaPago();

        Pedido pedido;
        try {
            pedido = service.iniciarPedido(usuarioId, formaPago);
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        System.out.println("\nProductos disponibles:");
        productoService.listar().forEach(p -> System.out.println("  " + p));

        boolean agregando = true;
        while (agregando) {
            long prodId = leerLong("\nID de producto (0 para terminar): ");
            if (prodId == 0) break;
            int cantidad = leerEntero("Cantidad: ");
            try {
                service.agregarDetalle(pedido, prodId, cantidad);
                DetallePedido ultimo = pedido.getListaDetallesPedidos().get(pedido.getListaDetallesPedidos().size() - 1);
                System.out.printf("  Subtotal: $%.2f%n", ultimo.getSubtotal());
            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.print("¿Agregar otro producto? (S/N): ");
            agregando = scanner.nextLine().trim().equalsIgnoreCase("S");
        }

        if (pedido.getListaDetallesPedidos().isEmpty()) {
            System.out.println("Pedido cancelado (sin detalles).");
            return;
        }

        pedido.calcularTotal();
        System.out.println("\n=== RESUMEN ===");
        pedido.getListaDetallesPedidos().forEach(System.out::println);
        System.out.printf("Total: $%.2f%n", pedido.getTotal());
        System.out.print("¿Confirmar pedido? (S/N): ");
        if (!scanner.nextLine().trim().equalsIgnoreCase("S")) {
            System.out.println("Pedido descartado.");
            return;
        }

        try {
            service.confirmarPedido(pedido);
            System.out.println("✓ Pedido creado con ID: " + pedido.getId());
        } catch (DAOException e) {
            System.out.println("Error al guardar pedido: " + e.getMessage());
        }
    }

    private void verDetalle() {
        long id = leerLong("ID de pedido: ");
        try {
            Pedido p = service.buscarPorId(id);
            System.out.println("\n" + p);
            System.out.println("Detalles:");
            if (p.getListaDetallesPedidos().isEmpty()) System.out.println("  (sin detalles cargados)");
            else p.getListaDetallesPedidos().forEach(System.out::println);
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void actualizar() {
        System.out.println("\n-- Actualizar pedido --");
        long id = leerLong("ID de pedido: ");
        try {
            Pedido actual = service.buscarPorId(id);
            System.out.println("Actual: " + actual);
            Estado    estado = leerEstado();
            FormaPago pago   = leerFormaPago();
            service.actualizarEstadoYPago(id, estado, pago);
            System.out.println("✓ Pedido actualizado.");
        } catch (ValidationException | DAOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminar() {
        System.out.println("\n-- Eliminar pedido --");
        long id = leerLong("ID de pedido: ");
        try {
            Pedido p = service.buscarPorId(id);
            System.out.print("¿Eliminar pedido #" + p.getId() + "? (S/N): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("S")) {
                System.out.println("Cancelado.");
                return;
            }
            service.eliminar(id);
            System.out.println("✓ Pedido eliminado.");
        } catch (ValidationException | DAOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private FormaPago leerFormaPago() {
        FormaPago[] valores = FormaPago.values();
        while (true) {
            System.out.println("Forma de pago:");
            for (int i = 0; i < valores.length; i++)
                System.out.println("  " + (i + 1) + ". " + valores[i]);
            int op = leerEntero("Seleccione: ");
            if (op >= 1 && op <= valores.length) return valores[op - 1];
            System.out.println("Opción inválida.");
        }
    }

    private Estado leerEstado() {
        Estado[] valores = Estado.values();
        while (true) {
            System.out.println("Estado:");
            for (int i = 0; i < valores.length; i++)
                System.out.println("  " + (i + 1) + ". " + valores[i]);
            int op = leerEntero("Seleccione: ");
            if (op >= 1 && op <= valores.length) return valores[op - 1];
            System.out.println("Opción inválida.");
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
}
