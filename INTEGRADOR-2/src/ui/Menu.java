package ui;

import java.util.Scanner;

public class Menu {
    
    private Scanner scan;
    private int opcionMenu;

    public Menu (){
        this.scan = new Scanner(System.in);
    }

    public void mostrarMenu() {
        String menu = """
        ***FOOD STORE***
        == SISTEMA DE PEDIDOS ==
        
        1. Categorías
        2. Productos
        3. Usuarios
        4. Pedidos
        0. Salir

        Seleccione: """;
        
        while (true) {
            System.out.print(menu);
            opcionMenu = Integer.valueOf(scan.next());
            switch (opcionMenu) {
                case 1:
                    System.out.println("Categorias");
                    break;
                case 2:
                    System.out.println("Productos");
                    break;
                case 3:
                    System.out.println("Usuarios");
                    break;
                case 4:
                    System.out.println("Pedidos");
                    break;
                case 0:
                    System.out.println("Gracias por usar el sistema!");
                    break;
                default:
                    System.out.println("Opcion incorrecta, por favor, ingrese una valida");
                    break;
            }
        }

    }
}
