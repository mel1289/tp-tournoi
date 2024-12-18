package org.devops.services.notification;

import org.devops.event.EventBus;

public class NotificationService {
    private final EventBus eventBus;

    public NotificationService(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void start() {
        eventBus.subscribe("NOUVELLE_EQUIPE_CREE", (eventType, eventData) -> {
            System.out.println("[NOTIFICATION] Nouvelle équipe créée : " + eventData);
        });

        eventBus.subscribe("MATCH_TERMINE", (eventType, eventData) -> {
            String[] parts = eventData.split(":");
            String team1 = parts[0];
            String team2 = parts[1];

            int score1 = Integer.parseInt(parts[2]);
            int score2 = Integer.parseInt(parts[3]);

            if (score1 == score2) {
                System.out.println("[NOTIFICATION] Le match est terminé, match null");
            } else {
                System.out.println("[NOTIFICATION] Le match est terminé, " + (score1 > score2 ? team1 : team2) + " a gagné");
            }
        });

        eventBus.subscribe("MATCH_ORGANISE", (eventType, eventData) -> {
            System.out.println("[NOTIFICATION] " + eventData);
        });

        eventBus.subscribe("EQUIPE_SUPPRIMEE", (eventType, eventData) -> {
            System.out.println("[Notification] L'équipe " + eventData + " se retire du tournoi");
        });
    }
}
