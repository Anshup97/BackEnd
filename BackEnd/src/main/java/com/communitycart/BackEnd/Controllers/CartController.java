package com.communitycart.BackEnd.Controllers;

import com.communitycart.BackEnd.dtos.OrderDTO;
import com.communitycart.BackEnd.dtos.OrderResponseDTO;
import com.communitycart.BackEnd.entity.Customer;
import com.communitycart.BackEnd.entity.Order;
import com.communitycart.BackEnd.entity.Product;
import com.communitycart.BackEnd.service.OrderService;
import com.communitycart.BackEnd.service.ProductService;
import com.communitycart.BackEnd.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(CartController.class);

    @GetMapping(value = "/getAllProducts")
    public ResponseEntity<List<Product>> getAllProducts() {

        List<Product> productList = productService.getAllProducts();

        return ResponseEntity.ok(productList);
    }

    @GetMapping(value = "/getOrder/{orderId}")
    public ResponseEntity<Order> getOrderDetails(@PathVariable Long orderId) {

        Order order = orderService.getOrderDetail(orderId);
        return ResponseEntity.ok(order);
    }


    @PostMapping("/placeOrder")
    public ResponseEntity<OrderResponseDTO> placeOrder(@RequestBody OrderDTO orderDTO) {
        logger.info("Request Payload " + orderDTO.toString());
        double amount = orderService.getCartAmount(orderDTO.getOrderItems());

        Order order = new Order(orderDTO.getOrderId(), orderDTO.getCustomerId(), orderDTO.getShopId(), orderDTO.getOrderItems(),
                orderDTO.getShippingAddress(), orderDTO.getPaymentMethod(), orderDTO.getItemsPrice(), orderDTO.getShippingPrice(), orderDTO.getTaxPrice(),
        orderDTO.getTotalPrice(), orderDTO.isPaid(), orderDTO.getPaidAt(), orderDTO.getStatus(), orderDTO.getDeliveredAt() );
        order = orderService.saveOrder(order);
        logger.info("Order processed successfully..");
        OrderResponseDTO responseOrderDTO = new OrderResponseDTO();
        responseOrderDTO.setAmount(amount);
        responseOrderDTO.setInvoiceNumber(new Random().nextLong());
        responseOrderDTO.setOrderId(order.getOrderId());

        return ResponseEntity.ok(responseOrderDTO);
    }

}
