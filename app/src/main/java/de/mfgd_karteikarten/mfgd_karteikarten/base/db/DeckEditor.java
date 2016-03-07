package de.mfgd_karteikarten.mfgd_karteikarten.base.db;

import java.util.List;

import javax.inject.Inject;

import de.mfgd_karteikarten.mfgd_karteikarten.base.ActivityScope;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Card;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import io.realm.Realm;

@ActivityScope
public class DeckEditor
{
    private Realm realm;
    private Deck deck;
    private int nextID = 1;

    @Inject
    public DeckEditor(Realm realm, Deck deck)
    {
        this.realm = realm;
        this.deck = TopicEditor.getDeck(realm, deck.getID());

        Number maxId = realm.where(Card.class).max("ID");
        if (maxId != null)
        {
            nextID = maxId.intValue() + 1;
        }
    }

    public String getDeckName()
    {
        return deck.getName();
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
        card.setID(nextID);
        nextID += 1;

        realm.beginTransaction();
        deck.getCards().add(card);
        realm.commitTransaction();

        return card;
    }

    /**
     * Liefert die Liste aller Karten im Deck.
     * @return Liste aller Karten
     */
    public List<Card> getCards()
    {
        return realm.copyFromRealm(deck.getCards());
    }

    /**
     * Verändert die Card mit ID in der Datenbank
     *
     * @param card Die zu verändernde Card, dessen ID bereits gesetzt ist
     */
    public void setCard(Card card)
    {
        realm.beginTransaction();
        Card oldCard = deck.getCards().where().equalTo("ID", card.getID()).findFirst();
        oldCard.setAnswer(card.getAnswer());
        oldCard.setQuestion(card.getQuestion());
        oldCard.setRating(card.getRating());
        oldCard.setType(card.getType());
        oldCard.setFalseanswer(card.getFalseanswer());
        realm.commitTransaction();
    }

    /**
     * Entfernd die Card aus der DB
     *
     * @param card Card mit ID die entfernt wird
     */
    public void removeCard(Card card)
    {
        realm.beginTransaction();
        Card realmObject = deck.getCards().where().equalTo("ID", card.getID()).findFirst();
        if (realmObject != null)
        {
            realmObject.removeFromRealm();
        }
        realm.commitTransaction();
        deck.getCards().remove(card);
    }
}
