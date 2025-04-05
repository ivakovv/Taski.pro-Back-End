package com.project.taskipro.dto.mapper.git;

import com.project.taskipro.dto.git.AddGitRepositoryDto;
import com.project.taskipro.model.desks.Desks;
import com.project.taskipro.model.git.GitRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = LocalDateTime.class)
public interface MapperToGitRepository {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createDate", expression = "java(LocalDateTime.now())")
    @Mapping(target = "lastSyncDate", ignore = true)
    @Mapping(target = "repositoryName", source = "request.repositoryName")
    @Mapping(target = "repositoryUrl", source = "request.repositoryUrl")
    @Mapping(target = "repositoryDescription", source = "request.repositoryDescription")
    @Mapping(target = "desk", source = "desk")
    @Mapping(target = "branchName", expression = "java(getBranchName(request))")
    GitRepository mapToGitRepository(AddGitRepositoryDto request, Desks desk);

    @Named("getBranchName")
    default String getBranchName(AddGitRepositoryDto request) {
        return request.branchName() != null && !request.branchName().isEmpty()
                ? request.branchName() : "main";
    }
}
