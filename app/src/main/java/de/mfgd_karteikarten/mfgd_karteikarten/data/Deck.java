package de.mfgd_karteikarten.mfgd_karteikarten.data;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Deck extends RealmObject {
    public static final int UNKNOWN_ID = -1;

    @PrimaryKey
    private int ID = UNKNOWN_ID;
    private String name;
    private RealmList<Card> cards;

    public Deck()
    {
        name = "";
        cards = new RealmList<>();
    }
    public Deck(String name)
    {
        this.name = name;
        cards = new RealmList<>();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public RealmList<Card> getCards() {
        return cards;
    }

    public void setCards(RealmList<Card> cards) {
        this.cards = cards;
    }
}
