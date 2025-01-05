package org.devops.services.equipe;

public class Equipe {
    private String nom;
    private int points;

    private Equipe(Builder builder) {
        this.nom = builder.nom;
        this.points = builder.points;
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

    public static class Builder {
        private String nom;
        private int points = 0;

        public Builder setNom(String nom) {
            this.nom = nom;
            return this;
        }

        public Builder setPoints(int points) {
            this.points = points;
            return this;
        }

        public Equipe build() {
            if (nom == null || nom.isEmpty()) {
                throw new IllegalArgumentException("Le nom de l'équipe est obligatoire !");
            }
            return new Equipe(this);
        }
    }
}

