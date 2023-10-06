package com.example.entity.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class EmailRegisterVO {
    @Email
    String email;
    @Length(max=6,min=6)
    String code;
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,16}$")
            @Length(min =1,max=10)
    String username;
    @Length(min=6,max=20)
    String password;
}
