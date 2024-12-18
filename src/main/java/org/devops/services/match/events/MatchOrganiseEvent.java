package org.devops.services.match.events;

import org.devops.event.Event;

public class MatchOrganiseEvent implements Event {
    private final String equipe1;
    private final String equipe2;

    public MatchOrganiseEvent(String equipe1, String equipe2) {
        this.equipe1 = equipe1;
        this.equipe2 = equipe2;
    }

    @Override
    public String getType() {
        return "MatchOrganise";
    }

    public String getEquipe1() {
        return equipe1;
    }

    public String getEquipe2() {
        return equipe2;
    }
}
