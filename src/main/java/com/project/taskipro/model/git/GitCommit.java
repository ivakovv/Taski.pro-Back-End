package com.project.taskipro.model.git;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "git_commit")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GitCommit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "commit_hash", nullable = false)
    private String commitHash;

    @Column(name = "short_message")
    private String shortMessage;

    @Column(name = "full_message", columnDefinition = "TEXT")
    private String fullMessage;

    @Column(name = "author_name")
    private String authorName;

    @Column(name = "author_email")
    private String authorEmail;

    @Column(name = "commit_date")
    private LocalDateTime commitDate;

    @ManyToOne
    @JoinColumn(name = "repository_id", nullable = false)
    private GitRepository repository;

    @Transient
    public String getCommitUrl(){
        return String.format("%s/commit/%s", repository.getRepositoryUrl(), commitHash);
    }

}
