package com.project.taskipro.dto.mapper;

import com.project.taskipro.dto.desk.UsersOnDeskResponseDto;
import com.project.taskipro.model.desks.UserRights;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MapperToUserResponseDto {
    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "rightType", source = "rightType")
    @Mapping(target = "roleType", source = "user.roleType")
    UsersOnDeskResponseDto mapToUserResponseDto(UserRights userRights);
}
