package fr.epita.backend.data.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "games")
@Getter
@Setter
@NoArgsConstructor
public class GameModel {

    @Id
    @UuidGenerator
    @Column(columnDefinition = "uuid", nullable = false, updatable = false)
    private UUID uuid;

    @Column(name = "subject_game_name", nullable = false, unique = true)
    private String subjectGameName;

    @OneToOne(fetch = FetchType.LAZY)
    private GameArticleVersionModel currentVersion;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameArticleVersionModel> versions = new ArrayList<>();
}
