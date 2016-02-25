package de.mfgd_karteikarten.mfgd_karteikarten.ui.topic;

import dagger.Module;
import dagger.Provides;
import de.mfgd_karteikarten.mfgd_karteikarten.base.ActivityScope;
import de.mfgd_karteikarten.mfgd_karteikarten.base.TopicManager;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Topic;

@Module
public class TopicModule
{
    int topic;

    public TopicModule(int topic)
    {
        this.topic = topic;
    }

    @Provides
    @ActivityScope
    public Topic provideTopic(TopicManager topicManager)
    {
        return topicManager.getTopic(topic);
    }
}
