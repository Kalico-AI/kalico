package com.kalico.api.service.instagram4j.responses.friendships;

import com.kalico.api.service.instagram4j.models.friendships.Friendship;
import com.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FriendshipStatusResponse extends IGResponse {
    private Friendship friendship_status;
}
