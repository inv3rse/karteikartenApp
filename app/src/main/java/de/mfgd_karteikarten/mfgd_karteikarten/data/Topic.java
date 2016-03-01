package de.mfgd_karteikarten.mfgd_karteikarten.data;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Topic extends RealmObject
{
    public static final int UNKNOWN_ID = -1;

    @PrimaryKey
    private int ID = UNKNOWN_ID;
    private String name;
    private RealmList<Deck> decks;

    public Topic()
    {
        name = "";
        decks = new RealmList<>();
    }

    public Topic(String name)
    {
        this.name = name;
        decks = new RealmList<>();
    }

    public int getID()
    {
        return ID;
    }

    public void setID(int ID)
    {
        this.ID = ID;
    }

    public RealmList<Deck> getDecks()
    {
        return decks;
    }

    public void setDecks(RealmList<Deck> decks)
    {
        this.decks = decks;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
