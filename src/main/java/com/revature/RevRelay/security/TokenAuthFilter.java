package com.revature.RevRelay.security;

import com.revature.RevRelay.services.UserService;
import com.revature.RevRelay.utils.JwtUtil;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Optional.ofNullable;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

//@FieldDefaults(level = PRIVATE, makeFinal = true)
@FieldDefaults(level = PRIVATE)
final class TokenAuthFilter extends AbstractAuthenticationProcessingFilter {
    private static final String BEARER = "Bearer";

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserService userService;

    TokenAuthFilter(final RequestMatcher requiresAuth) {
        super(requiresAuth);
    }

    /**
     * Takes a request & Response and returns an Authentication object
     *
     * @param request the http request to check for an authorization header
     * @param response http response taken as a param
     * @return returns an Authentication object
     */
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) {
        String param = ofNullable(request.getHeader(AUTHORIZATION))
                .orElse(request.getParameter("t"));

        String token = ofNullable(param)
                .map(value -> value.replace("Bearer ", ""))
                .map(String::trim)
                .orElseThrow(() -> new BadCredentialsException("Missing Authentication Token"));
        Authentication auth = new PreAuthenticatedAuthenticationToken(jwtUtil.extractUsername(token),token);
        if (jwtUtil.validateToken(token,userService.loadUserByUsername(jwtUtil.extractUsername(token)))) {
            auth.setAuthenticated(true);
        }
        else {
            auth.setAuthenticated(false);
        }
        return auth;
    }

    /**
     * Function for successful Authentication
     *
     * @param request  the HttpRequest
     * @param response The HttpResponse
     * @param chain The filter chain used by spring security
     * @param authResult the result of Authentication
     * @throws IOException both successfulAuthentication and doFilter propagate IOExceptions
     * @throws ServletException both successfulAuthentication and doFilter propagate ServletExceptions
     */
    @Override
    protected void successfulAuthentication(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain chain,
            final Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}