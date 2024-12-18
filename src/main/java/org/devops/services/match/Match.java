package org.devops.services.match;

public class Match {
    private String equipe1;
    private String equipe2;

    public Match(String equipe1, String equipe2) {
        this.equipe1 = equipe1;
        this.equipe2 = equipe2;
    }

    public String getEquipe1() {
        return equipe1;
    }

    public String getEquipe2() {
        return equipe2;
    }
}