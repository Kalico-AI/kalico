package com.kalico.api.service.instagram4j.responses.users;

import com.kalico.api.service.instagram4j.models.user.User;
import com.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserResponse extends IGResponse {
    private User user;
}
