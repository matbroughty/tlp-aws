package com.broughty.tlp.social;

public enum SocialMediaFactory {
    BLUESKY {
        @Override
        public MediaPost getNewInstance() {
            return new BlueSky();
        }
    },
    TWITTER {
        @Override
        public MediaPost getNewInstance() {
            return new TwitterX();
        }
    };

    /**
     * @return MediaPost new instance of specific MediaPost implementation
     */
    public abstract MediaPost getNewInstance();
}