package de.mfgd_karteikarten.mfgd_karteikarten.ui.main;

import dagger.Component;
import de.mfgd_karteikarten.mfgd_karteikarten.base.ActivityScope;
import de.mfgd_karteikarten.mfgd_karteikarten.base.AppComponent;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = MainModule.class)
public interface MainComponent extends AppComponent
{
    MainPresenter getMainPresenter();
}
