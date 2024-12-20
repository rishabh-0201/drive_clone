package com.org.googledrive.security;

import com.org.googledrive.Service.UserService;
import com.org.googledrive.models.User;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserService userService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if(header!=null && header.startsWith("Bearer ")){
            String token = header.substring(7); // get token after bearer

            try{

                // validate the token
                Claims claims = jwtUtil.validateToken(token);
                if(claims != null){
                    String email = claims.getSubject(); // get email(subject)

                    // fetch the UserDetails based on the email
                    Optional<User> userOptional = userService.findUserByEmail(email);

                    if(userOptional.isPresent()){

                        User user = userOptional.get();

                        // If the user is found, create an authentication token

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());

                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        // set the authentication in the security contextholder
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        // continue with the request
        filterChain.doFilter(request,response);
    }
}
