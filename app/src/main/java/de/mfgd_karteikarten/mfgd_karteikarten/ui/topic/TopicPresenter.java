package de.mfgd_karteikarten.mfgd_karteikarten.ui.topic;

import javax.inject.Inject;

import de.mfgd_karteikarten.mfgd_karteikarten.base.ActivityScope;
import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import de.mfgd_karteikarten.mfgd_karteikarten.base.TopicEditor;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import nucleus.factory.PresenterFactory;
import nucleus.presenter.Presenter;

@ActivityScope
public class TopicPresenter extends Presenter<TopicActivity>
{
    private TopicEditor editor;

    @Inject
    public TopicPresenter(TopicEditor editor)
    {
        this.editor = editor;
    }

    @Override
    protected void onTakeView(TopicActivity topicActivity) {
        topicActivity.setDecks(editor.getDecks());
    }

    public void addDeck(Deck deck)
    {
        editor.addDeck(deck);
    }

    public static class Factory implements PresenterFactory<TopicPresenter>
    {
        private App app;
        private int topicId;

        public Factory(App app, int topicId)
        {
            this.app = app;
            this.topicId = topicId;
        }

        @Override
        public TopicPresenter createPresenter()
        {
            return DaggerTopicComponent.builder()
                    .appComponent(app.component())
                    .topicModule(new TopicModule(topicId))
                    .build()
                    .getTopicPresenter();
        }
    }
}
