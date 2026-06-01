package fr.urca.covourca.model;

import fr.urca.covourca.model.enums.StatutDemande;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "demandes_covoiturage")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemandeCovoiturage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passager_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Utilisateur passager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_depart_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private PointRendezVous pointDepart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_arrivee_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private PointRendezVous pointArrivee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatutDemande statut = StatutDemande.EN_ATTENTE;

    @Column(name = "date_heure", nullable = false)
    @Builder.Default
    private LocalDateTime dateHeure = LocalDateTime.now();

    @OneToOne(mappedBy = "demande", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Trajet trajet;
}
