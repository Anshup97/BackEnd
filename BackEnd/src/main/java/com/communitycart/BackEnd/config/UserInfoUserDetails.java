package com.communitycart.BackEnd.config;

import com.communitycart.BackEnd.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Used for authenticating users.
 */
public class UserInfoUserDetails implements UserDetails {

    private String email;
    private String password;
    private List<GrantedAuthority> authorities;

    /**
     * Get user email, password and user role.
     * User password will be returned in encoded form.
     * @param userInfo
     */
    public UserInfoUserDetails(User userInfo){
        email=userInfo.getEmailId();
        password=userInfo.getPassword();
        authorities= Arrays.stream(userInfo.getRole().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    //Get user roles.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    //Get user password.
    @Override
    public String getPassword() {
        return password;
    }

    //Get username
    @Override
    public String getUsername() {
        return email;
    }

    //Get if user account expired.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //Get if user account is locked.
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //Get if user credentials expired.
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    ////Get if user account is enabled.
    @Override
    public boolean isEnabled() {
        return true;
    }
}
