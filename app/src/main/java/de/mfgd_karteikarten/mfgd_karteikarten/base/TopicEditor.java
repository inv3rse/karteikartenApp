package de.mfgd_karteikarten.mfgd_karteikarten.base;

import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Topic;
import io.realm.Realm;

public class TopicEditor {
    private Realm realm;
    private Topic topic;

    private int nextID;

    public TopicEditor(Realm realm, Topic topic) {
        this.realm = realm;
        this.topic = TopicManager.getTopic(realm, topic.getID());
        nextID = topic.getDecks().where().max("ID").intValue() + 1;
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

    public void removeDeck(Deck deck) {
        realm.beginTransaction();
        Deck realmObject = topic.getDecks().where().equalTo("ID", deck.getID()).findFirst();
        if (realmObject != null) {
            realmObject.removeFromRealm();
        }
        realm.commitTransaction();
        topic.getDecks().remove(deck);
    }

}
