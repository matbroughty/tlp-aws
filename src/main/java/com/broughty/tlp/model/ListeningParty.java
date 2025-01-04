package com.broughty.tlp.model;

import java.time.LocalDate;

public record ListeningParty(String date,
                             String band,
                             String album,
                             String announcementTweet,
                             String replay, // replay link
                             String tweeters,
                             String twitterCollectionLink,
                             String spotifyLink, // link to album
                             String spotifyImgLink, // link to album artwork  300/300
                             String spotifyImgLinkSmall, // link to album artwork 60/60
                             String spotifyYear, // year album released
                             String listeningPartyNumber, // twitter listening party number
                             String spotifyImgLinkLarge

) {

    // get the iso date time string as a LocalDate
    public LocalDate getListeningPartyDate() {
        return date != null && date.length() >= 10 ? LocalDate.parse(date.substring(0, 10)) : null;
    }

    public LocalDate getAlbumReleaseDate() {
        return spotifyYear != null && !spotifyYear.isBlank() && spotifyYear.length() == 10 ? LocalDate.parse(spotifyYear) : null;
    }

    /**
     * Check if the date of the listening party matches today's date or the album release date matches today's date
     *
     * @return true if we have a match
     */
    public boolean dateMatch() {
        return dateMatchCheck(getListeningPartyDate()) || dateMatchCheck(getAlbumReleaseDate());
    }

    public boolean dateMatchCheck(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.getMonth().equals(LocalDate.now().getMonth()) && date.getDayOfMonth() == LocalDate.now().getDayOfMonth();
    }

}
