package de.mfgd_karteikarten.mfgd_karteikarten.base;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    Application getApplication();

    TopicManager getTopicManager();
}