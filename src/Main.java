import entities.*;
import enums.*;
import ui.*;

public class Main {
    public static void main(String[] args) throws Exception {
        /*Categoria c1 = new Categoria("Lacteos", "vaca y vegetales");
        Categoria c2 = new Categoria("Panaderia", "panes y facturas");
        
        Producto pro1 = new Producto("Leche", 399.99, "Almendra", 15, "lechealmendra.png", true);
        Producto pro2 = new Producto("Pan", 250.99, "Casero", 10, "pan.png", true);
        System.out.println("******* MOSTRAR PRODUCTOS *********");
        System.out.println(c1);
        System.out.println(pro1);
        System.out.println("********** AGREGAR PRODUCTOS **********");
        c1.addProducto(pro1);
        c2.addProducto(pro2);
        System.out.println(c1);
        System.out.println(pro1);
        System.out.println("********* BORRAR PRODUCTOS ************");
        c1.deleteProducto(pro1);
        System.out.println(c1);
        System.out.println(pro1);*/

        Menu menu = new Menu();
        menu.mostrarMenu();


        
    }
}
