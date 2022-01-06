package com.revature.RevRelay.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Builds a RequestMatcher for PUBLIC_URLS, defined as URLs that are accessible
     * without authorization.
     * Included is an attempt at allowing Swagger to pass without authentication,
     * which was unsuccessful at
     * the time of writing this documentation (NL 211229).
     */
    private static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(
            // this allows public pages including user creation and login
            new AntPathRequestMatcher("/public/**"));

    /**
     * Builds a RequestMatcher for PROTECTED_URLS by negation of the list of
     * PUBLIC_URLS.
     */
    private static final RequestMatcher PROTECTED_URLS = new NegatedRequestMatcher(PUBLIC_URLS);

    TokenAuthProvider provider;
    PasswordEncoder passwordEncoder;
    UserDetailsService userDetailsService;

    /**
     * Autowired constructor providing TokenAuthProvider, PasswordEncoder, and
     * UserDetailsService.
     * 
     * @param provider           TokenAuthProvider from class in the security
     *                           package.
     * @param passwordEncoder    PasswordEncoder from RevRelayConfig; new
     *                           BCryptPasswordEncoder().
     * @param userDetailsService UserDetailsService from UserService.
     */
    @Autowired
    WebSecurityConfig(TokenAuthProvider provider, PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService) {
        super();
        this.provider = requireNonNull(provider);
        this.passwordEncoder = requireNonNull(passwordEncoder);
        this.userDetailsService = requireNonNull(userDetailsService);
    }

    /**
     * Configure override to use TokenAuthProvider in configuration.
     * 
     * @param auth AuthenticationManagerBuilder configured by writing.
     */
    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(provider);
    }

    /**
     * Configure override for WebSecurity. At this time, configures only
     * PUBLIC_URLS.
     *
     * @param web WebSecurity configured by wiring.
     */
    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().requestMatchers(PUBLIC_URLS);
    }

    /**
     * Configures HttpSecurity; this is where the magic happens. Many of these
     * elements add default or disabled
     * configuration elements, which are largely self-explanatory.
     * 
     * @param http Provided/wired HttpSecurity object.
     * @throws Exception Possible untyped exceptions thrown by .cors() and
     *                   restAuthenticationFilter() elements.
     */
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .cors().and()
                .sessionManagement()
                .sessionCreationPolicy(STATELESS)
                .and()
                .exceptionHandling()
                // this entry point handles when you request a protected page, and you are not
                // yet
                // authenticated
                .defaultAuthenticationEntryPointFor(forbiddenEntryPoint(), PROTECTED_URLS)
                .and()
                .authenticationProvider(provider)
                .addFilterBefore(restAuthenticationFilter(), AnonymousAuthenticationFilter.class)
                .authorizeRequests()
                .requestMatchers(PROTECTED_URLS)
                .authenticated()
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable();
    }

    /**
     * Provides default CorsConfigurationSource using applyPermitDefaultValues().
     * 
     * @return CorsConfigurationSource configured with applyPermitDefaultValues on
     *         all endpoints.
     */

    /*
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
    */
    // Alternate bean for providing CorsConfigurationSource. Necessary for allowing
    // non-default verbs such as delete.
    // As the defaults are plenty, this should not be used, but I'm keeping it just
    // in case we need it. - NL
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH",
        "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization",
        "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new
        UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Provides a TokenAuthFilter configured on PROTECTED_URLS using
     * authenticationManager() and successHandler().
     * 
     * @return Configured TokenAuthFilter.
     * @throws Exception Catches untyped Exceptions thrown by
     *                   authenticationManager().
     */
    @Bean
    TokenAuthFilter restAuthenticationFilter() throws Exception {
        final TokenAuthFilter filter = new TokenAuthFilter(PROTECTED_URLS);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(successHandler());
        return filter;
    }

    /**
     * Provides a SimpleUrlAuthenticationSuccessHandler containing an empty
     * NoRedirectStrategy().
     * 
     * @return SuccessHandler as above.
     */
    @Bean
    SimpleUrlAuthenticationSuccessHandler successHandler() {
        final SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setRedirectStrategy(new NoRedirectStrategy());
        return successHandler;
    }

    /**
     * Disable Spring boot automatic filter registration.
     */
    @Bean
    FilterRegistrationBean disableAutoRegistration(final TokenAuthFilter filter) {
        final FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }

    /**
     * Creates a bean establishing forbidden endpoints as forbidden.
     * 
     * @return AuthenticationEntryPoint defining forbidden endpoints.
     */
    @Bean
    AuthenticationEntryPoint forbiddenEntryPoint() {
        return new HttpStatusEntryPoint(FORBIDDEN);
    }
}
