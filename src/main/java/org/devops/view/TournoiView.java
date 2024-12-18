package org.devops.view;

import org.devops.event.Event;
import org.devops.services.equipe.events.AfficherEquipesEvent;
import org.devops.services.equipe.Equipe;

import java.util.List;
import java.util.Scanner;

public class TournoiView {
    private final Scanner scanner = new Scanner(System.in);

    public int afficherMenu() {
        System.out.println("=== Gestionnaire de Tournoi ===");
        System.out.println("1. Ajouter une équipe");
        System.out.println("2. Afficher les équipes");
        System.out.println("3. Organiser un match");
        System.out.println("4. Quitter");
        System.out.print("Choix : ");
        return scanner.nextInt();
    }

    private void afficherEquipes(Event event) {
        if (event instanceof AfficherEquipesEvent equipesEvent) {
            List<Equipe> equipes = equipesEvent.getEquipes();
            System.out.println("=== Liste des Équipes ===");
            if (equipes.isEmpty()) {
                System.out.println("Aucune équipe n'a été créée pour l'instant.");
            } else {
                for (Equipe equipe : equipes) {
                    System.out.println("- " + equipe.getNom());
                }
            }
        }
    }

    public String saisirNomEquipe() {
        System.out.print("Nom de l'équipe : ");
        return scanner.next();
    }

    public void afficherMessage(String message) {
        System.out.println(message);
    }
}

