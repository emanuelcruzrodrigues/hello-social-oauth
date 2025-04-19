package com.emanuel.hello.service;

import com.emanuel.hello.domain.Account;

import java.util.Map;

public interface AccountFacade {
    Account register(Map<String, Object> attributes);
}
