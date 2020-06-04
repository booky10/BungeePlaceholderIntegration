package tk.t11e.bpi.bungee;
// Created by booky10 in BungeePlaceholderIntegration (20:22 03.06.20)

import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {

    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerListener(this, new BungeePlaceholderAPI());
        getProxy().registerChannel("bungee:placeholder");
    }
}