package org.devops.services.match.events;

import org.devops.event.Event;
import org.devops.services.equipe.Equipe;

public class MatchTermineEvent implements Event {
    private final Equipe equipeGagnante;
    private final Equipe equipePerdante;

    public MatchTermineEvent(Equipe equipeGagnante, Equipe equipePerdante) {
        this.equipeGagnante = equipeGagnante;
        this.equipePerdante = equipePerdante;
    }

    @Override
    public String getType() {
        return "MatchTermine";
    }

    public Equipe getEquipeGagnante() {
        return equipeGagnante;
    }

    public Equipe getEquipePerdante() {
        return equipePerdante;
    }
}