package de.mfgd_karteikarten.mfgd_karteikarten.base.db;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.mfgd_karteikarten.mfgd_karteikarten.data.Card;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import io.realm.RealmList;
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

    public List<Deck> importDecks(File file) throws IOException {
        BufferedSource source = Okio.buffer(Okio.source(file));
        Deck[] decks = gson.fromJson(source.readUtf8(), Deck[].class);

        return Arrays.asList(decks);
    }

    public File exportDecks(List<Deck> decks, File dir) throws IOException
    {
        if (!dir.exists() && ! dir.mkdir())
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

    private Gson buildGson()
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
                .registerTypeAdapter(new TypeToken<RealmList<Card>>()
                {
                }.getType(), new TypeAdapter<RealmList<Card>>()
                {
                    @Override
                    public void write(JsonWriter out, RealmList<Card> value) throws IOException
                    {
                        out.beginArray();
                        for (Card card : value)
                        {
                            out.beginObject();
                            out.name("ID").value(card.getID());
                            out.name("rating").value(card.getRating());
                            out.name("question").value(card.getQuestion());
                            out.name("answer").value(card.getAnswer());
                            out.endObject();
                        }
                        out.endArray();
                    }

                    @Override
                    public RealmList<Card> read(JsonReader in) throws IOException
                    {
                        RealmList<Card> cards = new RealmList<>();
                        in.beginArray();
                        while (in.hasNext())
                        {
                            Card card = new Card();

                            in.beginObject();
                            while (in.hasNext())
                            {
                                String name = in.nextName();
                                switch (name)
                                {
                                    case "ID":
                                        card.setID(in.nextInt());
                                        break;
                                    case "rating":
                                        card.setRating(in.nextInt());
                                        break;
                                    case "question":
                                        card.setQuestion(in.nextString());
                                        break;
                                    case "answer":
                                        card.setAnswer(in.nextString());
                                        break;
                                }
                            }
                            in.endObject();
                            cards.add(card);
                        }
                        in.endArray();

                        return cards;
                    }
                })
                .create();
    }
}
