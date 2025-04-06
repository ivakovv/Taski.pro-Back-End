package com.project.taskipro.dto.mapper.user;

import com.project.taskipro.dto.user.UserResponseDto;
import com.project.taskipro.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface MapperToUserResponse {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "firstname", source = "firstname")
    @Mapping(target = "lastname", source = "lastname")
    @Mapping(target = "email", source = "email")
    UserResponseDto mapToUserResponse(User user);
}
