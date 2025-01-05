package org.devops;

import org.devops.controller.TournoiController;
import org.devops.event.EventBus;
import org.devops.services.equipe.EquipeService;
import org.devops.services.match.MatchService;
import org.devops.services.notification.NotificationService;

public class Main {
    public static void main(String[] args) {
        EventBus eventBus = EventBus.getInstance();

        EquipeService teamService = new EquipeService(eventBus);
        MatchService matchService = new MatchService(eventBus);
        NotificationService notificationService = new NotificationService(eventBus);

        teamService.start(5000);
        matchService.start(5001);
        notificationService.start();

        new TournoiController().run();
    }
}
