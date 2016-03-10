package de.mfgd_karteikarten.mfgd_karteikarten.ui.importDeck;

import dagger.Component;
import de.mfgd_karteikarten.mfgd_karteikarten.base.ActivityScope;
import de.mfgd_karteikarten.mfgd_karteikarten.base.AppComponent;

@ActivityScope
@Component(dependencies = {AppComponent.class}, modules = {ImportModule.class})
public interface ImportComponent {
    ImportPresenter getImportPresenter();
}
