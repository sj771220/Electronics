package com.example.shop.config;

import com.example.shop.dto.UserVo;
import com.example.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthProvider implements AuthenticationProvider {

    @Autowired
    UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userid = (String) authentication.getPrincipal(); // 로그인 창에 입력한 email
        String password = (String) authentication.getCredentials(); // 로그인 창에 입력한 password

        UsernamePasswordAuthenticationToken token;
        UserVo userVo = userService.getUserByUserid(userid);

        if (userVo != null && password.equals(userVo.getPassword())) {
            // 사용자의 권한을 설정
            List<GrantedAuthority> roles = new ArrayList<>();
            // 예: 사용자에게 ROLE_USER 권한을 부여
            roles.add(new SimpleGrantedAuthority("ROLE_USER"));

            token = new UsernamePasswordAuthenticationToken(userVo.getId(), null, roles);
            return token;
        }

        throw new BadCredentialsException("No such user or wrong password.");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}

