package com.jmgr.usersapp.models;
import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder 
public class UserDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * The unique identifier for the user.
     */
    @NotNull (message = "Id cannot be null")
    private Long id; 
    /**
     * The first name of the user.
     */
    @NotNull(message = "Name cannot be null")
    private String name;
    /**
     * The last name of the user.
     */
    @NotNull(message = "Lastname cannot be null")
    private String lastname;
    /**
     * The email address of the user.
     */
    @NotNull(message = "Email cannot be null")
    private String email;
    /**
     * The username of the user.
     */
    @NotNull(message = "Username cannot be null")
    private String username;
    /**
     * The password of the user.
     */
    @NotNull(message = "Password cannot be null")
    private String password;
}