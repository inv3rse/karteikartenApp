package de.mfgd_karteikarten.mfgd_karteikarten.base;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

@Module
public class AppModule {

    Application mApplication;

    public AppModule(Application application) {
        this.mApplication = application;
    }

    @Provides
    @Singleton
    public Application provideApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    public Realm provideRealm(Application application) {
        return Realm.getInstance(
                new RealmConfiguration.Builder(application)
                        .name("dataStore")
                        .build());
    }
}
