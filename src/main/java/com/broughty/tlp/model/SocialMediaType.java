package com.broughty.tlp.model;

public enum SocialMediaType {
    BLUE_SKY("bluesky"),
    TWITTER("twitter"),
    INSTAGRAM("instagram");

    private final String value;

    SocialMediaType(String value) {
        this.value = value;
    }

    @SuppressWarnings("unused")
    public String getValue() {
        return value;
    }
}
