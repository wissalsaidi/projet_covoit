package fr.urca.covourca.controller;

import fr.urca.covourca.model.PointRendezVous;
import fr.urca.covourca.repository.PointRendezVousRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final PointRendezVousRepository pointRepository;

    @GetMapping("/lignes/{id}/points")
    public ResponseEntity<List<Map<String, Object>>> getPoints(@PathVariable Long id) {
        List<PointRendezVous> points = pointRepository.findByLigneIdOrderByOrdrePassage(id);
        List<Map<String, Object>> result = points.stream().map(p -> Map.<String, Object>of(
            "id", p.getId(),
            "nom", p.getNomLieu(),
            "lat", p.getLatitude(),
            "lng", p.getLongitude(),
            "ordre", p.getOrdrePassage()
        )).toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/points")
    public ResponseEntity<List<Map<String, Object>>> getAllPoints() {
        List<PointRendezVous> points = pointRepository.findAllWithLigne();
        List<Map<String, Object>> result = points.stream().map(p -> {
            Map<String, Object> m = new java.util.LinkedHashMap<>();
            m.put("id",       p.getId());
            m.put("nom",      p.getNomLieu());
            m.put("lat",      p.getLatitude());
            m.put("lng",      p.getLongitude());
            m.put("ordre",    p.getOrdrePassage());
            m.put("ligneId",  p.getLigne().getId());
            m.put("ligneNom", p.getLigne().getNom());
            return m;
        }).toList();
        return ResponseEntity.ok(result);
    }
}
