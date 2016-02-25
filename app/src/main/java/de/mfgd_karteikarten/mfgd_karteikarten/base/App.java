package de.mfgd_karteikarten.mfgd_karteikarten.base;


import android.app.Application;
import android.content.Context;

public class App extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    public AppComponent component()
    {
        return appComponent;
    }

    public static App get(Context context)
    {
        return (App) context.getApplicationContext();
    }
}
