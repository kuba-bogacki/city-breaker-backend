package com.trip.service;

import com.trip.exception.UserAlreadyExistException;
import com.trip.security.AuthenticationRequest;
import com.trip.security.AuthenticationResponse;
import com.trip.security.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest registerRequest) throws UserAlreadyExistException;
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
}
