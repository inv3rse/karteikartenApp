package de.mfgd_karteikarten.mfgd_karteikarten.base;

import de.mfgd_karteikarten.mfgd_karteikarten.data.Card;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Topic;
import io.realm.Realm;

public class DeckEditor {
    private Realm realm;
    private Deck deck;
    private int nextID;

    public DeckEditor(Realm realm, Topic topic, Deck deck)
    {
        this.realm = realm;

        Topic realmTopic = TopicManager.getTopic(realm, topic.getID());
        this.deck = TopicEditor.getDeck(realmTopic, deck.getID());

        nextID = this.deck.getCards().where().max("ID").intValue() + 1;
    }

    /**
     * Gibt die Card mit id zurück
     *
     * @param id ID von Card
     * @return einfaches Card Objekt (kein Realm Objekt)
     */
    public Card getCard(int id)
    {
        Card realmObject = deck.getCards().where().equalTo("ID", id).findFirst();
        if (realmObject != null)
        {
            return realm.copyFromRealm(realmObject);
        }

        return null;
    }

    /**
     * Fügt eine neues Card hinzu. ID wird gesetzt.
     *
     * @param card neue Card
     * @return Card mit gesetzter ID (kein Realm Objekt)
     */
    public Card addCard(Card card)
    {
        card.setId(nextID);
        nextID += 1;

        realm.beginTransaction();
        deck.getCards().add(card);
        realm.commitTransaction();

        return card;
    }

    /**
     * Verändert die Card mit ID in der Datenbank
     *
     * @param card Die zu verändernde Card, dessen ID bereits gesetzt ist
     */
    public void setCard(Card card)
    {
        realm.beginTransaction();
        Card oldCard = deck.getCards().where().equalTo("ID", card.getId()).findFirst();
        oldCard.setAnswer(card.getAnswer());
        oldCard.setQuestion(card.getQuestion());
        oldCard.setRating(card.getRating());
        realm.commitTransaction();
    }

    /**
     * Entfernd die Card aus der DB
     * @param card Card mit ID die entfernt wird
     */
    public void removeCard(Card card)
    {
        realm.beginTransaction();
        Card realmObject = deck.getCards().where().equalTo("ID", card.getId()).findFirst();
        if (realmObject != null) {
            realmObject.removeFromRealm();
        }
        realm.commitTransaction();
        deck.getCards().remove(card);
    }
}
