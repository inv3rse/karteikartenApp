package de.mfgd_karteikarten.mfgd_karteikarten.ui.deck;

import dagger.Component;
import de.mfgd_karteikarten.mfgd_karteikarten.base.ActivityScope;
import de.mfgd_karteikarten.mfgd_karteikarten.base.AppComponent;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = DeckModule.class)
public interface DeckComponent extends AppComponent
{
    DeckPresenter getDeckPresenter();
}
