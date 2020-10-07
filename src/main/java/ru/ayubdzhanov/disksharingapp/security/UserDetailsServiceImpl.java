package ru.ayubdzhanov.disksharingapp.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ayubdzhanov.disksharingapp.domain.Credential;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Credential credential = credentialRepository.findByUsername(username);
//
//        if(credential == null) throw new UsernameNotFoundException(username + " was not found");

        UserBuilder user = User
                .withUsername(username)
                .password("credential.getPassword()")
                .roles("credential.getRole()");

        return user.build();
    }
}
