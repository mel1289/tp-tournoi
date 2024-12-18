package org.devops.services.equipe;

import com.google.gson.Gson;
import org.devops.event.EventBus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EquipeService {
    private final EventBus eventBus;
    private final List<Equipe> teams = new ArrayList<>();
    private final Gson gson = new Gson();

    public EquipeService(EventBus eventBus) {
        this.eventBus = eventBus;

        eventBus.subscribe("GET_TEAMS", (eventType, eventData) -> {
            List<String> teamNames = teams.stream()
                    .map(Equipe::getNom)
                    .collect(Collectors.toList());
            String jsonTeamNames = gson.toJson(teamNames);
            eventBus.publish("TEAMS_LIST", jsonTeamNames);
        });

        eventBus.subscribe("MATCH_TERMINE", (eventType, eventData) -> {
            String[] parts = eventData.split(":");
            String team1 = parts[0];
            String team2 = parts[1];
            int score1 = Integer.parseInt(parts[2]);
            int score2 = Integer.parseInt(parts[3]);

            if (score1 == score2) {
                updateTeamPoints(team1, 1);
                updateTeamPoints(team2, 1);
            } else {
                String winner = score1 > score2 ? team1 : team2;
                int points = Math.abs(score1 - score2);
                updateTeamPoints(winner, points);
            }
        });
    }

    public void start(int port) {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("EquipeService started on port " + port);

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
                if (command.startsWith("ADD_TEAM")) {
                    String teamName = command.split(":")[1];
                    teams.add(new Equipe(teamName));
                    eventBus.publish("NOUVELLE_EQUIPE_CREE", teamName);
                    writer.println("ACK: Team " + teamName + " added.");
                } else if (command.startsWith("REMOVE_TEAM")) {
                    String teamName = command.split(":")[1];
                    boolean removed = teams.removeIf(team -> team.getNom().equals(teamName));
                    if (removed) {
                        eventBus.publish("EQUIPE_SUPPRIMEE", teamName);
                        writer.println("ACK: Team " + teamName + " removed.");
                    } else {
                        writer.println("ERREUR: Team " + teamName + " not found.");
                    }
                } else if (command.startsWith("GET_CLASSEMENT")) {
                    String classement = teams.stream()
                            .sorted((a, b) -> Integer.compare(b.getPoints(), a.getPoints()))
                            .map(Equipe::toString)
                            .collect(Collectors.joining("\n"));
                    String[] lignesClassement = classement.split("\n");

                    for (String ligne : lignesClassement) {
                        writer.println(ligne);
                    }
                    writer.println("END");
                } else {
                    writer.println("ERREUR: Commande inconnue.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTeamPoints(String teamName, int points) {
        Optional<Equipe> teamOpt = teams.stream()
                .filter(team -> team.getNom().equals(teamName))
                .findFirst();

        if (teamOpt.isPresent()) {
            teamOpt.get().ajouterPoints(points);
        } else {
            System.out.println("Équipe introuvable : " + teamName);
        }
    }
}
