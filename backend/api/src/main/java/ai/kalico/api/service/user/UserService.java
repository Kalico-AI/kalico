package com.kalico.api.service.user;

import com.kalico.model.UserProfileResponse;

/**
 * @author Biz Melesse
 * created on 10/17/22
 */
public interface UserService {

  /**
   * Get session user profile. If the user does not exist, create a new record.
   * @return
   */
  UserProfileResponse getOrCreateUserprofileAsync();

  UserProfileResponse getOrCreateUserprofile();
}
