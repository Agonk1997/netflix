package com.example.netflix.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    @Email(message = "Emaili duhet te jete i sakte!")
    @NotBlank(message = "Emaili nuk mund te jete i zbrazet!")
    @NotNull(message = "Emaili nuk mund te jete null!")
    @Size(message = "Emaili duhet te jete me shkruajt 3 deri ne 50 karaktere!", min = 3, max = 50)
    private String email;

    @NotBlank(message = "Passwordi nuk mund te jete i zbrazet!")
    @NotNull(message = "Passwordi nuk mund te jete null!")
    @Size(message = "Passwordi duhet te jete me shkruajt 3 deri ne 50 karaktere!", min = 6, max = 50)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
            message = "Passwordi duhet te permbaje se paku nje shkronje te madhe, nje shkronje te vogel dhe nje numer!")
    private String password;




}

