package com.communitycart.BackEnd.service;

import com.communitycart.BackEnd.dtos.AddressDTO;
import com.communitycart.BackEnd.dtos.CustomerDTO;
import com.communitycart.BackEnd.dtos.SellerDTO;
import com.communitycart.BackEnd.entity.Address;
import com.communitycart.BackEnd.entity.Customer;
import com.communitycart.BackEnd.entity.Seller;
import com.communitycart.BackEnd.entity.User;
import com.communitycart.BackEnd.repository.CustomerRepository;
import com.communitycart.BackEnd.repository.SellerRepository;
import com.communitycart.BackEnd.repository.UsersRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        createUser(new User(customer.getEmailId(), passwordEncoder.encode(customer.getPassword()), "Customer"));
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

    public Seller addSeller(SellerDTO seller){
        createUser(new User(seller.getEmail(), passwordEncoder.encode(seller.getPassword()), "Seller"));
        Seller sellerEntity = new Seller();
        sellerEntity.setName(seller.getName());
        sellerEntity.setEmail(seller.getEmail());
        sellerEntity.setContactPhoneNo(seller.getContactPhoneNo());
        sellerEntity.setAlternatePhoneNo(seller.getAlternatePhoneNo());
        sellerEntity.setUpiPhoneNumber(seller.getShop().getUpiPhoneNumber());
        sellerEntity.setAadharNo(seller.getAadharNo());
        sellerEntity.setRegNo(seller.getShop().getRegNo());
        sellerEntity.setQrCodeLink(seller.getShop().getQrCodeLink());
        sellerEntity.setGstin(seller.getShop().getGstin());
        sellerEntity.setShopName(seller.getShop().getShopName());
        Address address = createAddress(seller.getShop().getAddress());
        sellerEntity.setAddress(address);
        return sellerRepository.save(sellerEntity);
    }

    public Seller deleteSeller(SellerDTO seller){
        deleteUser(seller.getEmail());
        return sellerRepository.deleteByEmail(seller.getEmail());
    }



    public Customer getCustomer(String email){
        return customerRepository.findByEmailId(email);

    }

    public Seller getSeller(String email){
        return sellerRepository.findByEmail(email);
    }


}
