package services;

import java.util.List;

import dao.DetallePedidoDAO;
import entities.DetallePedido;

public class DetallePedidoService {

    private final DetallePedidoDAO dao;

    public DetallePedidoService(DetallePedidoDAO dao) { this.dao = dao; }

    public List<DetallePedido> listarPorPedido(Long pedidoId) {
        return dao.findByPedidoId(pedidoId);
    }
}