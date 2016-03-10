package de.mfgd_karteikarten.mfgd_karteikarten.base.online;

import io.realm.RealmObject;

public class Mapping extends RealmObject {
    private int localId;
    private String onlineId;

    public Mapping()
    {

    }

    public Mapping(int localId, String onlineId)
    {
        this.localId = localId;
        this.onlineId = onlineId;
    }

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public String getOnlineId() {
        return onlineId;
    }

    public void setOnlineId(String onlineId) {
        this.onlineId = onlineId;
    }
}
