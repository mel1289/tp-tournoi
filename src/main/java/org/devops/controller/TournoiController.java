package org.devops.controller;

import org.devops.view.TournoiView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TournoiController {
    public void run() {
        TournoiView tournoiView = new TournoiView();

        try (Socket teamSocket = new Socket("localhost", 5000);
             PrintWriter teamWriter = new PrintWriter(teamSocket.getOutputStream(), true);
             BufferedReader teamReader = new BufferedReader(new InputStreamReader(teamSocket.getInputStream()));
             Socket matchSocket = new Socket("localhost", 5001);
             PrintWriter matchWriter = new PrintWriter(matchSocket.getOutputStream(), true);
             BufferedReader matchReader = new BufferedReader(new InputStreamReader(matchSocket.getInputStream()))) {

            boolean running = true;
            while (running) {
                int choice = tournoiView.afficherMenu();

                switch (choice) {
                    case 1:
                        String teamName = tournoiView.saisirNomEquipe();
                        teamWriter.println("ADD_TEAM:" + teamName);
                        tournoiView.afficherMessage(handleResponse(teamReader));
                        break;

                    case 2:
                        String team1 = tournoiView.saisirNomEquipe("Nom de la première équipe : ");
                        String team2 = tournoiView.saisirNomEquipe("Nom de la deuxième équipe : ");
                        int score1 = tournoiView.saisirScore(team1);
                        int score2 = tournoiView.saisirScore(team2);

                        matchWriter.println("ORGANIZE_MATCH:" + team1 + ":" + team2 + ":" + score1 + ":" + score2);
                        tournoiView.afficherMessage(handleResponse(matchReader));
                        break;


                    case 3:
                        String teamToRemove = tournoiView.saisirNomEquipe();
                        teamWriter.println("REMOVE_TEAM:" + teamToRemove);
                        tournoiView.afficherMessage(handleResponse(teamReader));
                        break;

                    case 4:
                        teamWriter.println("GET_CLASSEMENT");
                        tournoiView.afficherClassement(teamReader);
                        break;

                    case 5:
                        running = false;
                        tournoiView.afficherMessage("Fermeture de l'application...");
                        break;

                    default:
                        tournoiView.afficherErreur("Commande inconnue.");
                }
            }
        } catch (Exception e) {
            tournoiView.afficherErreur("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String handleResponse(BufferedReader reader) {
        try {
            String response = reader.readLine();
            if (response != null) {
                if (response.startsWith("ACK")) {
                    return "[INFO] " + response;
                } else if (response.startsWith("ERREUR")) {
                    return "[ERREUR] " + response;
                } else {
                    return "[UNKNOWN] " + response;
                }
            }
        } catch (Exception e) {
            return "Erreur lors de la lecture de la réponse : " + e.getMessage();
        }
        return null;
    }
}
