package de.mfgd_karteikarten.mfgd_karteikarten.base;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;

import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Topic;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class TestTopicEditor {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private TopicEditor topicEditor;

    @Before
    public void setUp() throws IOException
    {
        Realm realm = Realm.getInstance(new RealmConfiguration.Builder(testFolder.newFolder())
                .name("testRealm")
                .inMemory()
                .build());

        TopicManager topicManager = new TopicManager(realm);
        Topic topic = topicManager.addTopic(new Topic("TestTopic"));

        topicEditor = new TopicEditor(realm, topic);
    }

    @Test
    public void testAddDeck()
    {
        Deck deck1 = new Deck("deck1");
        Deck deck2 = new Deck("deck2");

        topicEditor.addDeck(deck1);
        deck2 = topicEditor.addDeck(deck2);

        assertNotEquals(Deck.UNKNOWN_ID, deck1.getID());
        assertNotEquals(Deck.UNKNOWN_ID, deck2.getID());

        Deck copyOfDeck1 = topicEditor.getDecks().get(0);
        copyOfDeck1.setName("new Name");
        assertNotEquals(copyOfDeck1.getName(), deck1.getName());

        assertEquals(2, topicEditor.getDecks().size());
        assertEquals(deck1.getName(), topicEditor.getDecks().get(0).getName());
        assertEquals(deck2.getName(), topicEditor.getDecks().get(1).getName());

        assertNotNull(topicEditor.getDecks().get(0).getCards());
    }

}
