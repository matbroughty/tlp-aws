package com.broughty.tlp.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.broughty.tlp.helper.ListeningPartyReader;
import com.broughty.tlp.model.ListeningParty;
import com.broughty.tlp.model.SocialMediaPost;
import com.broughty.tlp.social.SocialMediaFactory;
import com.google.gson.Gson;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Handler for requests to Lambda function.
 * Will find any listening parties that match today's date and post a message to all
 * the configured social media platforms for each party
 */
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
        // for each matching party post a message to the social media platforms configured
        List<SocialMediaPost> messages = matches.stream()
                .filter(lp -> lp.tweeters() != null && !lp.tweeters().isBlank()).map(lp -> Arrays.stream(SocialMediaFactory.values()).map(sm -> {
                    SocialMediaPost message;
                    try {
                        message = sm.getNewInstance().postMessage(lp);
                        logger.log("posted message = " + message);
                    } catch (Exception e) {
                        logger.log("Error posting message to " + sm + " for party " + lp + " " + e.getMessage());
                        e.printStackTrace(System.err);
                        message = new SocialMediaPost(sm, lp.listeningPartyNumber(), e.getMessage(), null);
                    }
                    return message;
                }).toList()).flatMap(List::stream).toList();
        logger.log("Posted '" + messages.size() + "' messages");
        Gson gson = new Gson();
        logger.log("Returned Output is: " + gson.toJson(messages));

        // post to SNS topic if configured
        if (System.getenv("AWS_SNS_TOPIC_NAME") != null) {
            logger.log("SNS topic configured - posting output to SNS topic " +
                    System.getenv("AWS_SNS_TOPIC_NAME"));
            pubTopic(gson.toJson(messages), System.getenv("AWS_SNS_TOPIC_NAME"));
        } else {
            logger.log("SNS topic not configured");
        }

        return gson.toJson(messages);

    }


    public static void pubTopic(String message, String topicArn) {
        try {
            PublishResponse result;
            try (SnsClient snsClient = SnsClient.builder()
                    .region(Region.EU_WEST_2)
                    .build()) {

                PublishRequest request = PublishRequest.builder()
                        .message(message)
                        .topicArn(topicArn)
                        .build();

                result = snsClient.publish(request);
            }
            System.out
                    .println(result.messageId() + " Message sent. Status is " + result.sdkHttpResponse().statusCode());

        } catch (SnsException e) {
            System.err.println("issue with SNS - not sent: " + e.awsErrorDetails().errorMessage());
        }
    }

}