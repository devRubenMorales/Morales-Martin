package com.example.orders.security;

import java.io.Serializable;


public class AuthenticationReq implements Serializable {

        private static final long serialVersionUID = 1L;

        private String username;
        private String password;

        // Constructor por defecto necesario para la deserialización JSON
        public AuthenticationReq() {
        }

        public AuthenticationReq(String username, String password) {
                this.username = username;
                this.password = password;
        }

        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }
}
