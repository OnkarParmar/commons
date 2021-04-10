package com.teamteach.commons.security.authentication;

import org.springframework.security.authentication.AuthenticationProvider;

public interface IAuthenticationProviderFactory {
    AuthenticationProvider getConfiguredProvider();
}
