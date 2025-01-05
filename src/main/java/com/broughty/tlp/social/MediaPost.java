package com.broughty.tlp.social;

import com.broughty.tlp.model.ListeningParty;
import com.broughty.tlp.model.SocialMediaPost;

import java.io.IOException;
import java.net.URISyntaxException;

public interface MediaPost {

    SocialMediaPost postMessage(ListeningParty listeningParty) throws IOException, URISyntaxException;
}
