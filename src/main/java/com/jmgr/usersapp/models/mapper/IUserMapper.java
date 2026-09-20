package com.jmgr.usersapp.models.mapper;
import org.mapstruct.Mapper;
import com.jmgr.usersapp.models.UserDTO;
import com.jmgr.usersapp.models.entity.User;

@Mapper(componentModel = "spring")
public interface IUserMapper {

    /**
     * Converts a UserDTO to a User entity.
     * @param userDTO the UserDTO to convert
     * @return the corresponding User entity
     */
    User toUser(UserDTO userDTO);

    /**
     * Converts a User entity to a UserDTO.
     * @param user the User entity to convert
     * @return the corresponding UserDTO
     */
    UserDTO toUserDTO(User user);
}
