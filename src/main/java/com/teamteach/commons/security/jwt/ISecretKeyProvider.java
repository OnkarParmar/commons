package com.teamteach.commons.security.jwt;

import java.security.Key;

public interface ISecretKeyProvider {
    Key getKey();
}
