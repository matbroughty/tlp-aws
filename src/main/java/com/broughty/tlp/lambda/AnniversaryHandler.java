package com.broughty.tlp.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.broughty.tlp.helper.ListeningPartyReader;
import com.broughty.tlp.model.ListeningParty;
import com.broughty.tlp.model.SocialMediaPost;
import com.broughty.tlp.model.SocialMediaType;
import com.broughty.tlp.social.BlueSky;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class AnniversaryHandler implements RequestHandler<Object, String> {

    public String handleRequest(Object input, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Input is: " + input);
        logger.log("JDK Version: " + System.getProperty("java.version"));
        List<ListeningParty> listeningPartyList = ListeningPartyReader.readListeningPartyFile();
        logger.log("Read in '" + listeningPartyList.size() + "' listening parties");
        // check if we have a match in the list based on today's date and ListeningParty.date or
        // ListeningParty.spotifyYear and get all matches back
        var matches = listeningPartyList.stream().filter(ListeningParty::dateMatch).toList();

        logger.log("Found '" + matches.size() + "' anniversary listening parties for today " + LocalDate.now());
        BlueSky blueSky = new BlueSky();
        List<SocialMediaPost> messages = matches.stream()
                .filter(lp -> lp.tweeters() != null && !lp.tweeters().isBlank()).map(lp -> {
            SocialMediaPost message;
            try {
                message = blueSky.postMessage(lp);
                logger.log("posted message = " + message);
            } catch (Exception e) {
                logger.log("Error posting message to Bluesky for party " + lp + " " + e.getMessage());
                e.printStackTrace(System.err);
                Arrays.stream(e.getStackTrace()).forEach(st -> logger.log(st.toString()));
                message = new SocialMediaPost(SocialMediaType.BLUE_SKY, lp.listeningPartyNumber(), null, null);
            }
            return message;
        }).toList();
        logger.log("Posted '" + messages.size() + "' messages to Bluesky");

        Gson gson = new Gson();
        logger.log("Returned Output is: " + gson.toJson(messages));

        return gson.toJson(messages);

    }

}