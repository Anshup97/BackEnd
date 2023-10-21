package com.communitycart.BackEnd.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Builder
public class User {
    @Id
    private String emailId;
    private String password;
    private String role;
}
