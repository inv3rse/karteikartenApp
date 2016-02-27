package de.mfgd_karteikarten.mfgd_karteikarten.base;

import java.util.List;

import javax.inject.Inject;

import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Topic;
import io.realm.Realm;

@ActivityScope
public class TopicEditor {
    private Realm realm;
    private Topic topic;
    private int nextID = 1;

    @Inject
    public TopicEditor(Realm realm, Topic topic) {
        this.realm = realm;
        this.topic = TopicManager.getTopic(realm, topic.getID());
        Number maxId = realm.where(Deck.class).max("ID");
        if (maxId != null)
        {
            nextID = maxId.intValue() + 1;
        }
    }

    /**
     * Gibt das Deck mit id zurück
     *
     * @param id ID vom Deck
     * @return einfaches Deck Objekt (kein Realm Objekt)
     */
    public Deck getDeck(int id) {
        Deck realmObject = topic.getDecks().where().equalTo("ID", id).findFirst();
        if (realmObject != null) {
            return realm.copyFromRealm(realmObject);
        }

        return null;
    }

    /**
     * Gibt die Liste aller Decks zurück.
     * @return Liste aller Decks.
     */
    public List<Deck> getDecks()
    {
        return realm.copyFromRealm(topic.getDecks());
    }


    /**
     * Fügt ein neues Deck hinzu. ID wird gesetzt.
     *
     * @param deck neues Deck
     * @return Deck mit gesetzter ID (kein Realm Objekt)
     */
    public Deck addDeck(Deck deck) {
        deck.setID(nextID);
        nextID += 1;

        realm.beginTransaction();
        topic.getDecks().add(deck);
        realm.commitTransaction();

        return deck;
    }

    /**
     * Verändert das Deck mit ID in der Datenbank
     *
     * @param deck Das zu verändernde Deck, dessen ID bereits gesetzt ist
     */
    public void setDeck(Deck deck) {
        realm.beginTransaction();
        Deck oldDeck = topic.getDecks().where().equalTo("ID", deck.getID()).findFirst();
        oldDeck.setName(deck.getName());
        oldDeck.setCards(deck.getCards());
        realm.commitTransaction();
    }

    /**
     * Entfernt das Deck aus der DB
     * @param deck Deck mit ID das entfernt wird
     */
    public void removeDeck(Deck deck) {
        realm.beginTransaction();
        Deck realmObject = topic.getDecks().where().equalTo("ID", deck.getID()).findFirst();
        if (realmObject != null) {
            realmObject.removeFromRealm();
        }
        realm.commitTransaction();
        topic.getDecks().remove(deck);
    }

    /**
     * Gibt das Realm Objekt zurück
     * @param realm Realm Instanz in der gesucht wird
     * @param id ID des Decks
     * @return Realm Objekt oder null
     */
    public static Deck getDeck(Realm realm, int id)
    {
        return realm.where(Deck.class).equalTo("ID", id).findFirst();
    }
}
