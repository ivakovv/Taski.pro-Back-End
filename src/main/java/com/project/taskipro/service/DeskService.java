package com.project.taskipro.service;

import com.project.taskipro.dto.desk.DeskCreateDto;
import com.project.taskipro.dto.desk.DeskResponseDto;
import com.project.taskipro.dto.desk.DeskUpdateDto;
import com.project.taskipro.dto.desk.UsersOnDeskResponseDto;
import com.project.taskipro.dto.mapper.MapperToDesk;
import com.project.taskipro.dto.mapper.MapperToDeskResponseDto;
import com.project.taskipro.dto.mapper.MapperToUserResponseDto;
import com.project.taskipro.model.desks.Desks;
import com.project.taskipro.model.desks.RightType;
import com.project.taskipro.model.desks.UserRights;
import com.project.taskipro.model.user.User;
import com.project.taskipro.repository.DeskRepository;
import com.project.taskipro.repository.UserRepository;
import com.project.taskipro.repository.UserRightsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DeskService {

    private final DeskRepository deskRepository;
    private final UserRightsRepository userRightsRepository;
    private final UserServiceImpl userService;
    private final UserRepository userRepository;
    private final MapperToDeskResponseDto mapperToDeskResponseDto;
    private final MapperToUserResponseDto mapperToUserResponseDto;
    private final MapperToDesk mapperToDesk;

    public Desks createDesk(DeskCreateDto request){
        Desks desk = mapperToDesk.deskCreateDtoToDesks(request);
        Desks createdDesk = deskRepository.save(desk);
        UserRights userRights = UserRights.builder()
                .desk(createdDesk)
                .user(userService.getCurrentUser())
                .rightType(RightType.CREATOR)
                .build();
        userRightsRepository.save(userRights);

        return createdDesk;
    }

    public void deleteDesk(Long deskId){
        Desks desk = getDeskById(deskId);
        checkUserAccess(desk, RightType.CREATOR);
        deskRepository.delete(desk);
    }

    public void addUserOnDesk(Long deskId, Long userId, RightType rightType){
        Desks desk = getDeskById(deskId);
        User user = getUser(userId);
        checkUserAccess(desk, RightType.CONTRIBUTOR);
        UserRights userRights = UserRights.builder()
                .user(user)
                .desk(desk)
                .rightType(rightType)
                .build();
        userRightsRepository.save(userRights);
    }

    public void deleteUserFromDesk(Long deskId, Long userId){
        Desks desk = getDeskById(deskId);
        User user = getUser(userId);

        checkUserAccess(desk, RightType.CREATOR);

        UserRights userRights = userRightsRepository.findByDeskAndUser(desk, user).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Данный пользователь не имеет доступа к доске!"));
        userRightsRepository.delete(userRights);
    }

    public void updateDesk(Long deskId, DeskUpdateDto deskUpdateDto){
        Desks desk = getDeskById(deskId);

        checkUserAccess(desk, RightType.CONTRIBUTOR);

        desk.setDeskName(deskUpdateDto.deskName());
        desk.setDeskDescription(deskUpdateDto.deskDescription());
        desk.setDeskFinishDate(deskUpdateDto.deskFinishDate());

        deskRepository.save(desk);
    }

    public Desks getDeskById(Long deskId){
        return deskRepository.findById(deskId).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Доска с id: %d не найдена!", deskId)));
    }

    public List<UsersOnDeskResponseDto> getUsersOnDesk(Long deskId){
        checkUserAccess(getDeskById(deskId), RightType.MEMBER);
        List<UserRights> userRights = userRightsRepository.findUsersByDeskId(deskId);
        return userRights.stream()
                .map(mapperToUserResponseDto::mapToUserResponseDto)
                .collect(Collectors.toList());
    }

    public List<DeskResponseDto> getDesksForUser(){
        User currentUser = userService.getCurrentUser();
        List<UserRights> userRights = userRightsRepository.findByUser(currentUser);
        return userRights.stream()
                .map(ur -> mapperToDeskResponseDto.mapToDeskResponseDto(ur.getDesk()))
                .collect(Collectors.toList());
    }

    private User getUser(Long userId){
        return userRepository.findById(userId).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Пользователь с id: %d не найден!", userId)));
    }

    private void checkUserAccess(Desks desk, RightType rightType){
        User user = userService.getCurrentUser();
        UserRights userRights = userRightsRepository.findByDeskAndUser(desk, user).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.FORBIDDEN, "Вам недоступна данная доска!"));

        if(!hasAcceptableRights(userRights.getRightType(), rightType)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "У вас недостаточно прав для этого действия!");
        }
    }

    private boolean hasAcceptableRights(RightType currentRight, RightType requiredRight){
        int currentRightValue = getRightValue(currentRight);
        int requiredRightValue = getRightValue(requiredRight);

        return currentRightValue >= requiredRightValue;
    }

    private int getRightValue(RightType rightType){
        return switch (rightType) {
            case MEMBER -> 1;
            case CONTRIBUTOR -> 2;
            case CREATOR -> 3;
        };
    }

}
