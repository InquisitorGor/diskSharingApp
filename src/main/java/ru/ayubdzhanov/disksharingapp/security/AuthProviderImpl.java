package ru.ayubdzhanov.disksharingapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.ayubdzhanov.disksharingapp.dao.spring.data.CredentialRepository;
import ru.ayubdzhanov.disksharingapp.domain.Credential;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthProviderImpl implements AuthenticationProvider {

    private CredentialRepository credentialRepository;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public AuthProviderImpl(CredentialRepository credentialRepository, PasswordEncoder passwordEncoder) {
        this.credentialRepository = credentialRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        Credential user = credentialRepository.findByUsername(name);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        String password = authentication.getCredentials().toString();

        if (!passwordEncoder.matches(password, user.getPassword())){
            throw new BadCredentialsException("Bad credentials.");
        }


        List<GrantedAuthority> authorityList = new ArrayList<>();
        return new UsernamePasswordAuthenticationToken(user, null, authorityList);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
