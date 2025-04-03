package com.project.taskipro.dto.mapper.git;

import com.project.taskipro.model.git.GitCommit;
import com.project.taskipro.model.git.GitRepository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring")
public interface MapperToGitCommit {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "commitHash", source = "revCommit.name")
    @Mapping(target = "shortMessage", source = "revCommit.shortMessage")
    @Mapping(target = "fullMessage", source = "revCommit.fullMessage")
    @Mapping(target = "authorName", source = "revCommit.authorIdent.name")
    @Mapping(target = "authorEmail", source = "revCommit.authorIdent.emailAddress")
    @Mapping(target = "commitDate", source = "revCommit", qualifiedByName = "mapCommitDate")
    @Mapping(target = "repository", source = "repository")
    GitCommit mapToGitCommit(RevCommit revCommit, GitRepository repository);

    @Named("mapCommitDate")
    default LocalDateTime mapCommitDate(RevCommit revCommit) {
        return LocalDateTime.ofInstant(revCommit.getAuthorIdent().getWhen().toInstant(), ZoneId.systemDefault());
    }
}
