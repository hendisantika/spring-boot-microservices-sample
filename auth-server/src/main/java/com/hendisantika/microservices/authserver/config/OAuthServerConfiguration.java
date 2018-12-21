package com.hendisantika.microservices.authserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;

/**
 * Created by IntelliJ IDEA.
 * Project : auth-server
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 2018-12-17
 * Time: 05:55
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@EnableAuthorizationServer
public class OAuthServerConfiguration extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcUserDetailsService jdbcUserDetailsService;

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        //Keypair is the alias name -> anilkeystore.jks / password / anila
        KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("anilkeystore.jks"), "password".toCharArray())
                .getKeyPair("anila");
        converter.setKeyPair(keyPair);
        return converter;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(this.dataSource).withClient("acme").secret("acmesecret").authorizedGrantTypes("authorization_code",
                "client_credentials", "password", "implicit", "refresh_token").scopes("openid");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager).accessTokenConverter(jwtAccessTokenConverter())
                .userDetailsService(this.jdbcUserDetailsService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    /**
     * Configure the {@link AuthenticationManagerBuilder} with initial
     * configuration to setup users.
     * <p>
     * Higher priority since lesser ordered value indicate higher priority.
     * {@link Ordered#LOWEST_PRECEDENCE} has value as {@link Integer#MAX_VALUE}
     *
     * @author anilallewar
     */
    @Configuration
    @Order(Ordered.LOWEST_PRECEDENCE - 30)
    protected static class AuthenticationManagerConfiguration extends GlobalAuthenticationConfigurerAdapter {

        @Autowired
        private DataSource dataSource;

        @Autowired
        private JdbcUserDetailsService jdbcUserDetailsService;

        /**
         * Setup 2 users with different roles
         */
        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            // @formatter:off
            auth.jdbcAuthentication().dataSource(dataSource).withUser("dave").password("secret").roles("USER").and()
                    .withUser("anil").password("password").roles("ADMIN", "USER").and().getUserDetailsService();
            // @formatter:on

            // Add the default service
            jdbcUserDetailsService.addService(auth.getDefaultUserDetailsService());
        }
    }

}