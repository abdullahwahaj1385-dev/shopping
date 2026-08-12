package com.example.shopping.Controller;

import com.example.shopping.DTO.OrderDTO;
import com.example.shopping.model.Order;
import com.example.shopping.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 1. Elenco di tutti gli ordini
    @GetMapping("/all")
    public List<Order> getAllOrders() {
        return orderService.findAll();
    }

    // 2. Creazione di un nuovo ordine
    @PostMapping("/createNewOrder")
    public String createOrder(@RequestBody OrderDTO orderDto) {
        try {
            Order nuovoOrdine = new Order();

            /*
             nuovoOrdine.setOrderNumber(orderDto.getOrderNumber());
             nuovoOrdine.setTotalAmount(orderDto.getTotalAmount());
             nuovoOrdine.setOrderDate(orderDto.getOrderDate());

             */

            orderService.saveOrder(nuovoOrdine);
            return "ordine inserito";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Errore di inserimento ordine", e);
        }
    }

    // La modifica
    @PutMapping("/{id}")
    public String updateOrder(@PathVariable Long id, @RequestBody OrderDTO orderDto) {
        try {
            Order existingOrder = orderService.findById(id);
            if (existingOrder == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ordine non trovato");
            }

            /*
             existingOrder.setTotalAmount(orderDto.getTotalAmount());
             existingOrder.setStatus(orderDto.getStatus());

             */

            orderService.saveOrder(existingOrder);
            return "ordine modificato";

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Errore di modifica ordine", e);
        }
    }

    // Delete
    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return "ordine cancellato";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Errore di cancellazione ordine", e);
        }
    }

    //  Restituisce
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        Order order = orderService.findById(id);
        if (order == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Ordine non trovato");
        }
        return order;
    }
}
