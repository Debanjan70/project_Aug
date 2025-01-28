package com.app.config;

import com.app.entity.User;
import com.app.repository.UserRepository;
import com.app.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private UserRepository userRepository;
    private JWTService jwtService;
    public JWTFilter(JWTService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        System.out.println(token);
        if(token != null && token.startsWith("Bearer ")){
            String jwtToken = token.substring(8,token.length()-1);
            System.out.println(jwtToken);
            String userName = jwtService.getUserName(jwtToken);
            System.out.println(userName);
            Optional<User> opUser = userRepository.findByUsername(userName);
            if(opUser.isPresent()){
                User user = opUser.get();
                UsernamePasswordAuthenticationToken AuthenticationToken = new UsernamePasswordAuthenticationToken
                        (user,
                null,
                                Collections.singleton(new SimpleGrantedAuthority(user.getRole()))
                        );
                AuthenticationToken.setDetails(new WebAuthenticationDetails(request));

                SecurityContextHolder.getContext().setAuthentication(AuthenticationToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
