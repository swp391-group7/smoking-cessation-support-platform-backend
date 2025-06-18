// src/main/java/com/Swp_391_gr7/smoking_cessation_support_platform_backend/services/user/UserService.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.user;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.UpdateuserRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.UserDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;

import java.util.UUID;

public interface UserService {
    User updateUser(UUID userId, UpdateuserRequest request);
    UserDto getUserInfo(UUID userId);
}