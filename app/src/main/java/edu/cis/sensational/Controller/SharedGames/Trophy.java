package edu.cis.sensational.Controller.SharedGames;

public class Trophy
{
    int smileyFaces;
    String name;

    public Trophy(int smileyFaces, String name){
        this.smileyFaces = smileyFaces;
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
}

