package edu.cis.sensational.Controller.SharedGames;

/**
 * This class identifies the attributes of a trophy
 */
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

    /**
     * Sets the trophy name
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Gets the trophy name
     * @return String
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the smiley faces
     * @param smileyFaces
     */
    public void setSmileyFaces(int smileyFaces)
    {
        this.smileyFaces = smileyFaces;
    }

    /**
     * Gets the smiley faces
     * @return int
     */
    public int getSmileyFaces()
    {
        return smileyFaces;
    }

    /**
     * Sets the image ID
     * @param imageID
     */
    public void setImageID(int imageID)
    {
        this.imageID = imageID;
    }

    /**
     * Gets the image ID
     * @return int
     */
    public int getImageID()
    {
        return imageID;
    }

    /**
     * Sets the next trophy
     * @param nextTrophy
     */
    public void setNextTrophy(Trophy nextTrophy)
    {
        this.nextTrophy = nextTrophy;
    }

    /**
     * Gets the next Trophy
     * @return Trophy
     */
    public Trophy getNextTrophy()
    {
        return nextTrophy;
    }
}

