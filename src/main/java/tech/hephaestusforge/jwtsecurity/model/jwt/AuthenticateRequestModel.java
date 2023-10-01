package tech.hephaestusforge.jwtsecurity.model.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthenticateRequestModel {
    private String email;
    private String password;
}
