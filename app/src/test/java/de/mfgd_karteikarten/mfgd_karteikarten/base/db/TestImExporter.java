package de.mfgd_karteikarten.mfgd_karteikarten.base.db;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.mfgd_karteikarten.mfgd_karteikarten.base.db.ImExporter;
import de.mfgd_karteikarten.mfgd_karteikarten.base.db.TopicManager;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Card;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Topic;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TestImExporter
{
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private ImExporter imExporter;

    @Before
    public void setUp()
    {
        imExporter = new ImExporter();
    }

    @Test
    public void testExport() throws IOException
    {
        RealmList<Card> cards = new RealmList<>();
        for (int i = 0; i < 10; ++i)
        {
            Card card = new Card();
            card.setID(i);
            card.setQuestion("Question " + i);
            card.setAnswer("Answer " + i);
            card.setRating(i);

            cards.add(card);
        }

        Deck deck = new Deck("testDeck");
        deck.setID(10);
        deck.setCards(cards);

        List<Deck> decks = new ArrayList<>();
        decks.add(deck);

        File result = imExporter.exportDecks(decks, testFolder.newFolder());
        assertNotNull(result);

        BufferedSource source = Okio.buffer(Okio.source(result));
        System.out.println(source.readByteString().utf8());
    }

}