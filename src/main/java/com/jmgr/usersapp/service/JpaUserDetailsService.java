package com.jmgr.usersapp.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.jmgr.usersapp.models.entity.User;
import com.jmgr.usersapp.repository.IUserRepository;

import lombok.extern.slf4j.Slf4j;


@Service 
@Slf4j
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired 
    private IUserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Entra JpaUserDetailsService.loadUserByUsername: {}", username);
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()){
            throw new UsernameNotFoundException(String.format("El usuario %s no existe", username));
        }

        User userEntity = optionalUser.orElseThrow();
        List<GrantedAuthority> authorities = userEntity.getRoles()
            .stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());
        log.info("Sale JpaUserDetailsService.loadUserByUsername: authorities={}", authorities);
        return new org.springframework.security.core.userdetails.User(
            username, userEntity.getPassword(), true, true, true, true, authorities);
    }

}
