package com.kalico.api.service.instagram4j.exceptions;

import com.kalico.api.service.instagram4j.IGClient;
import com.kalico.api.service.instagram4j.responses.accounts.LoginResponse;
import lombok.Getter;

@Getter
public class IGLoginException extends IGResponseException {
    private final IGClient client;
    private final LoginResponse loginResponse;

    public IGLoginException(IGClient client, LoginResponse body) {
        super(body);
        this.client = client;
        this.loginResponse = body;
    }

}
