package de.mfgd_karteikarten.mfgd_karteikarten.ui.topic;

import dagger.Component;
import de.mfgd_karteikarten.mfgd_karteikarten.base.ActivityScope;
import de.mfgd_karteikarten.mfgd_karteikarten.base.AppComponent;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = TopicModule.class)
public interface TopicComponent extends AppComponent
{
    TopicPresenter getTopicPresenter();
}
