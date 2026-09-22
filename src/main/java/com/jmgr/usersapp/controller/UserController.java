package com.jmgr.usersapp.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.jmgr.usersapp.models.UserDTO;
import java.util.List;
import org.springframework.http.ResponseEntity;

import com.jmgr.usersapp.service.IUserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves all users.
     * @return a ResponseEntity containing the list of all users
     */
    @GetMapping("/findAll")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    /**
     * Retrieves a user by their unique identifier.
     * @param id the unique identifier of the user to retrieve
     * @return a ResponseEntity containing the user if found, or a 404 status if not found
     */
    @GetMapping("/findById/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                          .map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Saves a new user.
     * @param userDTO the user data to save
     * @return a ResponseEntity containing the saved user
     */
    @PostMapping("/save")
    public ResponseEntity<UserDTO> saveUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO savedUser = userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    /**
     * Updates an existing user.
     * @param userDTO the user data to update
     * @return a ResponseEntity containing the updated user
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.update(id, userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedUser);
    }

    /**
     * Deletes a user by their unique identifier.
     * @param id the unique identifier of the user to delete
     * @return a ResponseEntity indicating the result of the delete operation
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.deleteById(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
