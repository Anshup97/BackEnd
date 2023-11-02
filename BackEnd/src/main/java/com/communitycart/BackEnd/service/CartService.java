package com.communitycart.BackEnd.service;

import com.communitycart.BackEnd.dtos.CartDTO;
import com.communitycart.BackEnd.dtos.CartItemDTO;
import com.communitycart.BackEnd.entity.Cart;
import com.communitycart.BackEnd.entity.CartItem;
import com.communitycart.BackEnd.entity.Customer;
import com.communitycart.BackEnd.entity.Product;
import com.communitycart.BackEnd.repository.CartItemRepository;
import com.communitycart.BackEnd.repository.CartRepository;
import com.communitycart.BackEnd.repository.CustomerRepository;
import jakarta.annotation.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public ModelMapper mapper(){
        return new ModelMapper();
    }
    public CartDTO getCart(Long customerId) {
        Customer customer = customerRepository.findByCustomerId(customerId);
        if(customer == null){
            return null;
        }
        Cart cart = customer.getCart();
        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(cart.getCartId());
        cartDTO.setCustomerId(customerId);
        cartDTO.setItems(cart.getItems().stream().map(i -> new ModelMapper().map(i, CartItemDTO.class))
                .collect(Collectors.toList()));
        return cartDTO;
    }

    public CartDTO addToCart(Long customerId, CartItemDTO item) {
        Customer customer = customerRepository.findByCustomerId(customerId);
        if(customer == null){
            return null;
        }
        Cart cart = customerRepository.findByCustomerId(customerId).getCart();
        List<CartItem> items = cart.getItems();
        if(items == null){
            items = new ArrayList<>();
        }
        long count = items.stream()
                .filter(x -> x.getProduct().getProductId().equals(item.getProduct().getProductId()))
                .count();
        if(count == 0){
            CartItem cartItem = new CartItem();
            cartItem.setCartId(cart.getCartId());
            cartItem.setProduct(new ModelMapper().map(item.getProduct(), Product.class));
            cartItem.setQuantity(item.getQuantity());
            items.add(cartItem);
            cart.setItems(items);
        } else {
            for(CartItem x: items){
                if(x.getProduct().getProductId().equals(item.getProduct().getProductId())){
                    x.setQuantity(x.getQuantity() + item.getQuantity());
                    break;
                }
            }

        }
        cartRepository.save(cart);
        customer.setCart(cart);
        return mapper().map(customerRepository.save(customer).getCart(), CartDTO.class);
    }

    public CartDTO updateCart(Long customerId, CartItemDTO item) {
        Customer customer = customerRepository.findByCustomerId(customerId);
        if(customer == null){
            return null;
        }
        Cart cart = customer.getCart();
        List<CartItem> cartItems = cart.getItems();
        for(CartItem x: cartItems){
            if(x.getProduct().getProductId().equals(item.getProduct().getProductId())){
                x.setQuantity(item.getQuantity());
                break;
            }
        }
        cartRepository.save(cart);
        customer.setCart(cart);
        return mapper().map(customerRepository.save(customer).getCart(), CartDTO.class);
    }

    public CartDTO deleteFromCart(Long customerId, Long productId) {
        Customer customer = customerRepository.findByCustomerId(customerId);
        if(customer == null){
            return null;
        }
        Cart cart = customer.getCart();
        List<CartItem> cartItems = cart.getItems();
        CartItem temp = null;
        if(productId == null){
            customer.getCart().setItems(new ArrayList<>());
            for(CartItem cartItem: cartItems){
                cartItemRepository.delete(cartItem);
            }
        } else {
            for(CartItem x: cartItems){
                if(x.getProduct().getProductId().equals(productId)){
                    temp = x;
                    break;
                }
            }
            if(temp != null){
                cartItems.remove(temp);
                cartItemRepository.delete(temp);
            }
        }
        cartRepository.save(cart);
        customer.setCart(cart);
        return mapper().map(customerRepository.save(customer).getCart(), CartDTO.class);
    }
}
