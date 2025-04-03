package com.project.taskipro.model.git;

import com.project.taskipro.model.audit.EntityAuditable;
import com.project.taskipro.model.desks.Desks;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "git_repositories")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GitRepository implements EntityAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "repository_url", nullable = false)
    private String repositoryUrl;

    @Column(name = "repository_name")
    private String repositoryName;

    @Column(name = "repository_description")
    private String repositoryDescription;

    @Column(name = "branch_name", nullable = false)
    private String branchName;

    @Column(name = "last_sync_date")
    private LocalDateTime lastSyncDate;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "desk_id", nullable = false)
    private Desks desk;

    @Override
    public void setCreationDate(LocalDateTime creationDate) {
        this.createDate = creationDate;
    }
}
