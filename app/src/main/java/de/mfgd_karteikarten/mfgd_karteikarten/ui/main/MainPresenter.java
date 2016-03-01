package de.mfgd_karteikarten.mfgd_karteikarten.ui.main;

import javax.inject.Inject;

import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import de.mfgd_karteikarten.mfgd_karteikarten.base.TopicManager;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Topic;
import nucleus.factory.PresenterFactory;
import nucleus.presenter.Presenter;

public class MainPresenter extends Presenter<MainActivity> {
    private TopicManager topicManager;

    @Inject
    public MainPresenter(TopicManager topicManager) {
        this.topicManager = topicManager;
    }

    @Override
    protected void onTakeView(MainActivity view) {
        view.setTopics(topicManager.getTopics());
    }

    public void onAddTopicClicked() {
        Topic topic = new Topic("topic " + topicManager.getTopics().size());
        topicManager.addTopic(topic);

        MainActivity view = getView();
        if (view != null) {
            getView().addTopic(topic);
        }
    }

    public void onPositionClicked(int position) {
        MainActivity view = getView();
        if (view != null) {
            view.startTopicActivity(topicManager.getTopics().get(position).getID());
        }
    }

    public static class Factory implements PresenterFactory<MainPresenter> {
        private App app;

        public Factory(App app) {
            this.app = app;
        }

        @Override
        public MainPresenter createPresenter() {
            return DaggerMainComponent.builder()
                    .appComponent(app.component())
                    .build()
                    .getMainPresenter();
        }
    }
}
