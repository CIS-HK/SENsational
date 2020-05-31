package edu.cis.sensational.Controller.SharedGames;

public class Trophy
{
    int smileyFaces;
    String name;
    int imageID;
    Trophy nextTrophy;

    public Trophy(int smileyFaces, String name, int imageID)
    {
        this.smileyFaces = smileyFaces;
        this.name = name;
        this.imageID = imageID;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setSmileyFaces(int smileyFaces)
    {
        this.smileyFaces = smileyFaces;
    }

    public int getSmileyFaces()
    {
        return smileyFaces;
    }

    public void setImageID(int imageID)
    {
        this.imageID = imageID;
    }

    public int getImageID()
    {
        return imageID;
    }

    public void setNextTrophy(Trophy nextTrophy)
    {
        this.nextTrophy = nextTrophy;
    }

    public Trophy getNextTrophy()
    {
        return nextTrophy;
    }
}

