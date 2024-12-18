package org.devops.services.equipe.events;

import org.devops.event.Event;

public class NouvelleEquipeCreeEvent implements Event {
    private final String name;

    public NouvelleEquipeCreeEvent(String name) {
        this.name = name;
    }

    @Override
    public String getType() {
        return "NouvelleEquipeCree";
    }

    public String getEquipeNom() {
        return this.name;
    }
}
