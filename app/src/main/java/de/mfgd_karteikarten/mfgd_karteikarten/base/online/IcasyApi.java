package de.mfgd_karteikarten.mfgd_karteikarten.base.online;

import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import rx.Observable;

public interface IcasyApi {
    @POST("/deck")
    Observable<String> createDeck(@Body Deck deck);

    @GET("/deck/{id}")
    Observable<Deck> getDeck(@Path("id") String id);

    @PUT("/deck/{id}")
    Observable<Deck> updateDeck(@Path("id") String id, @Body Deck deck);
}
