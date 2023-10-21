package com.communitycart.BackEnd.config;

import com.communitycart.BackEnd.entity.User;
import com.communitycart.BackEnd.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserInfoUserDetailsService implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = usersRepository.findByEmailId(username);
        if(user == null){
            return (UserDetails) new UsernameNotFoundException("User not found" + username);
        }
        return new UserInfoUserDetails(user);
    }
}
