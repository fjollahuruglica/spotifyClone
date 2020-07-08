package com.example.spotify;


import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpotifyClient {
    @FormUrlEncoded
    @POST("token")
    Call<AccessToken> getAccessToken(
            @Field("grant_type") String grant_type,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("code") String code,
            @Field("redirect_uri") String redirectUri
    );


    @GET("me")
    Call<UserData> getUserData();

///Home
    @GET("browse/new-releases")
    Call<NewReleases> getNewReleases(@Query("country") String country, @Query("limit") int limit);

    @GET("me/player/recently-played")
    Call<RecentlyPlayed> getRecentlyPlayed(@Query("limit") int limit);

    @GET("me/top/{type}")
    Call<FavArtist> getFavArtists(@Path(value = "type", encoded = true) String type, @Query("limit") int limit);

    @GET("browse/categories")
    Call<Genre> getGenre(@Query("country") String country);

    @GET("me/playlists")
    Call<MyPlaylist> getMyPlaylists();

    @GET("me/albums")
    Call<MyAlbums> getMyAlbums();

    @GET("me/following")
    Call<MyArtist> getMyArtists(@Query("type") String type);

    @GET("search")
    Call<Search> search(@Query("q") String q, @Query("type") String type);

    @GET("tracks/{id}")
    Call<Track> getTrack(@Path(value = "id", encoded = true) String id);

    @GET("albums/{id}")
    Call<Album> getAlbum(@Path(value = "id", encoded = true) String id);

    @GET("playlists/{playlist_id}")
    Call<GetPlaylist> getPlaylist(@Path(value = "playlist_id", encoded = true) String playlist_id, @Query("country") String country);

    @GET("browse/categories/{category_id}/playlists")
    Call<CatPlaylist> getCategoryPlaylist(@Path(value = "category_id", encoded = true) String category_id, @Query("country") String country);

    @GET("artists/{id}")
    Call<Artist> getArtist(@Path(value = "id", encoded = true) String id);

    @PUT("me/tracks")
    Call<Track> saveTrack(@Query("ids") String ids);

    @DELETE("me/tracks")
    Call<Track> unSaveTrack(@Query("ids") String ids);

    @GET("artists/{id}/top-tracks")
    Call<GetTop> getArtistTopTracks(@Path(value = "id", encoded = true) String id, @Query("country") String country);

    @GET("artists/{id}/albums")
    Call<MyAlbums> getArtistAlbums(@Path(value = "id", encoded = true) String id, @Query("include_groups") String include_groups);

    @GET("me/tracks")
    Call<MyPlaylist> getUserLikedTracks(@Query("limit") int limit);
}
