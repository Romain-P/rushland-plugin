package org.rushland.utils;

/**
 * Managed by romain on 06/11/2014.
 */
public class TimeUtils {
    public static int convertSecondsToTicks(double seconds) {
        return (int)((seconds) / 0.05);
    }
}
