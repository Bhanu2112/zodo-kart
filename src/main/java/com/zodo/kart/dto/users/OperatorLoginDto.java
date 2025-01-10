package com.zodo.kart.dto.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author : Bhanu prasad
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OperatorLoginDto {

    private String email;
    private String password;
}
