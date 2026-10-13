package com.jmgr.usersapp.service;
import com.jmgr.usersapp.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jmgr.usersapp.models.IUser;
import com.jmgr.usersapp.models.UserDTO;
import com.jmgr.usersapp.models.UserUpdateDTO;
import com.jmgr.usersapp.models.entity.Role;
import com.jmgr.usersapp.models.entity.User;
import com.jmgr.usersapp.repository.IUserRepository;

import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.jmgr.usersapp.models.mapper.IUserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IUserMapper userMapper;


    /**
     * Service implementation for managing users.
     */
    @Override
    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();
        log.info("Retrieved users: " + users.size());
        return userRepository.findAll()
                             .stream()
                             .map(userMapper::toUserDTO)
                             .toList();
    }

    @Override
    public Page<UserDTO> findAll(Pageable pageable) {
        // Fetch a page of users from the repository
        Page<User> usersPage = userRepository.findAll(pageable);
        // Map the entities to DTOs
        return usersPage.map(userMapper::toUserDTO);
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
        userEntity.setRoles(getRoles(user.isAdmin()));

        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
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
    public UserDTO update(Long id, UserUpdateDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return null;
        }
        User userEntity = optionalUser.get();
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setName(userDTO.getName());
        userEntity.setLastname(userDTO.getLastname());
        userEntity.setRoles(getRoles(userDTO.isAdmin()));

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

    /**
     * Retrieves the list of roles based on the administrative status of the user.
     * @param isAdmin indicates whether the user has administrative privileges
     * @return the list of roles assigned to the user   
     */
    private List<Role> getRoles(boolean isAdmin) {
        List<Role> roles = new ArrayList<>();
        Optional<Role> optionalRoles = roleRepository.findByName("ROLE_USER");
        optionalRoles.ifPresent(roles::add);

        if (isAdmin) {
            Optional<Role> optionalAdminRole = roleRepository.findByName("ROLE_ADMIN");
            optionalAdminRole.ifPresent(roles::add);
        }
        return roles;
    }
}
