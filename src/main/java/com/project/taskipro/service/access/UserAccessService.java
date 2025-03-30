package com.project.taskipro.service.access;

import com.project.taskipro.model.desks.Desks;
import com.project.taskipro.model.desks.RightType;
import com.project.taskipro.model.desks.UserRights;
import com.project.taskipro.model.user.User;
import com.project.taskipro.repository.UserRightsRepository;
import com.project.taskipro.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class UserAccessService {
    private final UserServiceImpl userService;
    private final UserRightsRepository userRightsRepository;

    public void checkUserAccess(Desks desk, RightType rightType){
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
