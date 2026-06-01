package fr.urca.covourca.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lignes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ligne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @OneToMany(mappedBy = "ligne", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("ordrePassage ASC")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<PointRendezVous> points = new ArrayList<>();

    @ManyToMany(mappedBy = "lignes", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Utilisateur> conducteurs = new ArrayList<>();
}
