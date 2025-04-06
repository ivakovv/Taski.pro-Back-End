package com.project.taskipro.dto.mapper.git;

import com.project.taskipro.dto.git.GitCommitResponseDto;
import com.project.taskipro.model.git.GitCommit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MapperToGitCommitResponseDto {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "commitHash", source = "commitHash")
    @Mapping(target = "shortMessage", source = "shortMessage")
    @Mapping(target = "fullMessage", source = "fullMessage")
    @Mapping(target = "authorName", source = "authorName")
    @Mapping(target = "authorEmail", source = "authorEmail")
    @Mapping(target = "commitDate", source = "commitDate")
    @Mapping(target = "repositoryId", source = "repository.id")
    @Mapping(target = "repositoryName", source = "repository.repositoryName")
    @Mapping(target = "commitUrl", expression = "java(gitCommit.getCommitUrl())")
    GitCommitResponseDto mapToGitCommitResponseDto(GitCommit gitCommit);
}
