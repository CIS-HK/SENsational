package edu.cis.sensational.Controller.SharedGames;

public class Trophy {
    int smileyFaces;
    int trophyImage;
    String name;

    public Trophy(int smileyFaces, int trophyImage, String name){
        this.smileyFaces = smileyFaces;
        this.trophyImage = trophyImage;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSmileyFaces(int smileyFaces) {
        this.smileyFaces = smileyFaces;
    }

    public int getSmileyFaces() {
        return smileyFaces;
    }

    public void setTrophyImage(int trophyImage) {
        this.trophyImage = trophyImage;
    }

    public int getTrophyImage() {
        return trophyImage;
    }
}
