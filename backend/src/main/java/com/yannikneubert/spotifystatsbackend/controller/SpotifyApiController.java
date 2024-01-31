package com.yannikneubert.spotifystatsbackend.controller;

import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.artists.GetArtistsTopTracksRequest;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import org.apache.hc.core5.http.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.yannikneubert.spotifystatsbackend.controller.AuthController.spotifyApi;

@RestController
@RequestMapping("/api")
public class SpotifyApiController {

    @GetMapping(value = "user-top-artists")
    public List<Artist> getUserTopArtists() {

        GetUsersTopArtistsRequest getUsersTopArtistsRequest = spotifyApi.getUsersTopArtists()
                .time_range("medium_term")
                .limit(10)
                .build();

        try {
            final Paging<Artist> artistPaging = getUsersTopArtistsRequest.execute();

            // return top artists as JSON
            return List.of(artistPaging.getItems());
        } catch (Exception e) {
            System.out.println("Something went wrong!\n" + e.getMessage());
        }
        return null;
    }

    @GetMapping(value = "create-top-playlist")
    public ResponseEntity<String> createTopPlaylist() throws IOException, ParseException, SpotifyWebApiException {
        var topArtists = getUserTopArtists();
        List<Track> randomTopTracks = new ArrayList<>();
            topArtists.forEach(artist -> {
                GetArtistsTopTracksRequest getArtistsTopTracks =
                        spotifyApi.getArtistsTopTracks(artist.getId(), CountryCode.DE).build();
                try {
                    var artistTopTracks = new ArrayList<>(List.of(getArtistsTopTracks.execute()));
                    Collections.shuffle(artistTopTracks);
                    var subsetArtistTopTracks = artistTopTracks.subList(0, 5);
                    randomTopTracks.addAll(subsetArtistTopTracks);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

        Collections.shuffle(randomTopTracks);
        String[] urisForTracksArray = randomTopTracks.stream().map(Track::getUri).toList().toArray(String[]::new);
        var createPlaylistRequest = spotifyApi.createPlaylist("1121789751", "Top Playlist " + Instant.now().toString().substring(0, 16)).build();
        var newPlaylist = createPlaylistRequest.execute();
        if (newPlaylist == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        var addTracksToPlaylistRequest = spotifyApi.addItemsToPlaylist(newPlaylist.getId(), urisForTracksArray).build();
        addTracksToPlaylistRequest.execute();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
