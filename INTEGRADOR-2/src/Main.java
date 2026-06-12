import entities.*;
import enums.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Categoria c1 = new Categoria("Lacteos", "vaca y vegetales");
        Categoria c2 = new Categoria("Panaderia", "panes y facturas");
        
        /*Producto pro1 = new Producto("Pan", 250.99, "Casero", 10, "pan.png", true);
        Producto pro2 = new Producto("Leche", 399.99, "Almendra", 15, "lechealmendra.png", true);*/
        System.out.println(c1);
        
    }
}
