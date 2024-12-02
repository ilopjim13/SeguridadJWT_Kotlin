package com.es.jwtSecurityKotlin.security

import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Autowired
    private lateinit var rsaKeys:RSAKeysProperties

    @Bean
    fun securityFilterChain(http: HttpSecurity):SecurityFilterChain {

        return http
            .csrf { csrf -> csrf.disable() } // cross-site forgery
            .authorizeHttpRequests { auth -> auth
                .requestMatchers("/usuarios/login").permitAll()
                .requestMatchers("/rutas_protegidas/recurso1").permitAll()
                .requestMatchers("/rutas_protegidas/recurso2/{id}").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/rutas_protegidas/recurso2/{id}").authenticated()
                .requestMatchers("/secretos_extra_confidenciales/ficha1").authenticated()
                .requestMatchers("/secretos_extra_confidenciales/ficha2").permitAll()
                .requestMatchers("/rutas_publicas/**").permitAll()
            } // Los recursos protegidos y publicos
            .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)}
            .httpBasic(Customizer.withDefaults())
            .build()
    }

    @Bean
    fun passwordEncoder():PasswordEncoder = BCryptPasswordEncoder()


    /*
        Metodo que inicializa un objeto de tipo AuthenticationManager
     */
    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration) :AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    /*
        Metodo para codificar un JWT
     */
    @Bean
    fun jwtEncoder():JwtEncoder {
        val jwk:JWK = RSAKey.Builder(rsaKeys.publicKey).privateKey(rsaKeys.privateKey).build()
        val jwks:JWKSource<SecurityContext> = ImmutableJWKSet(JWKSet(jwk))
        return NimbusJwtEncoder(jwks)
    }


    /*
        Metodo para decodificar un JWT
     */
    @Bean
    fun jwtDecoder():JwtDecoder {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey).build()
    }

}