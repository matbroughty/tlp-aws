package com.broughty.tlp.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ListeningPartyReaderTest {


    /**
     * More of an integration  test as actually uses the live data from the website
     * so needs internet access to run...
     */
    @Test
    public void testReadingAllParties() {
        var parties = ListeningPartyReader.readListeningPartyFile();
        assertEquals(1436, parties.size());
    }


    @Test
    public void testCheckFirstParty() {
        var parties = ListeningPartyReader.readListeningPartyFile();
        var firstParty = parties.getFirst();
        assertEquals("2020-03-23T22:00", firstParty.date());
        assertEquals("The Charlatans", firstParty.band());
        assertEquals("Some Friendly", firstParty.album());
        assertEquals("https://twitter.com/Tim_Burgess/status/1241686370789326849", firstParty.announcementTweet());
        assertEquals("https://open.spotify.com/album/0JWdN38GXUuoG3zHmXKmnj", firstParty.spotifyLink());
        assertEquals("1990-10-08", firstParty.spotifyYear());
        assertEquals("1", firstParty.listeningPartyNumber());
    }

    /**
     * The values in the csv file are encased in. quotes - i.e. "hello world" so best to remove this
     */
    @Test
    public void testTidyMethod() {
        assertEquals("charlatans", ListeningPartyReader.tidy("\"charlatans\""));
        assertEquals("\"charlatans", ListeningPartyReader.tidy("\"charlatans"));
        assertEquals("charlatans\"", ListeningPartyReader.tidy("charlatans\""));
        assertEquals("", ListeningPartyReader.tidy("\"\""));
        assertEquals("", ListeningPartyReader.tidy(""));
        assertNull(ListeningPartyReader.tidy(null));
    }


}