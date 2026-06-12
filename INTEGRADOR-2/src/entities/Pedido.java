package entities;

import java.time.LocalDate;
import enums.*;

public class Pedido {
    private LocalDate fecha;
    private Estado estado;
    private Double total;
    private FormaPago formaPago;
}
