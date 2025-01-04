package com.broughty.tlp.social;

import bsky4j.ATProtocolFactory;
import bsky4j.BlueskyFactory;
import bsky4j.api.entity.atproto.repo.RepoUploadBlobRequest;
import bsky4j.api.entity.atproto.repo.RepoUploadBlobResponse;
import bsky4j.api.entity.atproto.server.ServerCreateSessionRequest;
import bsky4j.api.entity.atproto.server.ServerCreateSessionResponse;
import bsky4j.api.entity.bsky.feed.FeedPostRequest;
import bsky4j.api.entity.bsky.feed.FeedPostResponse;
import bsky4j.api.entity.share.Response;
import bsky4j.domain.Service;
import bsky4j.model.bsky.embed.EmbedExternal;
import bsky4j.model.bsky.embed.EmbedExternalExternal;
import com.broughty.tlp.model.ListeningParty;
import com.broughty.tlp.model.SocialMediaPost;
import com.broughty.tlp.model.SocialMediaType;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Responsible for posting messages to Bluesky.
 * A 2 phase approach is used:
 * <ol>
 *     <li>Upload the image to Bluesky</li>
 *     <li>Post the message with the image/blob link</li>
 * </ol>
 * returns a SocialMediaPost object with the details of the post.
 */
public class BlueSky {


    public SocialMediaPost postMessage(ListeningParty listeningParty) throws IOException, URISyntaxException {
        System.out.println("Posting message to Bluesky for party " + listeningParty);
        var blueSkyUrl = System.getenv("BLUESKY_URL");
        var blueSkyId = System.getenv("BLUESKY_IDENTIFIER");
        var blueSkyPassword = System.getenv("BLUESKY_PASSWORD");
        System.out.println("BlueSky URL is " + blueSkyUrl + "and id is " + blueSkyId);

        Response<ServerCreateSessionResponse> responseSession = BlueskyFactory
                .getInstance(Service.BSKY_SOCIAL.getUri())
                .server().createSession(
                        ServerCreateSessionRequest.builder()
                                .identifier(blueSkyId)
                                .password(blueSkyPassword)
                                .build()
                );

        String accessJwt = responseSession.get().getAccessJwt();

        // Upload Image first based on the spotify image link
        URL url = new URI(listeningParty.spotifyImgLinkLarge()).toURL();
        InputStream stream = url.openStream();

        Response<RepoUploadBlobResponse> responseBlob = ATProtocolFactory
                .getInstance(Service.BSKY_SOCIAL.getUri())
                .repo().uploadBlob(
                        RepoUploadBlobRequest.fromStreamBuilder()
                                .accessJwt(accessJwt)
                                .stream(stream)
                                .name(listeningParty.album() + ".png")
                                .build()
                );


        System.out.println(responseBlob.get().getBlob().getRef().getLink());
        // Setup Image with response blob above
        EmbedExternal imagesMain = new EmbedExternal();
        EmbedExternalExternal external = new EmbedExternalExternal();
        external.setThumb(responseBlob.get().getBlob());
        external.setDescription(listeningParty.album());
        external.setTitle(listeningParty.band());
        external.setUri(listeningParty.replay());
        imagesMain.setExternal(external);

        // Build the text for the message based on the type of anniversary
        var date = Objects.requireNonNull(listeningParty.getAlbumReleaseDate()).format(DateTimeFormatter.ofPattern("d MMMM yyyy"));
        var msg = "";
        if (listeningParty.dateMatchCheck(listeningParty.getAlbumReleaseDate())) {
            msg = listeningParty.band().trim() + " released " + listeningParty.album().trim() + " on this day  in " + listeningParty.getAlbumReleaseDate().getYear() + " (" + date + "). You can replay the listening party (no_" + listeningParty.listeningPartyNumber() + ") here:";
        } else {
            var yearsAgo = LocalDate.now().getYear() - Objects.requireNonNull(listeningParty.getListeningPartyDate()).getYear();
            msg = yearsAgo + " years ago";
            msg = msg + " we had a listening party for " + listeningParty.album().trim() + " by " + listeningParty.band().trim() + ". You can replay the listening party (no_" + listeningParty.listeningPartyNumber() + ") here:";
        }

        // Post With Image
        Response<FeedPostResponse> responsePost = BlueskyFactory
                .getInstance(Service.BSKY_SOCIAL.getUri())
                .feed().post(
                        FeedPostRequest.builder()
                                .accessJwt(accessJwt)
                                .text(msg)
                                .embed(imagesMain)
                                .build()
                );

        var socialMediaPost = new SocialMediaPost(SocialMediaType.BLUE_SKY, listeningParty.listeningPartyNumber(),
                responsePost.get().getCid(), responsePost.get().getUri());
        System.out.println("Posted message to Bluesky for " + listeningParty + " with response " + socialMediaPost);
        return socialMediaPost;
    }


}
