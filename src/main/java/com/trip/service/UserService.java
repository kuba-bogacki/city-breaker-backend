package com.trip.service;

import com.trip.dto.UserDto;

import java.rmi.NoSuchObjectException;

public interface UserService {
    UserDto getUserDtoByUserEmail(String userEmail) throws NoSuchObjectException;
}
