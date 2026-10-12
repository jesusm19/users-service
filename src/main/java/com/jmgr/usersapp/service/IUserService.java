package com.jmgr.usersapp.service;

import java.util.List;
import com.jmgr.usersapp.models.UserDTO;
import com.jmgr.usersapp.models.UserUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.Optional;

public interface IUserService {

    List<UserDTO> findAll();

    Page<UserDTO> findAll(Pageable pageable);

    Optional<UserDTO> findById(Long id);

    UserDTO save(UserDTO user);

    boolean deleteById(Long id);

    UserDTO update(Long id, UserUpdateDTO userDTO);

}
