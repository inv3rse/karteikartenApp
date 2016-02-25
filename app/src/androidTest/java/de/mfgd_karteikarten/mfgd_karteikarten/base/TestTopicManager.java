package de.mfgd_karteikarten.mfgd_karteikarten.base;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.util.List;

import de.mfgd_karteikarten.mfgd_karteikarten.data.Topic;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TestTopicManager
{
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private TopicManager topicManager;

    @Before
    public void setUp() throws IOException
    {
        Realm realm = Realm.getInstance(new RealmConfiguration.Builder(testFolder.newFolder())
                .name("testRealm")
                .inMemory()
                .build());

        topicManager = new TopicManager(realm);
    }

    @Test
    public void testAddTopic()
    {
        Topic topic1 = new Topic("Test Topic");
        Topic topic2 = new Topic("Test Topic 2");

        assertEquals(Topic.UNKNOWN_ID, topic1.getID());
        assertTrue(topicManager.getTopics().isEmpty());

        topicManager.addTopic(topic1);
        topicManager.addTopic(topic2);
        assertNotEquals(Topic.UNKNOWN_ID, topic1.getID());
        assertEquals(2, topicManager.getTopics().size());

        Topic copyOfTopic = topicManager.getTopics().get(0);
        assertEquals(copyOfTopic.getName(), "Test Topic");
    }

    @Test
    public void testSetTopic()
    {
        Topic topic = new Topic("Test Topic");
        topicManager.addTopic(topic);

        topic.setName("Changed Name");
        assertNotEquals(topicManager.getTopics().get(0).getName(), topic.getName());

        topicManager.editTopic(topic);
        assertEquals(topicManager.getTopics().get(0).getName(), topic.getName());
    }

    @Test
    public void testRemoveTopic()
    {
        Topic topic1 = new Topic("Test Topic");
        Topic topic2 = new Topic("Test Topic2");
        topicManager.addTopic(topic1);
        topicManager.addTopic(topic2);
        assertEquals(topicManager.getTopics().size(), 2);

        topicManager.removeTopic(topic1);
        assertNotNull(topic1);

        assertEquals(topicManager.getTopics().size(), 1);
        assertEquals(topic2.getName(), topicManager.getTopics().get(0).getName());
    }

    @Test
    public void testGetTopic()
    {
        assertNull(topicManager.getTopic(42));

        Topic topic = new Topic("Test Topic");
        topicManager.addTopic(topic);

        Topic copyOfTopic = topicManager.getTopic(topic.getID());
        assertNotNull(copyOfTopic);

        assertEquals(topic.getName(), copyOfTopic.getName());
    }

    @Test
    public void testGetTopics()
    {
        Topic topic1 = new Topic("Test Topic1");
        Topic topic2 = new Topic("Test Topic2");

        topicManager.addTopic(topic1);
        topicManager.addTopic(topic2);

        // Sicherstellen das die Liste nicht aus Realm Objects besteht
        List<Topic> topicList = topicManager.getTopics();
        topicList.get(0).setName("Changed Name");

        assertNotEquals(topicList.get(0).getName(), topic1.getName());
    }
}
