package de.mfgd_karteikarten.mfgd_karteikarten.ui.cardEdit;

import dagger.Component;
import de.mfgd_karteikarten.mfgd_karteikarten.base.ActivityScope;
import de.mfgd_karteikarten.mfgd_karteikarten.base.AppComponent;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = CardEditModule.class)
public interface CardEditComponent {
    CardEditPresenter getCardEditPresenter();
}
