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
    @JoinColumn(name = "point_depart_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private PointRendezVous pointDepart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_arrivee_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private PointRendezVous pointArrivee;

    @Column(name = "depart_adresse")
    private String departAdresse;

    @Column(name = "depart_latitude")
    private Double departLatitude;

    @Column(name = "depart_longitude")
    private Double departLongitude;

    @Column(name = "arrivee_adresse")
    private String arriveeAdresse;

    @Column(name = "arrivee_latitude")
    private Double arriveeLatitude;

    @Column(name = "arrivee_longitude")
    private Double arriveeLongitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatutDemande statut = StatutDemande.EN_ATTENTE;

    @Column(name = "date_heure", nullable = false)
    @Builder.Default
    private LocalDateTime dateHeure = LocalDateTime.now();

    @Version
    @Column(name = "version")
    @Builder.Default
    private Long version = 0L;

    @OneToOne(mappedBy = "demande", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Trajet trajet;

    public String getDepartDisplay() {
        return pointDepart != null ? pointDepart.getNomLieu() : departAdresse;
    }

    public String getArriveeDisplay() {
        return pointArrivee != null ? pointArrivee.getNomLieu() : arriveeAdresse;
    }

    public Double getDepartLat() {
        return pointDepart != null ? pointDepart.getLatitude() : departLatitude;
    }

    public Double getDepartLng() {
        return pointDepart != null ? pointDepart.getLongitude() : departLongitude;
    }

    public Double getArriveeLat() {
        return pointArrivee != null ? pointArrivee.getLatitude() : arriveeLatitude;
    }

    public Double getArriveeLng() {
        return pointArrivee != null ? pointArrivee.getLongitude() : arriveeLongitude;
    }
}
