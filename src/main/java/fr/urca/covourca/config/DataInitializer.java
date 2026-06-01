package fr.urca.covourca.config;

import fr.urca.covourca.model.Ligne;
import fr.urca.covourca.model.PointRendezVous;
import fr.urca.covourca.repository.LigneRepository;
import fr.urca.covourca.repository.PointRendezVousRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final LigneRepository ligneRepository;
    private final PointRendezVousRepository pointRepository;

    @Override
    public void run(String... args) {
        if (ligneRepository.existsByNom("Ligne 1 – Campus vers Gare")) return;

        Ligne ligne1 = ligneRepository.save(Ligne.builder()
            .nom("Ligne 1 – Campus vers Gare")
            .build());

        pointRepository.save(PointRendezVous.builder()
            .ligne(ligne1).nomLieu("Parking BU").latitude(49.2315).longitude(4.0321).ordrePassage(1).build());
        pointRepository.save(PointRendezVous.builder()
            .ligne(ligne1).nomLieu("Entrée Campus").latitude(49.2330).longitude(4.0345).ordrePassage(2).build());
        pointRepository.save(PointRendezVous.builder()
            .ligne(ligne1).nomLieu("Gare SNCF Reims").latitude(49.2595).longitude(4.0318).ordrePassage(3).build());

        Ligne ligne2 = ligneRepository.save(Ligne.builder()
            .nom("Ligne 2 – Campus vers Centre-Ville")
            .build());

        pointRepository.save(PointRendezVous.builder()
            .ligne(ligne2).nomLieu("Parking BU").latitude(49.2315).longitude(4.0321).ordrePassage(1).build());
        pointRepository.save(PointRendezVous.builder()
            .ligne(ligne2).nomLieu("Faculté des Sciences").latitude(49.2340).longitude(4.0360).ordrePassage(2).build());
        pointRepository.save(PointRendezVous.builder()
            .ligne(ligne2).nomLieu("Place d'Erlon").latitude(49.2557).longitude(4.0334).ordrePassage(3).build());

        Ligne ligne3 = ligneRepository.save(Ligne.builder()
            .nom("Ligne 3 – Campus vers Tinqueux")
            .build());

        pointRepository.save(PointRendezVous.builder()
            .ligne(ligne3).nomLieu("Entrée Campus").latitude(49.2330).longitude(4.0345).ordrePassage(1).build());
        pointRepository.save(PointRendezVous.builder()
            .ligne(ligne3).nomLieu("Croix du Sud").latitude(49.2400).longitude(4.0100).ordrePassage(2).build());
        pointRepository.save(PointRendezVous.builder()
            .ligne(ligne3).nomLieu("Mairie de Tinqueux").latitude(49.2450).longitude(3.9950).ordrePassage(3).build());
    }
}
