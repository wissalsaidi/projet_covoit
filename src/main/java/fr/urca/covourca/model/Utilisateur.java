package fr.urca.covourca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "utilisateurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nom;

    @NotBlank
    @Column(nullable = false)
    private String prenom;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;

    @Column(name = "est_conducteur")
    @Builder.Default
    private boolean estConducteur = false;

    @Column(name = "telephone")
    @Pattern(regexp = "^[+]?[0-9\\s\\-\\(\\)]{7,20}$", message = "Numéro de téléphone invalide (ex: +33 6 12 34 56 78 ou 0612345678)")
    private String telephone;

    @Column(name = "note_moyenne")
    private Double noteMoyenne;

    @OneToMany(mappedBy = "proprietaire", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Vehicule> vehicules = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "conducteur_ligne",
        joinColumns = @JoinColumn(name = "conducteur_id"),
        inverseJoinColumns = @JoinColumn(name = "ligne_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Ligne> lignes = new ArrayList<>();
}
