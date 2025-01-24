package com.david.apis.loancalc.configs;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.GET;

import static org.springframework.security.config.Customizer.withDefaults;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import static org.springframework.http.HttpMethod.OPTIONS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig implements WebFluxConfigurer {
	private static Logger LOG = LoggerFactory.getLogger(SecurityConfig.class);
	
	private String username;
	private String password;
	
	
	public SecurityConfig(@Value("${app.username}") String username, @Value("${app.password}") String password) {
		this.username = username;
		this.password = password;
		
		LOG.info("Client Credentials loaded from props file: {}:{}", username, password);
	}
	
	
	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		/*
		http
			.csrf(c -> c.disable())
			.authorizeExchange(exchange -> exchange
					.pathMatchers("/headerrouting/**").permitAll()	// Allow Swgger-UI be accessible publicly
	        		.pathMatchers("/actuator/**").permitAll()
	        		.pathMatchers("/eureka/**").permitAll()
	        		.pathMatchers("/oauth2/**").permitAll()
	        		.pathMatchers("/login/**").permitAll()
	        		.pathMatchers("/error/**").permitAll()
	        		.pathMatchers("/openapi/**").permitAll()
	        		.pathMatchers("/webjars/**").permitAll()
	        		.pathMatchers(OPTIONS).permitAll()
	        		//.pathMatchers(POST, "/calc/**").hasAuthority("SCOPE_calc:execute")
	        		.pathMatchers("/actuator/**").permitAll()
	        		.anyExchange().authenticated()
				).httpBasic(Customizer.withDefaults());
			//.oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
		
		*/
		
		http
			.csrf(c -> c.disable())
			.cors(c -> c.disable())
			.authorizeExchange((exchanges) ->
            	exchanges
            		.pathMatchers("/openapi/**").permitAll()
            		.pathMatchers("/webjars/**").permitAll()
            		.pathMatchers("/swagger-ui/**").permitAll()
            		.pathMatchers(OPTIONS).permitAll()
	            	// any URL that starts with /calc/ requires the role "ROLE_USER"
	            	.pathMatchers(POST, "/calc/**").hasRole("USER")
            )
			.httpBasic(withDefaults());
		
		return http.build();
	}
	
	@Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
          .allowedOrigins("http://localhost:3000", "https://loancalc-dave.netlify.app")
          .allowedMethods("*")
          .maxAge(3600);
    }
	
	@Bean
	CorsWebFilter corsFilter() {
		CorsConfiguration conf = new CorsConfiguration();
		
		conf.setAllowCredentials(true);
		conf.addAllowedOrigin("http://localhost:3000");
		conf.addAllowedOrigin("https://loancalc-dave.netlify.app");
		conf.addAllowedHeader("*");
		conf.addAllowedMethod("*");
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", conf);
		
		return new CorsWebFilter(source);
	}

	@Bean
	public MapReactiveUserDetailsService userDetailsService() {
		UserDetails user = User.withDefaultPasswordEncoder()
			.username(username)
			.password(password)
			.roles("USER")
			.build();
		return new MapReactiveUserDetailsService(user);
	}
}
