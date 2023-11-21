package com.communitycart.BackEnd.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DTO for authentication request from frontend to backend.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthRequest {

    private String email;
    private String password;
    private boolean sso;
}
