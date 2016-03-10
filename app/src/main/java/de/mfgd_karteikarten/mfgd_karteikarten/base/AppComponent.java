package de.mfgd_karteikarten.mfgd_karteikarten.base;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import de.mfgd_karteikarten.mfgd_karteikarten.base.db.TopicManager;
import de.mfgd_karteikarten.mfgd_karteikarten.base.online.IcasyApi;
import de.mfgd_karteikarten.mfgd_karteikarten.base.online.OnlineMapper;
import io.realm.Realm;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    Application getApplication();

    Realm getRealmInstance();

    OnlineMapper getOnlineMapper();

    IcasyApi getApi();

    TopicManager getTopicManager();
}