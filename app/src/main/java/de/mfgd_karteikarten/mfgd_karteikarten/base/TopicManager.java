package de.mfgd_karteikarten.mfgd_karteikarten.base;


import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.mfgd_karteikarten.mfgd_karteikarten.data.Topic;
import io.realm.Realm;
import io.realm.RealmResults;

@Singleton
public class TopicManager {

    private Realm realm;
    private int nextID;

    @Inject
    public TopicManager(Realm realm)
    {
        this.realm = realm;
        nextID = realm.where(Topic.class).max("ID").intValue() + 1;
    }

    public Topic addTopic(Topic topic)
    {
        topic.setID(nextID);
        nextID += 1;

        realm.beginTransaction();
        realm.copyToRealm(topic);
        realm.commitTransaction();

        return topic;
    }

    public void editTopic(Topic topic)
    {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(topic);
        realm.commitTransaction();
    }

    public void removeTopic(Topic topic)
    {
        realm.beginTransaction();
        Topic realmObj = realm.where(Topic.class).equalTo("ID", topic.getID()).findFirst();
        realmObj.removeFromRealm();
        realm.commitTransaction();
    }

    @Nullable
    public Topic getTopic(int id)
    {
        Topic topic = realm.where(Topic.class).equalTo("ID", id).findFirst();
        if (topic != null)
        {
            return realm.copyFromRealm(topic);
        }

        return null;
    }

    public List<Topic> getTopics()
    {
        RealmResults<Topic> results = realm.where(Topic.class).findAllSorted("name");
        return realm.copyFromRealm(results.subList(0, results.size()));
    }

    /**
     * Gibt das Realm Objekt hinter der ID zurück
     * @param realm Realm Instanz in der gesucht wird
     * @param id ID nach der gesucht wird
     * @return Realm Object
     */
    public static Topic getTopic(Realm realm, int id)
    {
        return realm.where(Topic.class).equalTo("ID", id).findFirst();
    }
}
