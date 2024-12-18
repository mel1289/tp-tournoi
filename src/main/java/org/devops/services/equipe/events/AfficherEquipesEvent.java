package org.devops.services.equipe.events;

import org.devops.event.Event;
import org.devops.services.equipe.Equipe;

import java.util.List;

public class AfficherEquipesEvent implements Event {
    private final List<Equipe> equipes;

    public AfficherEquipesEvent(List<Equipe> equipes) {
        this.equipes = equipes;
    }

    @Override
    public String getType() {
        return "EquipesAffichees";
    }

    public List<Equipe> getEquipes() {
        return equipes;
    }
}
