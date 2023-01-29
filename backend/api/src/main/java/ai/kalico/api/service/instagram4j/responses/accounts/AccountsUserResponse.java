package com.kalico.api.service.instagram4j.responses.accounts;

import com.kalico.api.service.instagram4j.models.user.User;
import com.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AccountsUserResponse extends IGResponse {
    private User user;
}
