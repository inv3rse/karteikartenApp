package de.mfgd_karteikarten.mfgd_karteikarten.data;

import java.util.Collections;
import java.util.List;

public class Topic {
    private String title;
    private List<Deck> decks;

    public Topic(String title, List<Deck> decks)
    {
        this.title = title;
        this.decks = decks;
    }

    public String getTitle()
    {
        return title;
    }

    public List<Deck> getDecks()
    {
        return Collections.unmodifiableList(decks);
    }
}
