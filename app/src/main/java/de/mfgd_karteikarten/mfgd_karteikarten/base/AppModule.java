package de.mfgd_karteikarten.mfgd_karteikarten.base;

import android.app.Application;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.mfgd_karteikarten.mfgd_karteikarten.base.db.ImExporter;
import de.mfgd_karteikarten.mfgd_karteikarten.base.online.IcasyApi;
import de.mfgd_karteikarten.mfgd_karteikarten.base.online.OnlineMapper;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

@Module
public class AppModule
{

    Application mApplication;

    public AppModule(Application application)
    {
        this.mApplication = application;
    }

    @Provides
    @Singleton
    public Application provideApplication()
    {
        return mApplication;
    }

    @Provides
    @Singleton
    public Realm provideRealm(Application application)
    {
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
    public Gson provideRealmGson()
    {
        return ImExporter.buildGson();
    }

    @Provides
    @Singleton
    public IcasyApi provideApi(Gson gson)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://icasy-pro.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(IcasyApi.class);
    }
}
