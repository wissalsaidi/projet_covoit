package fr.urca.covourca.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage {
    private Long demandeId;
    private String nomPassager;
    private String pointDepart;
    private String pointArrivee;
    private Double notePassager;
}
