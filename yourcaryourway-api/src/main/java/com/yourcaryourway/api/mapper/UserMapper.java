package com.yourcaryourway.api.mapper;

import com.yourcaryourway.api.dto.AuthResponse;
import com.yourcaryourway.api.dto.RegisterRequest;
import com.yourcaryourway.api.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toUser(RegisterRequest request);

    @Mapping(target = "id", expression = "java(user.getId().toString())")
    @Mapping(target = "token", source = "token")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    AuthResponse toAuthResponse(User user, String token);
}