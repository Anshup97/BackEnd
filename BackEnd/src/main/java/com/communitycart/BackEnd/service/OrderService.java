package com.communitycart.BackEnd.service;

import com.communitycart.BackEnd.dtos.AddressDTO;
import com.communitycart.BackEnd.dtos.OrderDTO;
import com.communitycart.BackEnd.dtos.OrderItemDTO;
import com.communitycart.BackEnd.entity.*;
import com.communitycart.BackEnd.repository.CustomerRepository;
import com.communitycart.BackEnd.repository.OrderRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Value("${STRIPE_URL}")
    private String url;

    @Value("${STRIPE_SECRET_KEY}")
    private String secret;

    public ModelMapper mapper(){ return new ModelMapper(); }

    public OrderDTO customMap(Order order){
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getOrderId());
        dto.setCustomerId(order.getCustomerId());
        dto.setSellerId(order.getSellerId());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setShippingCharges(order.getShippingCharges());
        dto.setPaid(order.isPaid());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setDeliveryDate(order.getDeliveryDate());
        dto.setDeliveredAt(order.getDeliveredAt());
        dto.setStatus(order.getStatus());
        dto.setItems(order.getItems().stream()
                .map(i -> new ModelMapper().map(i, OrderItemDTO.class))
                .collect(Collectors.toList()));
        dto.setShippingAddress(mapper().map(order.getShippingAddress(), AddressDTO.class));
        return dto;
    }

    public OrderDTO checkOut(Long customerId, String paymentMethod) {
        Customer customer = customerRepository.findByCustomerId(customerId);
        if(customer == null){
            return null;
        }
        Cart cart = customer.getCart();
        List<CartItem> items = cart.getItems();
        if(items==null || items.isEmpty()){
            return null;
        }
        Map<Long, List<CartItem>> orderMap = new HashMap<>();
        for(CartItem cartItem: items){
            Long sellerId = cartItem.getProduct().getSellerId();
            if(!orderMap.containsKey(sellerId)){
                List<CartItem> cartItems = new ArrayList<>();
                cartItems.add(cartItem);
                orderMap.put(sellerId, cartItems);
            } else {
                List<CartItem> cartItems = orderMap.get(sellerId);
                cartItems.add(cartItem);
                orderMap.put(sellerId, cartItems);
            }
        }
        for(Map.Entry<Long, List<CartItem>> en: orderMap.entrySet()){
            Long sellerId = en.getKey();
            List<CartItem> cartItems = en.getValue();
            List<OrderItem> orderItems = new ArrayList<>();
            double totalPrice = 0D;
            for(CartItem item: cartItems){
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(item.getProduct());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setItemPrice(item.getProduct().getProductPrice());
                double tot = orderItem.getItemPrice() * item.getQuantity();
                totalPrice += tot;
                orderItem.setTotalPrice(tot);
                orderItems.add(orderItem);
            }
            Order order = new Order();
            order.setCustomerId(customerId);
            order.setSellerId(sellerId);
            order.setPaymentMethod(paymentMethod);
            order.setTotalPrice(totalPrice);
            if(paymentMethod.equalsIgnoreCase("COD")){
                order.setShippingCharges(10D);
                order.setPaid(false);
            } else {
                order.setShippingCharges(5D);
                order.setPaid(true);
            }
            order.setCreatedAt(new Date());
            order.setStatus("Placed");
            order.setItems(orderItems);
            order.setShippingAddress(customer.getAddress());
            Order savedOrder = orderRepository.save(order);
            cartService.deleteFromCart(customerId, null);
            return customMap(savedOrder);
        }

        return null;
    }

    public List<OrderDTO> getOrders(Long customerId, Long sellerId){
        List<OrderDTO> orderDTOS = new ArrayList<>();
        if(sellerId == null){
            List<Order> orders = orderRepository.findByCustomerId(customerId);
            for(Order o: orders){
                orderDTOS.add(customMap(o));
            }
        } else {
            List<Order> orders = orderRepository.findBySellerId(sellerId);
            for(Order o: orders){
                orderDTOS.add(customMap(o));
            }

        }
        return orderDTOS;
    }

    public Session createSession(Long customerId) throws StripeException {
        String successUrl = url + "payment/success";
        String failedUrl = url + "payment/failed";

        Stripe.apiKey = secret;
        List<SessionCreateParams.LineItem> sessionItemsList = new ArrayList<>();
        Customer customer = customerRepository.findByCustomerId(customerId);
        if(customer == null){
            return null;
        }
        Cart cart = customer.getCart();
        List<CartItem> cartItems = cart.getItems();
        if(cartItems==null || cartItems.isEmpty()){
            return null;
        }
        for(CartItem ci: cartItems){
            sessionItemsList.add(createSessionLineItem(ci));
        }
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCancelUrl(failedUrl)
                .setSuccessUrl(successUrl)
                .addAllLineItem(sessionItemsList)
                .build();
        return Session.create(params);
    }

    private SessionCreateParams.LineItem createSessionLineItem(CartItem ci) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(ci.getQuantity())
                .setPriceData(createPriceData(ci))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(CartItem ci) {
        double price = ci.getProduct().getProductPrice();
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("inr")
                .setUnitAmount((long)price)
                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(ci.getProduct().getProductName())
                        .setDescription(ci.getProduct().getProductDescription())
                        .build())
                .build();
    }
}
