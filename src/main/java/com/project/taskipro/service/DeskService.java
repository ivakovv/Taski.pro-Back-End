package com.project.taskipro.service;

import com.project.taskipro.dto.desk.AddUserOnDeskDto;
import com.project.taskipro.dto.desk.ChangeUserRightsDto;
import com.project.taskipro.dto.desk.DeskCreateDto;
import com.project.taskipro.dto.desk.DeskResponseDto;
import com.project.taskipro.dto.desk.DeskUpdateDto;
import com.project.taskipro.dto.desk.UsersOnDeskResponseDto;
import com.project.taskipro.dto.mapper.desk.MapperToDesk;
import com.project.taskipro.dto.mapper.desk.MapperToDeskResponseDto;
import com.project.taskipro.dto.mapper.MapperToUserResponseDto;
import com.project.taskipro.dto.mapper.desk.MapperUpdateDesk;
import com.project.taskipro.dto.mapper.user.MapperToUserResponse;
import com.project.taskipro.dto.user.UserResponseDto;
import com.project.taskipro.model.desks.Desks;
import com.project.taskipro.model.desks.RightType;
import com.project.taskipro.model.desks.UserRights;
import com.project.taskipro.model.user.User;
import com.project.taskipro.repository.DeskRepository;
import com.project.taskipro.repository.UserRepository;
import com.project.taskipro.repository.UserRightsRepository;
import com.project.taskipro.service.access.SubscriptionAccessService;
import com.project.taskipro.service.access.UserAccessService;
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
    private final UserAccessService userAccessService;
    private final SubscriptionAccessService subscriptionAccessService;
    private final UserRepository userRepository;
    private final MapperToDeskResponseDto mapperToDeskResponseDto;
    private final MapperToUserResponseDto mapperToUserResponseDto;
    private final MapperToUserResponse mapperToUserResponse;
    private final MapperToDesk mapperToDesk;
    private final MapperUpdateDesk mapperUpdateDesk;

    public DeskResponseDto createDesk(DeskCreateDto request){
        User currentUser = userService.getCurrentUser();
        Desks desk = mapperToDesk.deskCreateDtoToDesks(request, subscriptionAccessService.getLimitOfUsersOnDesk(currentUser));
        Desks createdDesk = deskRepository.save(desk);
        UserRights userRights = UserRights.builder()
                .desk(createdDesk)
                .user(currentUser)
                .rightType(RightType.CREATOR)
                .build();
        userRightsRepository.save(userRights);
        return mapperToDeskResponseDto.mapToDeskResponseDto(desk, currentUser.getUsername());
    }

    public void deleteDesk(Long deskId){
        Desks desk = getDeskById(deskId);
        userAccessService.checkUserAccess(desk, RightType.CREATOR);
        deskRepository.delete(desk);
    }

    public void addUserOnDesk(Long deskId, AddUserOnDeskDto addUserDto){
        Desks desk = getDeskById(deskId);
        userAccessService.checkUserAccess(desk, RightType.CONTRIBUTOR);
        User user = userRepository.findByUsername(addUserDto.username()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Пользователь с именем: %s не найден", addUserDto.username())));
        if(userRightsRepository.findByDeskAndUser(desk, user).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Данный пользователь уже участвует в доске");
        }
        checkUsersOnDeskLimit(deskId);
        UserRights userRights = UserRights.builder()
                .user(user)
                .desk(desk)
                .rightType(addUserDto.rightType())
                .build();
        userRightsRepository.save(userRights);
    }

    public void deleteUserFromDesk(Long deskId, Long userId){
        Desks desk = getDeskById(deskId);
        User user = getUser(userId);

        userAccessService.checkUserAccess(desk, RightType.CREATOR);

        UserRights userRights = userRightsRepository.findByDeskAndUser(desk, user).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Данный пользователь не имеет доступа к доске!"));
        userRightsRepository.delete(userRights);
    }

    public DeskResponseDto updateDesk(Long deskId, DeskUpdateDto deskUpdateDto){
        Desks desk = getDeskById(deskId);
        userAccessService.checkUserAccess(desk, RightType.CONTRIBUTOR);
        mapperUpdateDesk.updateDeskFromDto(deskUpdateDto, desk);
        deskRepository.save(desk);
        return mapperToDeskResponseDto.mapToDeskResponseDto(desk, getCreatorOfDesk(desk.getId()).getUsername());
    }

    public Desks getDeskById(Long deskId){
        return deskRepository.findById(deskId).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Доска с id: %d не найдена!", deskId)));
    }

    public List<UsersOnDeskResponseDto> getUsersOnDesk(Long deskId){
        userAccessService.checkUserAccess(getDeskById(deskId), RightType.MEMBER);
        List<UserRights> userRights = userRightsRepository.findUsersByDeskId(deskId);
        return userRights.stream()
                .map(mapperToUserResponseDto::mapToUserResponseDto)
                .collect(Collectors.toList());
    }

    public List<DeskResponseDto> getDesksForUser(){
        User currentUser = userService.getCurrentUser();
        List<UserRights> userRights = userRightsRepository.findByUser(currentUser);
        return userRights.stream()
                .map(ur -> mapperToDeskResponseDto.mapToDeskResponseDto(ur.getDesk(), getCreatorOfDesk(ur.getDesk().getId()).getUsername()))
                .collect(Collectors.toList());
    }

    public DeskResponseDto getDesk(Long deskId){
        Desks desk = getDeskById(deskId);
        return mapperToDeskResponseDto.mapToDeskResponseDto(desk, getCreatorOfDesk(deskId).getUsername());
    }

    public List<UserResponseDto> getListOfUsers(){
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(mapperToUserResponse::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public void changeUserRightsOnDesk(Long deskId, ChangeUserRightsDto changeUserRights){
        Desks desk = getDeskById(deskId);
        userAccessService.checkUserAccess(desk, RightType.CREATOR);
        UserRights changedUser = userRightsRepository.findByDeskAndUser(desk, getUser(changeUserRights.userId())).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Пользователь с id %d не найден!", changeUserRights.userId())));
        changedUser.setRightType(changeUserRights.rightType());
        userRightsRepository.save(changedUser);
    }

    private User getUser(Long userId){
        return userRepository.findById(userId).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Пользователь с id: %d не найден!", userId)));
    }

    private void checkUsersOnDeskLimit(Long deskId){
        Desks desk = getDeskById(deskId);
        if (userRightsRepository.countByDeskId(deskId) >= desk.getUserLimit()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    String.format("Превышен лимит пользователей на доске. Максимум: %d пользователей", desk.getUserLimit()));
        }
    }

    private User getCreatorOfDesk(Long deskId){
        return userRightsRepository.findCreatorByDeskId(deskId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("У доски с id: %d не найден владелей", deskId)));
    }

    public String getUsersForChatGPT(long deskId) {
        List<UsersOnDeskResponseDto> rawUsers = getUsersOnDesk(deskId);
        if (rawUsers == null || rawUsers.isEmpty()) {
            return "";
        }
        return rawUsers.stream()
                .map(user -> String.format(
                        "Пользователь: %s, роль: %s",
                        user.userName(),
                        user.roleType()))
                .collect(Collectors.joining("; "));
    }
    }
}
