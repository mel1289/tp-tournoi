package org.devops.services.equipe.events;

import org.devops.event.Event;

public class ObtenirEquipesEvent implements Event {
    public ObtenirEquipesEvent() {
    }

    @Override
    public String getType() {
        return "ObtenirEquipesEvent";
    }
}
