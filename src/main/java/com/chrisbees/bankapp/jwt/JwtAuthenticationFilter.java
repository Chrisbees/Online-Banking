package com.chrisbees.bankapp.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);


    private final JwtService jwtService;
   @Override
   protected void doFilterInternal(@NonNull HttpServletRequest request,
                                   @NonNull HttpServletResponse response,
                                   @NonNull FilterChain filterChain) throws ServletException, IOException {
//       response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
//       response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//       response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
//       response.setHeader("Access-Control-Allow-Credentials", "true");
       final String authHeader = request.getHeader("Authorization");
       final String jwt;
       final String username;
       if (authHeader == null || !authHeader.startsWith("Bearer ")){
           filterChain.doFilter(request, response);
           return;
       }
       jwt = authHeader.substring(7);
       username = jwtService.extractUsername(jwt);
       System.out.println(username);
       if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
           UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
           if (jwtService.isTokenValid(jwt, userDetails)){
               UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                       userDetails,
                       null,
                       userDetails.getAuthorities()
               );
               authToken.setDetails(
                       new WebAuthenticationDetailsSource().buildDetails(request)
               );
               SecurityContextHolder.getContext().setAuthentication(authToken);
           }else {
               logger.error("Invalid Token");
           }
       }
       filterChain.doFilter(request, response);

   }
}
