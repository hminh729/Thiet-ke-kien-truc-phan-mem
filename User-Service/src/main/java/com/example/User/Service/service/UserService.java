package com.example.User.Service.service;

import com.example.User.Service.dto.UserDTO;

public interface UserService {
    UserDTO login(String username, String password);
    UserDTO register(UserDTO userDTO);
}
