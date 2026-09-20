package com.jmgr.usersapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jmgr.usersapp.models.UserDTO;
import com.jmgr.usersapp.repository.IUserRepository;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;

import com.jmgr.usersapp.models.mapper.IUserMapper;

import java.util.List;
import java.util.Optional;

@Service
@Log
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IUserMapper userMapper;


    /**
     * Service implementation for managing users.
     */
    @Override
    public List<UserDTO> findAll() {
        List<com.jmgr.usersapp.models.entity.User> users = userRepository.findAll();
        log.info("Retrieved users: " + users.size());
        return userRepository.findAll()
                             .stream()
                             .map(userMapper::toUserDTO)
                             .toList();
    }

    /**
     * Retrieves a user by their unique identifier.
     * @param id the unique identifier of the user to retrieve
     * @return an Optional containing the user if found, or empty if not found
     */
    @Override
    public Optional<UserDTO> findById(Long id) {
        return userRepository.findById(id)
                             .map(userMapper::toUserDTO);
    }

    /**
     * Saves a user.
     * @param user the user to save
     * @return the saved user
     */
    @Override
    @Transactional 
    public UserDTO save(UserDTO user) {
        var userEntity = userMapper.toUser(user);
        return userMapper.toUserDTO(userRepository.save(userEntity));
    }

    /**
     * Updates an existing user.
     * @param id the unique identifier of the user to update
     * @param userDTO the user data to update
     * @return the updated user
     */
    @Override
    @Transactional
    public UserDTO update(Long id, UserDTO userDTO) {
        var userEntity = userMapper.toUser(userDTO);
        userEntity.setId(id);
        return userMapper.toUserDTO(userRepository.save(userEntity));
    }

    /**
     * Deletes a user by their unique identifier.
     * @param id the unique identifier of the user to delete
     * @return true if the user was deleted, false if the user was not found
     */
    @Override
    @Transactional
    public boolean deleteById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
