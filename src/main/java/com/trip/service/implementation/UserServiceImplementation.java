package com.trip.service.implementation;

import com.trip.dto.UserDto;
import com.trip.model.User;
import com.trip.repository.UserRepository;
import com.trip.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.rmi.NoSuchObjectException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto getUserDtoByUserEmail(String userEmail) throws NoSuchObjectException {
        Optional<User> user = userRepository.findUserByUserEmail(userEmail);
        if (user.isPresent()) {
            return convertToUserDto(user.get());
        } else {
            throw new NoSuchObjectException("Can't find " + userEmail + " user");
        }
    }

    private UserDto convertToUserDto(User user) {
        return UserDto.builder()
                .userFirstName(user.getUserFirstName())
                .userLastName(user.getUserLastName())
                .userEmail(user.getUserEmail())
                .userRole(user.getUserRole())
                .userPassword(user.getUserPassword())
                .accountNonExpired(user.isCredentialsNonExpired())
                .accountNonLocked(user.isAccountNonLocked())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .enabled(user.isEnabled())
                .build();
    }
}