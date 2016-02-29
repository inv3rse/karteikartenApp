package de.mfgd_karteikarten.mfgd_karteikarten.base;

import java.util.List;

import javax.inject.Inject;

import de.mfgd_karteikarten.mfgd_karteikarten.data.Card;

@ActivityScope
public class LearnAssistant {

    @Inject
    public LearnAssistant(List<Card> cards)
    {

    }

    public boolean hasNextCard()
    {
        return true;
    }

    public Card getNextCard()
    {
        return new Card();
    }

    public void gradePositive()
    {

    }

    public void bewerteNegativ()
    {

    }
}
