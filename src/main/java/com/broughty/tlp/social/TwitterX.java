package com.broughty.tlp.social;

import com.broughty.tlp.model.ListeningParty;
import com.broughty.tlp.model.SocialMediaPost;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.tweet.Tweet;
import io.github.redouane59.twitter.signature.TwitterCredentials;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class TwitterX implements MediaPost {

    @Override
    public SocialMediaPost postMessage(ListeningParty listeningParty) {


        TwitterClient twitterClient = new TwitterClient(TwitterCredentials.builder()
                .accessToken(System.getenv("twitter4j_oauth_accessToken"))
                .accessTokenSecret(System.getenv("twitter4j_oauth_accessTokenSecret"))
                .apiKey(System.getenv("twitter4j_oauth_consumerKey"))
                .apiSecretKey(System.getenv("twitter4j_oauth_consumerSecret"))
                .build());


        var date = Objects.requireNonNull(listeningParty.getAlbumReleaseDate()).format(DateTimeFormatter.ofPattern("d MMMM yyyy"));
        var msg = "";
        if (listeningParty.dateMatchCheck(listeningParty.getAlbumReleaseDate())) {
            msg = date + " " + listeningParty.band().trim() + " released " + listeningParty.album().trim() + ". You can find the replay here " + listeningParty.replay() + ". Input from " + listeningParty.tweeterLinkList() + " #TimsTwitterListeningParty #ttlp" + listeningParty.listeningPartyNumber();
        } else {
            var yearsAgo = LocalDate.now().getYear() - Objects.requireNonNull(listeningParty.getListeningPartyDate()).getYear();
            msg = yearsAgo + " years ago today we had a listening party for " + listeningParty.album().trim() + " by " + listeningParty.band().trim() + ". You can find the replay here " + listeningParty.replay() + ". Input from " + listeningParty.tweeterLinkList() + " #TimsTwitterListeningParty #ttlp" + listeningParty.listeningPartyNumber();
        }

        Tweet tweet = twitterClient.postTweet(msg);
        System.out.println("Tweet posted with id " + tweet.getId());

        return new SocialMediaPost(SocialMediaFactory.TWITTER, listeningParty.listeningPartyNumber(),
                tweet.getId(), tweet.getSource());

    }
}
