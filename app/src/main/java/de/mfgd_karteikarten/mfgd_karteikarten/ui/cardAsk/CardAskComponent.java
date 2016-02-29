package de.mfgd_karteikarten.mfgd_karteikarten.ui.cardAsk;

import dagger.Component;
import de.mfgd_karteikarten.mfgd_karteikarten.base.ActivityScope;
import de.mfgd_karteikarten.mfgd_karteikarten.base.AppComponent;
import de.mfgd_karteikarten.mfgd_karteikarten.ui.cardAsk.CardAskPresenter;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = CardAskModul.class)
public interface CardAskComponent extends AppComponent
{
    CardAskPresenter getCardAskPresenter();
}
