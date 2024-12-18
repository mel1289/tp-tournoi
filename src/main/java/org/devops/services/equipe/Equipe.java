package org.devops.services.equipe;

public class Equipe {
    private String nom;
    private int points;

    public Equipe(String nom) {
        this.nom = nom;
        this.points = 0;
    }

    public String getNom() {
        return nom;
    }

    public int getPoints() {
        return points;
    }

    public void ajouterPoints(int points) {
        this.points += points;
    }

    @Override
    public String toString() {
        return "Équipe : " + nom + ", Points : " + points;
    }
}
