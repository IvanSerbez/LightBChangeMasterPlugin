package org.Gornichnaya.lightBChangeMaster;

import org.Gornichnaya.lightBChangeMaster.Listeners.BlockChangeListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class LightBChangeMaster extends JavaPlugin {


    @Override
    public void onEnable() {

        getServer().getPluginManager().registerEvents(new BlockChangeListener(this), this);
        System.out.println("LightBChangeMaster is enabled");


    }

    @Override
    public void onDisable() {
        System.out.println("LightBChangeMaster is disabled");

    }
}
