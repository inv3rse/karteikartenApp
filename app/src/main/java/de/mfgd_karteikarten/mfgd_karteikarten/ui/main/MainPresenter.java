package de.mfgd_karteikarten.mfgd_karteikarten.ui.main;

import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import de.mfgd_karteikarten.mfgd_karteikarten.base.db.TopicManager;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Topic;
import nucleus.factory.PresenterFactory;
import nucleus.presenter.Presenter;

public class MainPresenter extends Presenter<MainActivity> {
    private TopicManager topicManager;
    private List<Topic> topics;

    private HashSet<Integer> selection;
    private boolean dialogVisible;
    private String dialogText;
    private int dialogId;

    @Inject
    public MainPresenter(TopicManager topicManager) {
        this.topicManager = topicManager;
        selection = new HashSet<>();
    }

    @Override
    protected void onTakeView(MainActivity view) {
        topics = topicManager.getTopics();
        view.setTopics(topics);
        view.setSelection(selection);

        if (dialogVisible)
        {
            view.showCreateEditDialog(dialogId, dialogText);
        }
    }

    public void onAddTopicClicked(MainActivity view) {
        dialogVisible = true;
        dialogId = Topic.UNKNOWN_ID;
        view.showCreateEditDialog(dialogId, "");
    }

    public void updateDialogStatus(boolean visible, String name)
    {
        dialogVisible = visible;
        dialogText = name;
    }

    public void addUpdateTopicName(int topicId, String name) {
        dialogVisible = false;
        dialogText = "";

        if (topicId != Topic.UNKNOWN_ID) {
            Topic topic = topicManager.getTopic(topicId);
            topic.setName(name);
            topicManager.editTopic(topic);
            update();
        } else {
            Topic topic = new Topic(name);
            topicManager.addTopic(topic);

            MainActivity view = getView();
            if (view != null) {
                view.addTopic(topic);
            }
        }
    }

    public void updateSelection(HashSet<Integer> selection)
    {
        this.selection = selection;
    }

    public void deleteSelection(MainActivity view)
    {
        for (int i = topics.size() - 1; i >= 0; --i) {
            if (selection.contains(i)) {
                topicManager.removeTopic(topics.remove(i));
            }
        }

        view.setTopics(topics);
    }

    public void editSelected(MainActivity view)
    {
        if (selection.size() == 1)
        {
            Topic topic = topics.get(selection.iterator().next());
            view.showCreateEditDialog(topic.getID(), topic.getName());
        }
    }

    // update function for the user interface
    private void update() {
        MainActivity view = getView();
        if (view != null) {
            getView().setTopics(topicManager.getTopics());
        }
    }

    public void onPositionClicked(int position) {
        MainActivity view = getView();
        if (view != null) {
            view.startTopicActivity(topics.get(position).getID());
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
