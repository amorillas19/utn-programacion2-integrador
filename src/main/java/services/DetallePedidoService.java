package main.java.services;

import java.util.List;

import main.java.dao.DetallePedidoDAO;
import main.java.entities.DetallePedido;

public class DetallePedidoService {

    private final DetallePedidoDAO dao;

    public DetallePedidoService(DetallePedidoDAO dao) { this.dao = dao; }

    public List<DetallePedido> listarPorPedido(Long pedidoId) {
        return dao.findByPedidoId(pedidoId);
    }
}