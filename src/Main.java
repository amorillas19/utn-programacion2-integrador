import java.util.Scanner;

import dao.*;
import services.*;
import ui.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // DAOs
        CategoriaDAO     categoriaDAO     = new CategoriaDAO();
        ProductoDAO      productoDAO      = new ProductoDAO();
        UsuarioDAO       usuarioDAO       = new UsuarioDAO();
        DetallePedidoDAO detallePedidoDAO = new DetallePedidoDAO();
        PedidoDAO        pedidoDAO        = new PedidoDAO(detallePedidoDAO);

        // Services
        CategoriaService     categoriaService     = new CategoriaService(categoriaDAO);
        ProductoService      productoService      = new ProductoService(productoDAO, categoriaDAO);
        UsuarioService       usuarioService       = new UsuarioService(usuarioDAO);
        DetallePedidoService detallePedidoService = new DetallePedidoService(detallePedidoDAO);
        PedidoService        pedidoService        = new PedidoService(pedidoDAO, usuarioDAO, productoDAO);

        // Menu
        new Menu(scanner, categoriaService, productoService, usuarioService, pedidoService).mostrar();

        scanner.close();
    }
}
