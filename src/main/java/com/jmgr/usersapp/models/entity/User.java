package com.jmgr.usersapp.models.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@Entity 
@Table(name = "users")
public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * The unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 
    /**
     * The first name of the user.
     */
    private String name;
    /**
     * The last name of the user.
     */
    private String lastname;
    /**
     * The email address of the user.
     */
    private String email;
    /**
     * The username of the user.
     */
    private String username;
    /**
     * The password of the user.
     */
    private String password;

}
