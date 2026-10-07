package com.jmgr.usersapp.models;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class RoleDTO implements Serializable {
    
    /**
     * The unique identifier for the role.
     */
    private Long id;
    /**
     * The name of the role.
     */
    private String name;

}
