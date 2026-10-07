package com.jmgr.usersapp.models;
import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder 
@AllArgsConstructor 
public class UserDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * The unique identifier for the user.
     */
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
    @Email(message = "Email should be valid")
    private String email;
    /**
     * The username of the user.
     */
    @NotNull(message = "Username cannot be null")
    @Size(min = 4, max = 12, message = "Username must be between 4 and 12 characters")
    private String username;
    /**
     * The password of the user.
     */
    @NotNull(message = "Password cannot be null")
    private String password;
    /**
     * The roles assigned to the user.
     */
    private List<RoleDTO> roles;
}