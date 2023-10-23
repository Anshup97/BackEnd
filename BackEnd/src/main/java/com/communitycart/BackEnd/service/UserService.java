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
    private ImageStorageService imageStorageService;
    public User getUser(String emailId){
        return usersRepository.findByEmailId(emailId);
    }

    public void createUser(User user){
        usersRepository.save(user);
    }

    public void deleteUser(String email){
        usersRepository.deleteByEmailId(email);
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

    public Customer addCustomer(CustomerDTO customer){
        createUser(new User(customer.getEmailId(), passwordEncoder.encode(customer.getPassword()), "CUSTOMER"));
        Customer customerEntity = new Customer();
        customerEntity.setName(customer.getName());
        customerEntity.setEmailId(customer.getEmailId());
        customerEntity.setPhoneNo(customer.getPhoneNo());
        customerEntity.setAlternatePhoneNo(customer.getAlternatePhoneNo());
        List<Address> addressEntityList = new ArrayList<>();
        for(AddressDTO add: customer.getAddress()){
            Address ae = createAddress(add);
            addressEntityList.add(ae);
        }
        customerEntity.setAddresses(addressEntityList);
        return customerRepository.save(customerEntity);
    }

    public Customer updateCustomer(CustomerDTO customer){
        Customer customerEntity = new Customer();
        customerEntity.setName(customer.getName());
        customerEntity.setEmailId(customer.getEmailId());
        customerEntity.setPhoneNo(customer.getPhoneNo());
        customerEntity.setAlternatePhoneNo(customer.getAlternatePhoneNo());
        List<Address> addressEntityList = new ArrayList<>();
        for(AddressDTO add: customer.getAddress()){
            Address ae = createAddress(add);
            addressEntityList.add(ae);
        }
        customerEntity.setAddresses(addressEntityList);
        return customerRepository.save(customerEntity);
    }


    public Customer deleteCustomer(CustomerDTO customer){
        deleteUser(customer.getEmailId());
        return customerRepository.deleteByEmailId(customer.getEmailId());
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


    public Customer getCustomer(String email){
        return customerRepository.findByEmailId(email);

    }

    public SellerDTO getSeller(String email){

         return new ModelMapper().map(sellerRepository.findByEmail(email), SellerDTO.class);
    }


    public SellerDTO uploadPhoto(String email, MultipartFile profilePhoto) throws IOException {
        Seller seller = sellerRepository.findByEmail(email);
        ImageData imageData = imageStorageService.uploadImage(profilePhoto);
        seller.setProfilePhotoId(imageData.getId());
        return new ModelMapper().map(sellerRepository.save(seller), SellerDTO.class);
    }
}
