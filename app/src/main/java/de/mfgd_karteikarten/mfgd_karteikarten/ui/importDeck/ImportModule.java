package de.mfgd_karteikarten.mfgd_karteikarten.ui.importDeck;

import dagger.Module;
import dagger.Provides;

@Module
public class ImportModule {

    private String importUrl;

    public ImportModule(String importUrl)
    {
        this.importUrl = importUrl;
    }

    @Provides
    public String provideImportUrl()
    {
        return importUrl;
    }
}
