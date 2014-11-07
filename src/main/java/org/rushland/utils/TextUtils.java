package org.rushland.utils;

import org.bukkit.ChatColor;

/**
 * Managed by romain on 31/10/2014.
 */
public class TextUtils {
    public static String parseColors(String text, boolean replace) {
        String parsed = text;

        for(ChatColor color: ChatColor.values()) {
            if (replace && text.contains(color.name()))
                parsed = text.replace(color.name(), color.toString());
            else if(!replace && text.contains(color.toString()))
                parsed = text.replace(color.toString(), "");
        }

        return parsed;
    }
}
