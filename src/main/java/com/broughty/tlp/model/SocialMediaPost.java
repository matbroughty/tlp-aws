package com.broughty.tlp.model;

import com.broughty.tlp.social.SocialMediaFactory;

public record SocialMediaPost(SocialMediaFactory socialMediaType, String listeningNumber, String postId,
                              String postUrl) {
}
