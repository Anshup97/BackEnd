package com.communitycart.BackEnd.service;

import com.communitycart.BackEnd.entity.Cart;
import com.communitycart.BackEnd.entity.Order;
import com.communitycart.BackEnd.entity.Product;
import com.communitycart.BackEnd.repository.OrderRepository;
import com.communitycart.BackEnd.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    public Order getOrderDetail(Long orderId) {
        Optional<Order> order = this.orderRepository.findById(orderId);
        return order.isPresent() ? order.get() : null;
    }

    public double getCartAmount(List<Cart> cartList) {

        double totalCartAmount = 0d;
        double singleCartAmount = 0d;
        int availableQuantity = 0;

        for (Cart cart : cartList) {

            Long productId = cart.getProductId();
            Optional<Product> product = productRepository.findById(productId);
            if (product.isPresent()) {
                Product product1 = product.get();
                if (product1.getProductQuantity() < cart.getQuantity()) {
                    singleCartAmount = product1.getProductPrice() * product1.getProductQuantity();
                    cart.setQuantity(product1.getProductQuantity());
                } else {
                    singleCartAmount = cart.getQuantity() * product1.getProductPrice();
                    availableQuantity = product1.getProductQuantity() - cart.getQuantity();
                }
                totalCartAmount = totalCartAmount + singleCartAmount;
                product1.setProductQuantity(availableQuantity);
                availableQuantity=0;
                cart.setProductId(product1.getProductId());
                productRepository.save(product1);
            }
        }
        return totalCartAmount;
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }
}
