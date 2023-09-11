package com.example.bookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserRegistrationRequestDto {
    @NotNull
    @Email
    private String email;
    @NotNull
    @Length(min = 8, max = 25)
    private String password;
    @NotNull
    @Length(min = 8, max = 25)
    private String repeatedPassword;
    @NotNull
    @Length(min = 3, max = 255)
    private String firstName;
    @NotNull
    @Length(min = 5, max = 255)
    private String lastName;
    @NotNull
    private String shippingAddress;
}
