package org.rushland.utils;

import org.bukkit.ChatColor;

/**
 * Managed by romain on 31/10/2014.
 */
public class TextUtils {
    public static String parseColors(String text) {
        String parsed = text;

        for(ChatColor color: ChatColor.values())
            if(text.contains(color.name()))
                parsed = text.replace(color.name(), color.toString());

        return parsed;
    }
}
