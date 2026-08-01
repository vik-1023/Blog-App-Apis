package org.blog.apis.security;

import org.springframework.stereotype.Component;

@Component
public class JwtHelper {

    private String secret = "mysecretkeymysecretkeymysecretkey123456";
    private long jwtExpiration = 86400000;

}
