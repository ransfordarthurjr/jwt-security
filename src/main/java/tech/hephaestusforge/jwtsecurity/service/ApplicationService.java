package tech.hephaestusforge.jwtsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.hephaestusforge.jwtsecurity.model.datasource.Role;
import tech.hephaestusforge.jwtsecurity.model.datasource.User;
import tech.hephaestusforge.jwtsecurity.model.jwt.AuthenticateRequestModel;
import tech.hephaestusforge.jwtsecurity.model.jwt.AuthenticationResponseModel;
import tech.hephaestusforge.jwtsecurity.model.jwt.RegisterRequestModel;
import tech.hephaestusforge.jwtsecurity.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponseModel register(RegisterRequestModel request) {
        var user = User.builder()
                .email(request.getEmail())
                .password(this.passwordEncoder.encode(request.getPassword()))
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .role(Role.USER)
                .build();
        this.userRepository.save(user);

        var jwt = this.jwtService.generateJwt(user);
        return AuthenticationResponseModel.builder()
                .accessToken(jwt)
                .build();
    }

    public AuthenticationResponseModel authenticate(AuthenticateRequestModel request) {
        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = this.userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwt = this.jwtService.generateJwt(user);
        return AuthenticationResponseModel.builder()
                .accessToken(jwt)
                .build();
    }
}
