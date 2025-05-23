package com.marketplus.market_auht_sv.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username", "message", "jwtToken", "status"})
public record AuthLoginResDto(String username,
                              String message,
                              String jwtToken,
                              boolean status) {
}
