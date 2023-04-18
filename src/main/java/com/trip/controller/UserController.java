package com.trip.controller;

import com.trip.service.JwtService;
import com.trip.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.NoSuchObjectException;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @GetMapping(value = "/find-user")
    public ResponseEntity<?> getUserDtoByUserEmail(@RequestHeader("Authorization") String jwtToken) {
        try {
            String userEmail = jwtService.extractUsername(jwtToken.substring(7));
            return new ResponseEntity<>(userService.getUserDtoByUserEmail(userEmail), HttpStatus.OK);
        } catch (NoSuchObjectException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}