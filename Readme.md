# Listening Party automated replays on bsky4j

Java lambda library to post to the [Bluesky](https://blueskyweb.xyz/)/ATProtocol API. 

Posts to:

* bluesky: https://bsky.app/profile/listeningparty.bsky.social

## How to run

This can all be run locally if AWS is configured on your machine.

The Lambda is in:

```
com.broughty.tlp.lambda.AnniversaryHandler 
```

Set the following environment variables for the Lambda:

* BLUESKY_IDENTIFIER - the bluesky handle
* BLUESKY_PASSWORD - the bluesky password

The Lambda will then pick up the listening part data file and determine if a replay post is required.

The [listening party data file (csv)](https://timstwitterlisteningparty.com/data/time-slot-data.csv) that drives the Lambda. 

Column 1 is the date of the listening party and column 11 is the album release date.  If either of these match then
a replay post is made.

## Libraries used


* [Apache Commons CSV](https://commons.apache.org/proper/commons-csv/)
* [AWS SDK for Java 2.x](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/home.html)
* [Bluesky4j](https://github.com/uakihir0/bsky4j)

## TODO List

* Add a CI/CD pipeline to auto-update AWS - at the moment just takes the shade jar and uploads it
* Switch [Bluesky4j](https://github.com/uakihir0/bsky4j) to use [Kotlin](https://github.com/uakihir0/kbsky) or write own library
* Add twitter posting
* Add Instagram posting

## Author

* [@matbroughty.bsky.social](https://bsky.app/profile/matbroughty.bsky.social)
* [Twitter:@matbroughty](https://twitter.com/matbroughty)

