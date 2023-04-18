package com.trip.service.implementation;

import com.trip.exception.UserAlreadyExistException;
import com.trip.model.User;
import com.trip.model.type.Role;
import com.trip.repository.UserRepository;
import com.trip.security.AuthenticationRequest;
import com.trip.security.AuthenticationResponse;
import com.trip.security.RegisterRequest;
import com.trip.service.AuthenticationService;
import com.trip.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImplementation implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest registerRequest) throws UserAlreadyExistException {
        checkIfUserExist(registerRequest.getUserEmail());
        User user = User.builder()
                .userFirstName(registerRequest.getUserFirstName())
                .userLastName(registerRequest.getUserLastName())
                .userEmail(registerRequest.getUserEmail())
                .userRole(Role.USER)
                .userPassword(passwordEncoder.encode(registerRequest.getUserPassword()))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();
        userRepository.save(user);
        String jwtToken = jwtService.generateJwtToken(user);
        return AuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUserEmail(),
                authenticationRequest.getUserPassword()
        ));
        User user = userRepository.findUserByUserEmail(authenticationRequest.getUserEmail())
                .orElseThrow();
        String jwtToken = jwtService.generateJwtToken(user);
        return AuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .build();
    }

    private void checkIfUserExist(final String userEmail) throws UserAlreadyExistException {
        final Optional<User> userFromDatabase = userRepository.findUserByUserEmail(userEmail);
        if (userFromDatabase.isPresent()) {
            throw new UserAlreadyExistException("User " + userEmail + " already exist in database");
        }
    }
}