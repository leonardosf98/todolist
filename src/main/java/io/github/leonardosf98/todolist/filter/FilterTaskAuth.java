package io.github.leonardosf98.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.github.leonardosf98.todolist.user.UserModel;
import io.github.leonardosf98.todolist.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String servletPath = request.getServletPath();

        if (!servletPath.startsWith("/tasks/")) {
            filterChain.doFilter(request, response);
        } else {
            String authEncoded = request.getHeader("Authorization").substring("Basic".length()).trim();

            byte[] authDecoded = Base64.getDecoder().decode(authEncoded);

            String decodedString = new String(authDecoded);
            String[] credentials = decodedString.split(":");
            String username = credentials[0];
            String password = credentials[1];

            UserModel userToCheck = this.userRepository.findByUsername(username);
            if (userToCheck == null) {
                response.sendError(401);
            } else {
                BCrypt.Result passVerification = BCrypt.verifyer().verify(password.toCharArray(), userToCheck.getPassword());
                if (passVerification.verified) {
                    request.setAttribute("idUser", userToCheck.getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }
            }
        }
    }
}
