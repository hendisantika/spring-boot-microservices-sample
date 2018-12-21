package com.hendisantika.microservices.authserver.service;

import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * Project : auth-server
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 2018-12-17
 * Time: 05:56
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class JdbcUserDetailsService implements UserDetailsService {

    private List<UserDetailsService> uds = new LinkedList<>();

    public JdbcUserDetailsService() {
        // Default constructor
    }

    /**
     * Add the default user detail service or any other user detail service so
     * that we can validate the user.
     *
     * @param srv
     */
    public void addService(UserDetailsService srv) {
        uds.add(srv);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        if (uds != null) {
            for (UserDetailsService srv : uds) {
                try {
                    final UserDetails details = srv.loadUserByUsername(userName);
                    if (details != null) {
                        return details;
                    }
                } catch (UsernameNotFoundException ex) {
                    assert ex != null;
                } catch (Exception ex) {
                    throw ex;
                }
            }
        }

        throw new UsernameNotFoundException("Unknown user");
    }
}
