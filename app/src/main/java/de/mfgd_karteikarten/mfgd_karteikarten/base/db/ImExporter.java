package de.mfgd_karteikarten.mfgd_karteikarten.base.db;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import io.realm.RealmObject;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class ImExporter
{
    private Gson gson;

    public ImExporter()
    {
        gson = buildGson();
    }

    static public Gson buildGson()
    {
        return new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy()
                {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f)
                    {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz)
                    {
                        return false;
                    }
                })
                .create();
    }

    public List<Deck> importDecks(File file) throws IOException
    {
        BufferedSource source = Okio.buffer(Okio.source(file));
        Deck[] decks = gson.fromJson(source.readUtf8(), Deck[].class);

        return Arrays.asList(decks);
    }

    public File exportDecks(List<Deck> decks, File dir) throws IOException
    {
        if (!dir.exists() && !dir.mkdir())
        {
            throw new IOException("could not create directory");
        }
        File file = new File(dir, "deckExport.json");

        if (file.exists())
        {
            if (!file.delete())
            {
                throw new IOException("Could not delete old File");
            }
        }
        if (!file.createNewFile())
        {
            throw new IOException("Could not create File");
        }

        BufferedSink sink = Okio.buffer(Okio.sink(file));
        String json = gson.toJson(decks);
        sink.writeUtf8(json);
        sink.close();

        return file;
    }
}
