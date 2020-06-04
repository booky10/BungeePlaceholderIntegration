package tk.t11e.bpi.bukkit;
// Created by booky10 in BungeePlaceholderIntegration (20:22 03.06.20)

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class Main extends JavaPlugin implements PluginMessageListener {

    @Override
    public void onEnable() {
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "bungee:placeholder");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "bungee:placeholder", this);
    }

    @SuppressWarnings({"NullableProblems", "UnstableApiUsage"})
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("bungee:placeholder")) return;
        ByteArrayDataInput dataInput = ByteStreams.newDataInput(message);
        String subchannel = dataInput.readUTF();
        String identifier = dataInput.readUTF();
        switch (subchannel) {
            case "containsPlaceholders":
                String containsPlaceholders = Boolean.toString(PlaceholderAPI.containsPlaceholders(dataInput.readUTF()));
                sendPluginMessage(player, subchannel, identifier, containsPlaceholders);
                break;
            case "containsBracketPlaceholders":
                String containsBracketPlaceholders = Boolean.toString(PlaceholderAPI.containsBracketPlaceholders(dataInput.readUTF()));
                sendPluginMessage(player, subchannel, identifier, containsBracketPlaceholders);
                break;
            case "setBracketPlaceholders":
                String bracketPlaceholders = PlaceholderAPI.setBracketPlaceholders(player, dataInput.readUTF());
                sendPluginMessage(player, subchannel, identifier, bracketPlaceholders);
                break;
            case "setPlaceholders":
                String placeholders = PlaceholderAPI.setPlaceholders(player, dataInput.readUTF());
                sendPluginMessage(player, subchannel, identifier, placeholders);
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    private void sendPluginMessage(Player player, String subchannel, String identifier, String text) {
        ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
        dataOutput.writeUTF(subchannel);
        dataOutput.writeUTF(identifier);
        dataOutput.writeUTF(text);
        player.sendPluginMessage(Main.getPlugin(Main.class), "bungee:placeholder", dataOutput.toByteArray());
    }
}