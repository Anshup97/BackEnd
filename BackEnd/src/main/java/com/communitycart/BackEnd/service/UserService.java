package com.communitycart.BackEnd.service;

import com.communitycart.BackEnd.dtos.AddressDTO;
import com.communitycart.BackEnd.dtos.CustomerDTO;
import com.communitycart.BackEnd.dtos.SellerDTO;
import com.communitycart.BackEnd.entity.*;
import com.communitycart.BackEnd.repository.CustomerRepository;
import com.communitycart.BackEnd.repository.SellerRepository;
import com.communitycart.BackEnd.repository.UsersRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private FIleStorage fIleStorage;

    @Autowired
    private ImageStorageService imageStorageService;
    public User getUser(String emailId){
        return usersRepository.findByEmailId(emailId);
    }

    public void createUser(User user){
        usersRepository.save(user);
    }

    public void deleteUser(String email){
        User user = usersRepository.findByEmailId(email);
        usersRepository.delete(user);
    }


    Address createAddress(AddressDTO addressDTO){
        Address address = new Address();
        address.setAddress1(addressDTO.getAddress1());
        address.setAddress2(addressDTO.getAddress2());
        address.setCity(addressDTO.getCity());
        address.setDistrict(addressDTO.getDistrict());
        address.setState(addressDTO.getState());
        address.setPinCode(addressDTO.getPinCode());
        address.setLatitude(addressDTO.getLatitude());
        address.setLongitude(addressDTO.getLongitude());
        return address;
    }

    public CustomerDTO addCustomer(CustomerDTO customer){
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        Customer customerEntity = new Customer();
        customerEntity.setName(customer.getName());
        customerEntity.setEmail(customer.getEmail());
        customerEntity.setContactPhoneNo(customer.getContactPhoneNo());
        customerEntity.setAddress(new ModelMapper().map(customer.getAddress(), Address.class));
        customerEntity.setCart(new Cart());
        createUser(new User(customer.getEmail(), encodedPassword, "BUYER"));
        return new ModelMapper().map(customerRepository.save(customerEntity), CustomerDTO.class);
    }

    public CustomerDTO updateCustomer(CustomerDTO customer){
        Customer customerEntity = customerRepository.findByEmail(customer.getEmail());
        if(customerEntity != null){
            customerEntity.setName(customer.getName());
            customerEntity.setEmail(customer.getEmail());
            customerEntity.setContactPhoneNo(customer.getContactPhoneNo());
            customerEntity.setAddress(new ModelMapper().map(customer.getAddress(), Address.class));
            return new ModelMapper().map(customerRepository.save(customerEntity), CustomerDTO.class);
        }
        return null;
    }


    public CustomerDTO deleteCustomer(String email){
        Customer customer = customerRepository.findByEmail(email);
        if(customer == null){
            return null;
        }
        deleteUser(customer.getEmail());
        CustomerDTO customerDTO = new ModelMapper().map(customer, CustomerDTO.class);
        customerRepository.delete(customer);
        return customerDTO;
    }

    public SellerDTO addSeller(SellerDTO seller) throws IOException {
        String encodedPassword = passwordEncoder.encode(seller.getPassword());
        Seller sellerEntity = new Seller();
        sellerEntity.setName(seller.getName());
        sellerEntity.setEmail(seller.getEmail());
        sellerEntity.setContactPhoneNo(seller.getContactPhoneNo());
        sellerEntity.setAadharNo(seller.getAadharNo());
        sellerEntity.setShopName(seller.getShopName());
        ModelMapper modelMapper = new ModelMapper();
        sellerEntity.setAddress(modelMapper.map(seller.getAddress(), Address.class));
        sellerEntity.setGstin(seller.getGstin());
        createUser(new User(seller.getEmail(), encodedPassword, "SELLER"));
        return new ModelMapper().map(sellerRepository.save(sellerEntity), SellerDTO.class);
    }

    public Seller deleteSeller(SellerDTO seller){
        deleteUser(seller.getEmail());
        return sellerRepository.deleteByEmail(seller.getEmail());
    }

    public Seller updateSeller(Seller seller){
        Seller seller1 = sellerRepository.findByEmail(seller.getEmail());
        seller1.setName(seller.getName());
        seller1.setContactPhoneNo(seller.getContactPhoneNo());
        seller1.setAadharNo(seller.getAadharNo());
        seller1.setShopName(seller.getShopName());
        seller1.setAddress(seller.getAddress());
        seller1.setGstin(seller.getGstin());
        seller1.setProducts(seller.getProducts());
        return sellerRepository.save(seller1);
    }


    public CustomerDTO getCustomer(String email){
        Customer customer = customerRepository.findByEmail(email);
        if(customer != null){
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setCustomerId(customer.getCustomerId());
            customerDTO.setName(customer.getName());
            customerDTO.setEmail(customer.getEmail());
            customerDTO.setContactPhoneNo(customer.getContactPhoneNo());
            customerDTO.setAddress(new ModelMapper().map(customer.getAddress(), AddressDTO.class));
            customerDTO.setCustomerImageUrl(customer.getCustomerImageUrl());
            return customerDTO;
        }
        return null;
    }

    public SellerDTO getSeller(Long id){

         return new ModelMapper().map(sellerRepository.findById(id), SellerDTO.class);
    }


    public SellerDTO uploadPhoto(String email, MultipartFile profilePhoto) throws Exception {
        Seller seller = sellerRepository.findByEmail(email);
        if(seller != null){
            String url = fIleStorage.saveSellerPhoto(profilePhoto, seller.getSellerId());
            url = "http://172.17.84.65:8080/images/shop/" + url;
            seller.setShopPhotoUrl(url);
            return new ModelMapper().map(sellerRepository.save(seller), SellerDTO.class);
        }
        return null;

    }

    public CustomerDTO uploadCustomerPhoto(String email, MultipartFile profilePhoto) throws Exception {
        Customer customer = customerRepository.findByEmail(email);
        if(customer != null){
            String url = fIleStorage.saveCustomerPhoto(profilePhoto, customer.getCustomerId());
            url = "http://172.17.84.65:8080/images/customers/" + url;
            customer.setCustomerImageUrl(url);
            return new ModelMapper().map(customerRepository.save(customer), CustomerDTO.class);
        }
        return null;
    }
}
