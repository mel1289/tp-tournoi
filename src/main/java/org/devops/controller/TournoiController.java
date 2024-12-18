package org.devops.controller;

import org.devops.view.TournoiView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TournoiController {
    public void run() {
        Scanner scanner = new Scanner(System.in);
        TournoiView tournoiView = new TournoiView();

        try (Socket teamSocket = new Socket("localhost", 5000);
             PrintWriter teamWriter = new PrintWriter(teamSocket.getOutputStream(), true);
             BufferedReader teamReader = new BufferedReader(new InputStreamReader(teamSocket.getInputStream()));
             Socket matchSocket = new Socket("localhost", 5001);
             PrintWriter matchWriter = new PrintWriter(matchSocket.getOutputStream(), true);
             BufferedReader matchReader = new BufferedReader(new InputStreamReader(matchSocket.getInputStream()));
        ) {

            boolean running = true;
            while (running) {
                System.out.println("=== Gestionnaire de Tournoi ===");
                System.out.println("1. Ajouter une équipe");
                System.out.println("2. Organiser un match");
                System.out.println("3. Supprimer une équipe");
                System.out.println("4. Afficher le classement");
                System.out.println("5. Quitter");
                System.out.print("Choix : ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1": // Ajouter une équipe
                        System.out.print("Nom de l'équipe : ");
                        String teamName = scanner.nextLine();
                        teamWriter.println("ADD_TEAM:" + teamName);
                        handleResponse(teamReader);
                        break;

                    case "2": // Organiser un match
                        System.out.print("Nom de la première équipe : ");
                        String team1 = scanner.nextLine();
                        System.out.print("Nom de la deuxième équipe : ");
                        String team2 = scanner.nextLine();
                        System.out.print("Score de " + team1 + " : ");
                        int score1 = Integer.parseInt(scanner.nextLine());
                        System.out.print("Score de " + team2 + " : ");
                        int score2 = Integer.parseInt(scanner.nextLine());
                        matchWriter.println("ORGANIZE_MATCH:" + team1 + ":" + team2 + ":" + score1 + ":" + score2);
                        handleResponse(matchReader);
                        break;

                    case "3": // Supprimer une équipe
                        System.out.print("Nom de l'équipe à supprimer : ");
                        String teamToRemove = scanner.nextLine();
                        teamWriter.println("REMOVE_TEAM:" + teamToRemove);
                        handleResponse(teamReader);
                        break;

                    case "4": // Afficher le classement
                        teamWriter.println("GET_CLASSEMENT");
                        System.out.println("=== Classement des équipes ===");
                        handleMultiLineResponse(teamReader);
                        break;

                    case "5": // Quitter
                        running = false;
                        break;

                    default:
                        System.out.println("Commande inconnue.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleResponse(BufferedReader reader) {
        try {
            String response = reader.readLine();
            if (response != null) {
                if (response.startsWith("ACK")) {
                    System.out.println("[INFO] " + response);
                } else if (response.startsWith("ERREUR")) {
                    System.err.println("[ERREUR] " + response);
                } else {
                    System.out.println("[UNKNOWN] " + response);
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la lecture de la réponse : " + e.getMessage());
        }
    }

    private void handleMultiLineResponse(BufferedReader reader) {
        try {
            String response;
            while ((response = reader.readLine()) != null) {
                if (response.equals("END")) {
                    break;
                } else {
                    System.out.println(response);
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la lecture des données multi-lignes : " + e.getMessage());
        }
    }
}
