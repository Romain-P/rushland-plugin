package org.rushland.plugin.listeners;

import com.google.inject.Inject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.rushland.api.interfaces.bukkit.ImprovedListener;
import org.rushland.utils.TimeUtils;

/**
 * Managed by romain on 27/11/2014.
 */
public class ClientDefaultListener extends ImprovedListener {
    @Inject
    JavaPlugin plugin;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDie(PlayerDeathEvent event) {
        final Player p = event.getEntity();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                try {
                    Object nmsPlayer = p.getClass().getMethod("getHandle").invoke(p);
                    Object packet = Class.forName(nmsPlayer.getClass().getPackage().getName() + ".PacketPlayInClientCommand").newInstance();
                    Class<?> enumClass = Class.forName(nmsPlayer.getClass().getPackage().getName() + ".EnumClientCommand");

                    for(Object ob : enumClass.getEnumConstants()){
                        if(ob.toString().equals("PERFORM_RESPAWN")){
                            packet = packet.getClass().getConstructor(enumClass).newInstance(ob);
                        }
                    }

                    Object con = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
                    con.getClass().getMethod("a", packet.getClass()).invoke(con, packet);
                } catch(Throwable t){
                    t.printStackTrace();
                }
            }
        }, TimeUtils.convertSecondsToTicks(1));
    }
}
