package com.communitycart.BackEnd.service;

import com.communitycart.BackEnd.entity.User;
import com.communitycart.BackEnd.repository.CustomerRepository;
import com.communitycart.BackEnd.repository.SellerRepository;
import com.communitycart.BackEnd.repository.UsersRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Used for JWT authentication.
 */
@Component
@Service
public class JWTService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Generate JWT token for user after user is authenticated.
     * Add user role, seller Id or customer Id as claims.
     * @param emailId
     * @return
     */
    public String generateToken(String emailId){
        Map<String, Object> claims = new HashMap<>();
        User user = usersRepository.findByEmailId(emailId);
        if(user != null){
            claims.put("role", user.getRole());
            if(user.getRole().equals("SELLER")){
                claims.put("sellerId", sellerRepository.findByEmail(user.getEmailId()).getSellerId());
            } else {
                claims.put("customerId", customerRepository.findCustomerByEmail(
                        user.getEmailId()).getCustomerId());
            }
        }
        return createToken(claims, emailId);
    }

    /**
     * Create JWT token.
     * @param claims
     * @param emailId
     * @return
     */
    private String createToken(Map<String, Object> claims, String emailId) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(emailId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*30*100))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Uses 64-bit hex key to generate token.
     * @return
     */
    private Key getSignKey() {
        String secret = "52afc3599e302060ff8d44d717742476949066694e0c11049ff1b345e727d262";
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    //Extracts username from payload of the token.
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //Extract token expiration date of the JWT token.
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //Extract claims of the JWT token.
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //Extract all claims of the JWT token.
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //Check if JWT token is expired.
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //Check validity of the token.
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
