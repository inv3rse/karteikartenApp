package de.mfgd_karteikarten.mfgd_karteikarten.data;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Topic extends RealmObject {
    @PrimaryKey
    private int ID;
    private String name;
    private RealmList<Deck> decks;

    public Topic() {
        decks = new RealmList<>();
    }

    public Topic(String name, RealmList<Deck> decks) {
        this.name = name;
        this.decks = decks;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public RealmList<Deck> getDecks() {
        return decks;
    }

    public void setDecks(RealmList<Deck> decks) {
        this.decks = decks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
