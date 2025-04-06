package com.project.taskipro.dto.mapper.git;

import com.project.taskipro.dto.git.GitRepositoryResponseDto;
import com.project.taskipro.model.git.GitRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MapperToGitRepositoryResponseDto {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "repositoryUrl", source = "repositoryUrl")
    @Mapping(target = "repositoryName", source = "repositoryName")
    @Mapping(target = "repositoryDescription", source = "repositoryDescription")
    @Mapping(target = "branchName", source = "branchName")
    @Mapping(target = "lastSyncDate", source = "lastSyncDate")
    @Mapping(target = "createDate", source = "createDate")
    GitRepositoryResponseDto mapToGitRepositoryResponseDto(GitRepository gitRepository);
}
