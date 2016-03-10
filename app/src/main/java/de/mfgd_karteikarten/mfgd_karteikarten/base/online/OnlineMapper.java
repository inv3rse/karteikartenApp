package de.mfgd_karteikarten.mfgd_karteikarten.base.online;

import io.realm.Realm;

/**
 * Merkt sich welche Decks online welche ID haben.
 */
public class OnlineMapper {

    private Realm realm;

    public OnlineMapper(Realm realm) {
        this.realm = realm;
    }

    public void addMapping(int localId, String onlineId) {
        Mapping mapping = new Mapping(localId, onlineId);
        realm.beginTransaction();
        realm.copyToRealm(mapping);
        realm.commitTransaction();
    }

    public String getOnlineMapping(int localId) {
        Mapping mapping = realm.where(Mapping.class).equalTo("localId", localId).findFirst();
        if (mapping != null) {
            return mapping.getOnlineId();
        }

        return null;
    }
}
