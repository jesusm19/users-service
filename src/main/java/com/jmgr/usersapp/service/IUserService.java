package com.jmgr.usersapp.service;

import java.util.List;
import com.jmgr.usersapp.models.UserDTO;

import jakarta.validation.Valid;

import java.util.Optional;

public interface IUserService {

    List<UserDTO> findAll();

    Optional<UserDTO> findById(Long id);

    UserDTO save(UserDTO user);

    boolean deleteById(Long id);

    UserDTO update(Long id, UserDTO userDTO);

}
