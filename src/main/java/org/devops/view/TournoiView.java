package org.devops.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;

public class TournoiView {
    private final Scanner scanner = new Scanner(System.in);

    public int afficherMenu() {
        System.out.println("=== Gestionnaire de Tournoi ===");
        System.out.println("1. Ajouter une équipe");
        System.out.println("2. Organiser un match");
        System.out.println("3. Supprimer une équipe");
        System.out.println("4. Afficher le classement");
        System.out.println("5. Quitter");
        System.out.print("Choix : ");
        int choix = scanner.nextInt();
        scanner.nextLine();
        return choix;
    }

    public String saisirNomEquipe() {
        System.out.print("Nom de l'équipe : ");
        return scanner.nextLine();
    }

    public String saisirNomEquipe(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    public int saisirScore(String teamName) {
        System.out.print("Score de " + teamName + " : ");
        int score = scanner.nextInt();
        scanner.nextLine();
        return score;
    }

    public void afficherMessage(String message) {
        System.out.println(message);
    }

    public void afficherClassement(BufferedReader reader) {
        System.out.println("=== Classement des équipes ===");
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("END")) {
                    break;
                }
                System.out.println(line);
            }
        } catch (IOException e) {
            afficherErreur("Erreur lors de l'affichage du classement : " + e.getMessage());
        }
    }

    public void afficherErreur(String message) {
        System.err.println("[Erreur] " + message);
    }
}
