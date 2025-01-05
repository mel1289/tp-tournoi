package org.devops.services.match;

import com.google.gson.Gson;
import org.devops.event.EventBus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MatchService {
    private final EventBus eventBus;
    private List<String> availableTeams = new ArrayList<>();
    private final Gson gson = new Gson();

    public MatchService(EventBus eventBus) {
        this.eventBus = eventBus;

        eventBus.subscribe("TEAMS_LIST", (eventType, eventData) -> {
            try {
                String[] teamNamesArray = gson.fromJson(eventData, String[].class);
                availableTeams = Arrays.asList(teamNamesArray);
                System.out.println("Équipes disponibles : " + availableTeams);
            } catch (Exception e) {
                System.err.println("Erreur lors de la désérialisation de TEAMS_LIST : " + e.getMessage());
            }
        });
    }

    public void start(int port) {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("MatchService started on port " + port);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(() -> handleClient(clientSocket)).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handleClient(Socket socket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            String command;
            while ((command = reader.readLine()) != null) {
                if (command.startsWith("ORGANIZE_MATCH")) {
                    String[] parts = command.split(":");

                    if (parts.length < 5) {
                        writer.println("ERREUR: Commande mal formée.");
                        continue;
                    }

                    String team1 = parts[1];
                    String team2 = parts[2];

                    int score1 = Integer.parseInt(parts[3]);
                    int score2 = Integer.parseInt(parts[4]);

                    eventBus.publish("GET_TEAMS", "");
                    int retryCount = 0;
                    while ((availableTeams == null || availableTeams.isEmpty()) && retryCount < 10) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        retryCount++;
                    }

                    if (!availableTeams.contains(team1) || !availableTeams.contains(team2)) {
                        writer.println("ERREUR: Une ou plusieurs équipes n'existent pas.");
                        continue;
                    }

                    eventBus.publish("MATCH_ORGANISE", "Un match est organisé entre " + team1 + " et " + team2);
                    writer.println("ACK: Match organisé entre " + team1 + " et " + team2);

                    eventBus.publish("MATCH_TERMINE", team1 + ":" + team2 + ":" + score1 + ":" + score2);
                    writer.println("ACK: Match terminé.");
                } else {
                    writer.println("ERREUR: Commande inconnue.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
