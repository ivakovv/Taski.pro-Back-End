package com.project.taskipro.service;


import com.project.taskipro.dto.DeskCreateDto;
import com.project.taskipro.dto.DeskResponseDto;
import com.project.taskipro.model.desks.Desks;
import com.project.taskipro.model.desks.RightType;
import com.project.taskipro.model.desks.UserRights;
import com.project.taskipro.repository.DeskRepository;
import com.project.taskipro.repository.UserRightsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Date;

@RequiredArgsConstructor
@Service
public class DeskService {

    private final DeskRepository deskRepository;
    private final UserRightsRepository userRightsRepository;
    private final UserServiceImpl userService;

    public void createDesk(DeskCreateDto request){
        Desks desk = new Desks();

        desk.setDeskName(request.deskName());
        desk.setDeskDescription(request.deskDescription());
        desk.setDeskCreateDate(new Date());

        //пересмотреть это поле
        desk.setDeskFinishDate(new Date());

        Desks createdDesk = deskRepository.save(desk);

        UserRights userRights = new UserRights();

        userRights.setDesk(createdDesk);
        userRights.setUser(userService.getCurrentUser());
        userRights.setRightType(RightType.CREATOR);

        userRightsRepository.save(userRights);
    }

    public void deleteDesk(Long deskId){
        Desks desk = getDeskById(deskId);
        deskRepository.delete(desk);
    }

    private Desks getDeskById(Long deskId){
        return deskRepository.findById(deskId).orElseThrow(() -> new IllegalArgumentException ("Доска с id: " + deskId + " не найдена!"));
    }

    public DeskResponseDto mapToResponseDto(Desks desk) {
        return DeskResponseDto.builder()
                .id(desk.getId())
                .deskName(desk.getDeskName())
                .deskDescription(desk.getDeskDescription())
                .deskCreateDate(desk.getDeskCreateDate())
                .deskFinishDate(desk.getDeskFinishDate())
                .userLimit(desk.getUserLimit())
                .build();
    }
}
