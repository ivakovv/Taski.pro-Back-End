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
import com.project.taskipro.repository.GitRepositoryRepository;
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
    private final GitRepositoryRepository gitRepositoryRepository;
    private final GitCommitRepository gitCommitRepository;
    private final MapperToGitRepository mapperToGitRepository;
    private final MapperToGitRepositoryResponseDto mapperToGitRepositoryResponseDto;
    private final MapperToGitCommitResponseDto mapperToGitCommitResponseDto;
    private final MapperToGitCommit mapperToGitCommit;

    public GitRepositoryResponseDto addRepositoryOnDesk(Long deskId, AddGitRepositoryDto request){
        Desks desk = deskService.getDeskById(deskId);
        if (gitRepositoryRepository.findByRepositoryUrlAndDesk(request.repositoryUrl(), desk).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Репозиторий URL: %s уже добавлен к доске!", request.repositoryUrl()));
        }
        validateRepository(request.repositoryUrl(), request.branchName());
        GitRepository gitRepository = mapperToGitRepository.mapToGitRepository(request, desk);
        gitRepositoryRepository.save(gitRepository);
        syncCommits(gitRepository);
        return mapperToGitRepositoryResponseDto.mapToGitRepositoryResponseDto(gitRepository);
    }

    public void removeRepositoryFromDesk(Long repositoryId, Long deskId){
        Desks desk = deskService.getDeskById(deskId);
        GitRepository repository = gitRepositoryRepository.findByIdAndDesk(repositoryId, desk).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Репозитория с id: %d, нет на доске с id %d", repositoryId, deskId)));
        gitRepositoryRepository.delete(repository);
    }

    public List<GitRepositoryResponseDto> getRepositoriesByDeskId(Long deskId) {
        List<GitRepository> repositories = gitRepositoryRepository.findByDeskId(deskId);
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
        GitRepository repository = gitRepositoryRepository.findById(repositoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Репозиторий с id: %d не найден", repositoryId)));
        syncCommits(repository);
    }

    @Scheduled(fixedRate = 600000, initialDelay = 600)
    private void syncAllRepositories(){
        log.info("Запуск синхронизации всех репозиториев");
        List<GitRepository> repositories = gitRepositoryRepository.findAll();
        for (GitRepository repository : repositories){
            syncCommits(repository);
        }
        log.info("Синхронизация всех репозиториев завершена");
    }

    public void syncCommits(GitRepository repository){
        try (TempDirectory tempDir = new TempDirectory(String.format("git-temp-%d", repository.getId()))) {
            log.info(String.format("Начало синхронизации репозитория %s", repository.getRepositoryUrl()));
            Git git = Git.cloneRepository()
                    .setURI(repository.getRepositoryUrl())
                    .setDirectory(tempDir.toFile())
                    .setBranch(repository.getBranchName())
                    .setNoTags()
                    .setNoCheckout(true)
                    .call();
            log.info(String.format("Клонирование репозитория %s выполнено", repository.getRepositoryUrl()));
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
            gitRepositoryRepository.save(repository);
            git.close();
            log.info(String.format("Синхронизация репозитория %s выполнена", repository.getRepositoryUrl()));
        } catch (IOException | GitAPIException e) {
            log.error("Ошибка при синхронизации с репозиторием: {}", repository.getRepositoryUrl(), e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, String.format("Не удалось синхронизировать репозиторий с id: %d", repository.getId()));
        }
    }

    private void validateRepository(String gitUrl, String branchName) {
        log.info("Проверка доступности репозитория: {}, ветка: {}", gitUrl, branchName);
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
                    log.warn("Ветка '{}' не найдена в репозитории {}", branch, gitUrl);
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            String.format("Ветка '%s' не найдена в репозитории", branch)
                    );
                }
            }
            log.info("Репозиторий доступен: {}, ветка: {}", gitUrl, branch);
        } catch (GitAPIException e) {
            log.error("Ошибка Git при проверке репозитория: {}", gitUrl, e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Репозиторий не доступен");
        }
    }
}
