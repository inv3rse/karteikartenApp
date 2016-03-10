package de.mfgd_karteikarten.mfgd_karteikarten.base;

import android.app.Application;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.mfgd_karteikarten.mfgd_karteikarten.base.online.IcasyApi;
import de.mfgd_karteikarten.mfgd_karteikarten.base.online.OnlineMapper;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

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

    @Provides
    @Singleton
    public OnlineMapper provideOnlineMapper(Realm realm)
    {
        return new OnlineMapper(realm);
    }

    @Provides
    public Gson provideRealmGson() {
        return new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();
    }

    @Provides
    @Singleton
    public IcasyApi provideApi(Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://icasy-pro.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(IcasyApi.class);
    }
}
