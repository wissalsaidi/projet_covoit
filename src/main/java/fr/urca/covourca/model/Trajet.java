package fr.urca.covourca.model;

import fr.urca.covourca.model.enums.StatutTrajet;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trajets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trajet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conducteur_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Utilisateur conducteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passager_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Utilisateur passager;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demande_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private DemandeCovoiturage demande;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicule_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Vehicule vehicule;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatutTrajet statut = StatutTrajet.EN_COURS;

    @Column(name = "identifiant_echange", length = 4)
    private String identifiantEchange;

    @OneToMany(mappedBy = "trajet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Notation> notations = new ArrayList<>();
}
