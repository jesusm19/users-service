package com.jmgr.usersapp.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.jmgr.usersapp.models.entity.User;
import com.jmgr.usersapp.repository.IUserRepository;

@Service 
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired 
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()){
            throw new UsernameNotFoundException(String.format("El usuario %s no existe", username));
        }

        User userEntity = optionalUser.orElseThrow();
        List<GrantedAuthority> authorities = userEntity.getRoles()
            .stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
            username, userEntity.getPassword(), true, true, true, true, authorities);
    }

}
