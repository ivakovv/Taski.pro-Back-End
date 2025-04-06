package com.project.taskipro.service.git;

import com.project.taskipro.dto.git.AddGitRepositoryDto;
import com.project.taskipro.dto.git.GitCommitResponseDto;
import com.project.taskipro.dto.git.GitRepositoryResponseDto;
import com.project.taskipro.dto.mapper.git.MapperToGitCommit;
import com.project.taskipro.dto.mapper.git.MapperToGitCommitResponseDto;
import com.project.taskipro.dto.mapper.git.MapperToGitRepository;
import com.project.taskipro.dto.mapper.git.MapperToGitRepositoryResponseDto;
import com.project.taskipro.model.desks.Desks;
import com.project.taskipro.model.git.GitCommit;
import com.project.taskipro.model.git.GitRepository;
import com.project.taskipro.repository.GitCommitRepository;
import com.project.taskipro.repository.GitRepositoryStore;
import com.project.taskipro.service.DeskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GitService {

    private final DeskService deskService;
    private final GitRepositoryStore gitRepositoryStore;
    private final GitCommitRepository gitCommitRepository;
    private final MapperToGitRepository mapperToGitRepository;
    private final MapperToGitRepositoryResponseDto mapperToGitRepositoryResponseDto;
    private final MapperToGitCommitResponseDto mapperToGitCommitResponseDto;
    private final MapperToGitCommit mapperToGitCommit;

    public GitRepositoryResponseDto addRepositoryOnDesk(Long deskId, AddGitRepositoryDto request){
        Desks desk = deskService.getDeskById(deskId);
        if (gitRepositoryStore.findByRepositoryUrlAndDesk(request.repositoryUrl(), desk).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Репозиторий URL: %s уже добавлен к доске!", request.repositoryUrl()));
        }
        validateRepository(request.repositoryUrl(), request.branchName());
        GitRepository gitRepository = mapperToGitRepository.mapToGitRepository(request, desk);
        gitRepositoryStore.save(gitRepository);
        syncCommits(gitRepository);
        return mapperToGitRepositoryResponseDto.mapToGitRepositoryResponseDto(gitRepository);
    }

    public void removeRepositoryFromDesk(Long repositoryId, Long deskId){
        Desks desk = deskService.getDeskById(deskId);
        GitRepository repository = gitRepositoryStore.findByIdAndDesk(repositoryId, desk).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Репозитория с id: %d, нет на доске с id %d", repositoryId, deskId)));
        gitRepositoryStore.delete(repository);
    }

    public List<GitRepositoryResponseDto> getRepositoriesByDeskId(Long deskId) {
        List<GitRepository> repositories = gitRepositoryStore.findByDeskId(deskId);
        return repositories.stream()
                .map(mapperToGitRepositoryResponseDto::mapToGitRepositoryResponseDto)
                .collect(Collectors.toList());
    }

    public List<GitCommitResponseDto> getCommitsByRepositoryId(Long repositoryId) {
        List<GitCommit> commits = gitCommitRepository.findByRepositoryId(repositoryId);
        return commits.stream()
                .map(mapperToGitCommitResponseDto::mapToGitCommitResponseDto)
                .collect(Collectors.toList());
    }

    public void syncRepository(Long repositoryId) {
        GitRepository repository = gitRepositoryStore.findById(repositoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Репозиторий с id: %d не найден", repositoryId)));
        syncCommits(repository);
    }

    @Scheduled(fixedRateString = "${app.git.sync.interval}")
    private void syncAllRepositories(){
        log.info("Starting synchronization of Git repositories");
        List<GitRepository> repositories = gitRepositoryStore.findAll();
        for (GitRepository repository : repositories){
            syncCommits(repository);
        }
        log.info("Synchronization ended successful");
    }

    public void syncCommits(GitRepository repository){
        try (TempDirectory tempDir = new TempDirectory(String.format("git-temp-%d", repository.getId()))) {
            log.info(String.format("Synchronizing repository: %s", repository.getRepositoryUrl()));
            Git git = Git.cloneRepository()
                    .setURI(repository.getRepositoryUrl())
                    .setDirectory(tempDir.toFile())
                    .setBranch(repository.getBranchName())
                    .setNoTags()
                    .setNoCheckout(true)
                    .call();
            log.info(String.format("Cloning repository %s completed", repository.getRepositoryUrl()));
            Set<String> processedHashes = new HashSet<>();
            List<GitCommit> commits = new ArrayList<>();
            Iterable<RevCommit> revCommits = git.log().call();
            for (RevCommit revCommit : revCommits) {
                if (processedHashes.contains(revCommit.getName())) {
                    continue;
                }
                if (!gitCommitRepository.existsByCommitHashAndRepository(revCommit.getName(), repository)) {
                    GitCommit commit = mapperToGitCommit.mapToGitCommit(revCommit, repository);
                    commits.add(commit);
                    processedHashes.add(revCommit.getName());
                }
            }
            if (!commits.isEmpty()) {
                gitCommitRepository.saveAll(commits);
            }
            repository.setLastSyncDate(LocalDateTime.now());
            gitRepositoryStore.save(repository);
            git.close();
            log.info(String.format("Synchronization repository: %s completed", repository.getRepositoryUrl()));
        } catch (IOException | GitAPIException e) {
            log.error("Ошибка при синхронизации с репозиторием: {}", repository.getRepositoryUrl(), e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, String.format("Unable to synchronize repository id: %d", repository.getId()));
        }
    }

    private void validateRepository(String gitUrl, String branchName) {
        log.info("Checking access to repository: {}, branch: {}", gitUrl, branchName);
        String branch = (branchName == null || branchName.trim().isEmpty()) ? "main" : branchName.trim();
        try {
            LsRemoteCommand lsRemoteCommand = Git.lsRemoteRepository()
                    .setRemote(gitUrl)
                    .setHeads(true);
            Collection<Ref> refs = lsRemoteCommand.call();
            if (!branch.equals("main") && !branch.equals("master")) {
                boolean branchExists = refs.stream()
                        .anyMatch(ref -> ref.getName().equals("refs/heads/" + branch));

                if (!branchExists) {
                    log.warn("Branch '{}' not found in repository {}", branch, gitUrl);
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            String.format("Branch '%s' not found in repository", branch)
                    );
                }
            }
            log.info("Repository available: {}, branch: {}", gitUrl, branch);
        } catch (GitAPIException e) {
            log.error("Error Git while checking repository: {}", gitUrl, e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Repository unavailable");
        }
    }
}
