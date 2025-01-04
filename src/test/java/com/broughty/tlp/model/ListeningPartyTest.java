package com.broughty.tlp.model;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ListeningPartyTest {

    @Test
    void testGetDateAsLocalDate() {
        ListeningParty listeningParty = new ListeningParty("2020-03-23T22:00", "The Charlatans", "Some Friendly",
                "https://twitter.com/Tim_Burgess/status/1241686370789326849",
                "https://open.spotify.com/album/0JWdN38GXUuoG3zHmXKmnj",
                "1990-10-08", "1", "https://open.spotify.com/album/0JWdN38GXUuoG3zHmXKmnj",
                "https://open.spotify.com/album/0JWdN38GXUuoG3zHmXKmnj",
                "https://open.spotify.com/album/0JWdN38GXUuoG3zHmXKmnj",
                "https://open.spotify.com/album/0JWdN38GXUuoG3zHmXKmnj", "1", "");
        assertEquals("2020-03-23", Objects.requireNonNull(listeningParty.getListeningPartyDate()).toString());
    }


    @Test
    void testGetAlbumReleaseDate() {
        ListeningParty listeningParty = new ListeningParty("2020-03-23T22:00", "The Charlatans", "Some Friendly",
                "https://twitter.com/Tim_Burgess/status/1241686370789326849",
                "https://open.spotify.com/album/0JWdN38GXUuoG3zHmXKmnj",
                "@matbroughty", "1", "https://open.spotify.com/album/0JWdN38GXUuoG3zHmXKmnj",
                "https://open.spotify.com/album/0JWdN38GXUuoG3zHmXKmnj",
                "https://open.spotify.com/album/0JWdN38GXUuoG3zHmXKmnj",
                "1990-10-08", "1", "");
        assertEquals("1990-10-08", Objects.requireNonNull(listeningParty.getAlbumReleaseDate()).toString());
    }

    @Test
    void testGetAlbumReleaseDateNull() {
        ListeningParty listeningParty = new ListeningParty("2020-03-23T22:00", "The Charlatans", "Some Friendly",
                "https://twitter.com/Tim_Burgess/status/1241686370789326849",
                "https://open.spotify.com/album/0JWdN38GXUuoG3zHmXKmnj",
                "@matbroughty", "1", "https://open.spotify.com/album/0JWdN38GXUuoG3zHmXKmnj",
                "https://open.spotify.com/album/0JWdN38GXUuoG3zHmXKmnj",
                "https://open.spotify.com/album/0JWdN38GXUuoG3zHmXKmnj",
                null, "1", "");
        assertNull(listeningParty.getAlbumReleaseDate());
    }


    /**
     * Test the dateMatch method - wont match as this will use todays date by default
     */
    @Test
    public void testDateMatchFalse() {
        ListeningParty listeningParty = new ListeningParty("2020-03-22T22:00", "The Charlatans", "Some Friendly",
                "https://twitter.com/Tim_Burgess/status/1241686370789326849",
                "https://open.spotify.com/album/0JWdN38GXUuoG3zHmXKmnj",
                "@matbroughty", "1", "https://open.spotify.com/album/0JWdN38GXUuoG3zHmXKmnj",
                "https://open.spotify.com/album/0JWdN38GXUuoG3zHmXKmnj",
                "https://open.spotify.com/album/0JWdN38GXUuoG3zHmXKmnj",
                "1990-10-08", "1", "");
        assertFalse(listeningParty.dateMatch());
    }


    @Test
    public void testLListeningPartyLocalDateValid() {
        ListeningParty listeningParty = new ListeningParty("2020-03-22T22:00", "The Charlatans", "Some Friendly",
                "https://twitter.com/Tim_Burgess/status/1241686370789326849",
                "https://open.spotify.com/album/0JWdN38GXUuoG3zHmXKmnj",
                "@matbroughty", "1", "https://open.spotify.com/album/0JWdN38GXUuoG3zHmXKmnj",
                "https://open.spotify.com/album/0JWdN38GXUuoG3zHmXKmnj",
                "https://open.spotify.com/album/0JWdN38GXUuoG3zHmXKmnj",
                "1990-10-08", "1", "");

        assertEquals("2020-03-22", Objects.requireNonNull(listeningParty.getListeningPartyDate()).toString());
    }

    @Test
    public void testListeningPartyLocalDateNull() {
        ListeningParty listeningParty = new ListeningParty(null, "The Charlatans", "Some Friendly",
                "https://twitter.com/Tim_Burgess/status/1241686370789326849",
                "https://open.spotify.com/album/0JWdN38GXUuoG3zHmXKmnj",
                "@matbroughty", "1", "https://open.spotify.com/album/0JWdN38GXUuoG3zHmXKmnj",
                "https://open.spotify.com/album/0JWdN38GXUuoG3zHmXKmnj",
                "https://open.spotify.com/album/0JWdN38GXUuoG3zHmXKmnj",
                "1990-10-08", "1", "");

        assertNull(listeningParty.getListeningPartyDate());
    }


}