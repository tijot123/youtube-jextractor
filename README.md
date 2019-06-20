# YoutubeJExtractor for Android

YoutubeJExtractor is Android library that allows you to extract youtube video and audio from any youtube video along with some other data such as a video title, description, author, thumbnails and others.
 
## Current status

WIP, library is functional but not well tested and documented. 

#### TODO
1. Live streams support (dash, hls, etc)
2. Workaround for region restricted videos
3. Add more tests

## How to install
[![](https://jitpack.io/v/kotvertolet/youtube-jextractor.svg)](https://jitpack.io/#kotvertolet/youtube-jextractor)


## How to use

```java
    YoutubeJExtractor youtubeJExtractor = new YoutubeJExtractor();
    YoutubeVideoData videoData;
    try {
        videoData = youtubeJExtractor.extract(videoId);
    }
    catch (ExtractionException e) {
        // Something really bad happened, nothing we can do except just show the user error some notification 
    }
    catch (YoutubeRequestException e) {
        // It's possibly there are some connection problems, ask user to check the internet connection and then retry 
    }
``` 
**YoutubeVideoData** is an object that contains data for the requested 
video split across two main objects: **VideoDetails** and **StreamingData**.

* **VideoDetails** contains various video data such as title, description, author, rating, view count, etc.
* **StreamingData** contains two fields with the lists of streams (both video and audio) and ***expiresInSeconds***
field which indicates how long links will be alive.
 
To get all the video streams:
```java
    List<VideoStreamItem> videoStreamsList = videoData.getStreamingData().getVideoStreamItems()
``` 

Each StreamItem object contains fields that describe the stream such as
 it's ***extension*** (like mp4, ogg, etc),
 ***codec***, ***bitrate***, ***url*** and many others. 
 Check *VideoStreamItem.class* and *AudioStreamItem.class* for the details.

## Requirements

The minimum SDK level is limited to 16 due to a third-party library limitation

## Credits

[Youtube-dl](https://github.com/ytdl-org/youtube-dl) - the idea and implementation were influenced by Youtube-dl
 
## License

 Distributed under the GPL v2 License. See [LICENSE.md](https://github.com/kotvertolet/YoutubeJExtractor/blob/master/LICENSE) for terms and conditions.
