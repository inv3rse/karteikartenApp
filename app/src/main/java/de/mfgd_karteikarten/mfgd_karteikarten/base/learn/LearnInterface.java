package de.mfgd_karteikarten.mfgd_karteikarten.base.learn;


import de.mfgd_karteikarten.mfgd_karteikarten.data.Card;

public interface LearnInterface {

    boolean hasNextCard();
    Card getNextCard();
    Card shuffleCard();

    void gradeCurrentCard(boolean positive);
}
