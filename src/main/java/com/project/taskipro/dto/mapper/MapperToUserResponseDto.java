package com.project.taskipro.dto.mapper;

import com.project.taskipro.dto.desk.UsersOnDeskResponseDto;
import com.project.taskipro.model.desks.UserRights;
import com.project.taskipro.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserService.class})
public interface MapperToUserResponseDto {
    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "rightType", source = "rightType")
    UsersOnDeskResponseDto mapToUserResponseDto(UserRights userRights);
}
