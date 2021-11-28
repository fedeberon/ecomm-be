package com.ideaas.ecomm.ecomm.payload;

import com.ideaas.ecomm.ecomm.services.UserService;
import org.springframework.security.config.authentication.UserServiceBeanDefinitionParser;

public class UserSession {

    private String token;

    private String username;

    private String lastName;

    public UserSession(UserSessionBuilder builder) {
        this.token = builder.token;
        this.username = builder.username;
        this.lastName = builder.lastName;
    }

    public static class UserSessionBuilder {
        private String token;

        private String username;

        private String lastName;

        public UserSessionBuilder withToken(final String token) {
            this.token = token;

            return this;
        }

        public UserSessionBuilder withUsername(final String username) {
            this.username = username;

            return this;
        }

        public UserSessionBuilder withLastName(final String lastName) {
            this.lastName = lastName;

            return this;
        }


        public UserSession build() {
            return new UserSession(this);
        }

    }

}
