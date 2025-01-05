[![Java CI with Maven](https://github.com/matbroughty/tlp-aws/actions/workflows/maven.yml/badge.svg)](https://github.com/matbroughty/tlp-aws/actions/workflows/maven.yml)


# Listening Party automated replays on bsky4j and X (fka twitter)

Java lambda library to post to the [Bluesky](https://blueskyweb.xyz/)/ATProtocol API and to X.

Uses Java 21.

Posts to:

* bluesky: https://bsky.app/profile/listeningparty.bsky.social
* twitter (x): https://x.com/LlSTENlNG_PARTY

And if configured will write results of any posting to AWS SNS.


## How to run

This can all be run locally if AWS is configured on your machine.

The Lambda is in:

```
com.broughty.tlp.lambda.AnniversaryHandler 
```

Set the following environment variables for the Lambda:

* Bluesky
  * BLUESKY_IDENTIFIER - the bluesky handle
  * BLUESKY_PASSWORD - the bluesky password
* Twitter
  * twitter4j_oauth_accessToken - the twitter consumer key
  * twitter4j_oauth_accessTokenSecret - the twitter consumer secret
  * twitter4j_oauth_consumerKey - the twitter access token
  * twitter4j_oauth_consumerSecret - the twitter access token secret

Optionally if results of social messaging and required to be sent to AWS SNS

* AWS_SNS_TOPIC_NAME - the SNS topic name - i.e. 
  * arn:aws:sns:eu-west-2:343893643132:tlpanniversary

The Lambda will then pick up the listening party data file and determine if a replay post is required.

The [listening party data file (csv)](https://timstwitterlisteningparty.com/data/time-slot-data.csv) that drives the Lambda. 

Column 1 is the date of the listening party and column 11 is the album release date.  If either of these match then
a replay post is made.

For each listening party replay the SocialMediaFactory is called for the implemented social media types.

The lambda will return every post as json with the following format:

* socialMediaType - the social media type
* listeningNumber - the listening party number
* postId - the post id
* postUrl - the post url

```
[{"socialMediaType":"BLUE_SKY","listeningNumber":"624","postId":"bafyreif75nwxdzz3rwozgzz74xqbqpxdz56b5khc4yotbansaqsu2jin4u","postUrl":"at://did:plc:455jef2gx3ntk6ouqos3d4x4/app.bsky.feed.post/3levy5vfbjo2p"}]
```

If the AWS SNS topic is (env variable AWS_SNS_TOPIC_NAME ) set then the json will be sent to the topic.

## Libraries used


* [Apache Commons CSV](https://commons.apache.org/proper/commons-csv/)
* [AWS SDK for Java 2.x](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/home.html)
* [Bluesky4j](https://github.com/uakihir0/bsky4j)
* google/gson
* twitter4j
* sns-java-sdk

## TODO List

- [ ] Add a CI/CD pipeline to auto-update AWS - at the moment just takes the shade jar and uploads it 
- [ ] Switch [Bluesky4j](https://github.com/uakihir0/bsky4j) to use [Kotlin](https://github.com/uakihir0/kbsky) or write own library
- [x] Add twitter posting
- [ ] Add Instagram posting

## Author

* [@matbroughty.bsky.social](https://bsky.app/profile/matbroughty.bsky.social)
* [Twitter:@matbroughty](https://twitter.com/matbroughty)

