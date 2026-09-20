package com.jmgr.usersapp.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jmgr.usersapp.models.entity.User;

public interface IUserRepository extends JpaRepository<User, Long> {

}
