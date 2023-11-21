package com.communitycart.BackEnd.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * StripeResponse DTO
 * sessionId -> Stripe payment sessionId.
 * url -> Payment page to be sent to frontend so that the customer can make payment.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StripeResponse {

    private String sessionId;
    private String url;
}
