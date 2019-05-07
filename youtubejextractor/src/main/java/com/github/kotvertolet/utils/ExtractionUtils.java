package com.github.kotvertolet.utils;


import com.google.code.regexp.Matcher;
import com.google.code.regexp.Pattern;

public class ExtractionUtils {

    public String extractYoutubeVideoPlayerCode(String playerUrl) {
        playerUrl = preparePlayerUrl(playerUrl);

        Pattern pattern = Pattern.compile("([a-z]+)$");
        Matcher matcher = pattern.matcher(playerUrl);
        if (!matcher.find()) {
            throw new IllegalStateException("Cannot identify player by url: " + playerUrl);
        }

        String playerType = matcher.group();
        switch (playerType) {
            case "js":
                return YoutubePlayerUtils.downloadJsPlayer(playerUrl);
            //TODO: This might be not necessary as swf player is outdated and not used anymore
            case "swf":
                throw new UnsupportedOperationException("Swf player type is not supported");
            default:
                throw new UnsupportedOperationException("Invalid player type: " + playerType);
        }
    }

    public String extractDecryptFunctionName(String playerCode) {
        Pattern pattern1 = Pattern.compile("([\"\\'])signature\\1\\s*,\\s*(?<sig>[a-zA-Z0-9$]+)\\(");
        Pattern pattern2 = Pattern.compile("\\.sig\\|\\|(?<sig>[a-zA-Z0-9$]+)\\(");
        Pattern pattern3 = Pattern.compile("yt\\.akamaized\\.net/\\)\\s*\\|\\|\\s*.*?\\s*c\\s*&&\\s*d\\.set\\([^,]+\\s*,\\s*(?:encodeURIComponent\\s*\\()?(?<sig>[a-zA-Z0-9$]+)\\(");
        Pattern pattern4 = Pattern.compile("\\bc\\s*&&\\s*d\\.set\\([^,]+\\s*,\\s*(?:encodeURIComponent\\s*\\()?\\s*(?<sig>[a-zA-Z0-9$]+)\\(");
        Pattern pattern5 = Pattern.compile("\\bc\\s*&&\\s*d\\.set\\([^,]+\\s*,\\s*\\([^)]*\\)\\s*\\(\\s*(?<sig>[a-zA-Z0-9$]+)\\(");

        String signatureDecryptFunctionName;
        Matcher matcher;
        if (pattern1.matcher(playerCode).find()) {
            matcher = pattern1.matcher(playerCode);
        } else if (pattern2.matcher(playerCode).find()) {
            matcher = pattern2.matcher(playerCode);
        } else if (pattern3.matcher(playerCode).find()) {
            matcher = pattern3.matcher(playerCode);
        } else if (pattern4.matcher(playerCode).find()) {
            matcher = pattern4.matcher(playerCode);
        } else if (pattern5.matcher(playerCode).find()) {
            matcher = pattern5.matcher(playerCode);
        } else throw new IllegalStateException("Can't find required JS function in JS player code");
        // Restarting the search
        matcher.find(0);
        signatureDecryptFunctionName = matcher.group(1);
        return signatureDecryptFunctionName;
    }

    private String preparePlayerUrl(String playerUrl) {
        if (playerUrl.isEmpty()) {
            throw new IllegalArgumentException("Cannot decrypt signature without player_url");
        }
        if (playerUrl.startsWith("//")) {
            playerUrl = "https:" + playerUrl;
        }
        Pattern pattern = Pattern.compile("https?://");
        Matcher matcher = pattern.matcher(playerUrl);
        if (!matcher.matches()) {
            playerUrl = "https://www.youtube.com" + playerUrl;
        } else
            throw new IllegalStateException("Can't create proper player url with url: " + playerUrl);
        return playerUrl;
    }
}