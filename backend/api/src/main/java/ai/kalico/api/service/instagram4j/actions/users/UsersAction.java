package com.kalico.api.service.instagram4j.actions.users;

import com.kalico.api.service.instagram4j.IGClient;
import com.kalico.api.service.instagram4j.models.user.User;
import com.kalico.api.service.instagram4j.requests.users.UsersInfoRequest;
import com.kalico.api.service.instagram4j.requests.users.UsersSearchRequest;
import com.kalico.api.service.instagram4j.requests.users.UsersUsernameInfoRequest;
import com.kalico.api.service.instagram4j.responses.users.UserResponse;
import com.kalico.api.service.instagram4j.responses.users.UsersSearchResponse;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsersAction {
    @NonNull
    private IGClient client;

    public CompletableFuture<UserAction> findByUsername(String username) {
        return new UsersUsernameInfoRequest(username).execute(client)
                .thenApply(response -> new UserAction(client, response.getUser()));
    }

    public CompletableFuture<User> info(long pk) {
        return new UsersInfoRequest(pk).execute(client)
                .thenApply(UserResponse::getUser);
    }

    public CompletableFuture<UsersSearchResponse> search(String query) {
        return new UsersSearchRequest(query).execute(client);
    }

}
