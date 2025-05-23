package com.marketplus.market_auht_sv.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthLoginReqDto(@NotBlank(message = "puedes poner un nombre!") String username,
                               @NotBlank(message = "puedes poner una contraseña") String password){}
