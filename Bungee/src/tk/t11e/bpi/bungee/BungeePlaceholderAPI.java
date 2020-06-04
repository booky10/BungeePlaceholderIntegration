package tk.t11e.bpi.bungee;
// Created by booky10 in BungeePlaceholderIntegration (20:24 03.06.20)

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;
import java.util.UUID;

public class BungeePlaceholderAPI implements Listener {

    private static final HashMap<String, CompletableInFuture<Boolean>> booleanFutures = new HashMap<>();
    private static final HashMap<String, CompletableInFuture<String>> stringFutures = new HashMap<>();

    public static CompletableInFuture<Boolean> containsPlaceholders(ProxiedPlayer player, String text) {
        String identifier = UUID.randomUUID().toString();
        sendPluginMessage(player, "containsPlaceholders", identifier, text);
        CompletableInFuture<Boolean> future = new CompletableInFuture<>();
        booleanFutures.put(identifier, future);
        return future;
    }

    public static CompletableInFuture<Boolean> containsBracketPlaceholders(ProxiedPlayer player, String text) {
        String identifier = UUID.randomUUID().toString();
        sendPluginMessage(player, "containsBracketPlaceholders", identifier, text);
        CompletableInFuture<Boolean> future = new CompletableInFuture<>();
        booleanFutures.put(identifier, future);
        return future;
    }

    public static CompletableInFuture<String> setBracketPlaceholders(ProxiedPlayer player, String text) {
        String identifier = UUID.randomUUID().toString();
        sendPluginMessage(player, "setBracketPlaceholders", identifier, text);
        CompletableInFuture<String> future = new CompletableInFuture<>();
        stringFutures.put(identifier, future);
        return future;
    }

    public static CompletableInFuture<String> setPlaceholders(ProxiedPlayer player, String text) {
        String identifier = UUID.randomUUID().toString();
        sendPluginMessage(player, "setPlaceholders", identifier, text);
        CompletableInFuture<String> future = new CompletableInFuture<>();
        stringFutures.put(identifier, future);
        return future;
    }

    @SuppressWarnings("UnstableApiUsage")
    private static void sendPluginMessage(ProxiedPlayer player, String subchannel, String identifier, String text) {
        ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
        dataOutput.writeUTF(subchannel);
        dataOutput.writeUTF(identifier);
        dataOutput.writeUTF(text);
        player.sendData("bungee:placeholder", dataOutput.toByteArray());
        player.getServer().sendData("bungee:placeholder", dataOutput.toByteArray());
    }

    @SuppressWarnings("UnstableApiUsage")
    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().equals("bungee:placeholder")) return;
        ByteArrayDataInput dataInput = ByteStreams.newDataInput(event.getData());
        String subchannel = dataInput.readUTF();
        String identifier = dataInput.readUTF();
        switch (subchannel) {
            case "containsPlaceholders":
            case "containsBracketPlaceholders":
                Boolean containsPlaceholders = Boolean.parseBoolean(dataInput.readUTF());
                booleanFutures.get(identifier).setValue(containsPlaceholders);
                booleanFutures.remove(identifier);
                break;
            case "setBracketPlaceholders":
            case "setPlaceholders":
                String placeholders = dataInput.readUTF();
                stringFutures.get(identifier).setValue(placeholders);
                stringFutures.remove(identifier);
                break;
            default:
                break;
        }
    }
}