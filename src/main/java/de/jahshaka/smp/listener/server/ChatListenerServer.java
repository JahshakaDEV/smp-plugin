package de.jahshaka.smp.listener.server;

import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import de.jahshaka.smp.Config;
import de.jahshaka.smp.Smp;
import de.jahshaka.smp.methods.AllowedPlayerList;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.dv8tion.jda.api.entities.Member;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.IOException;

public class ChatListenerServer implements Listener {

    @EventHandler
    public static void onAsyncChat(AsyncChatEvent event) throws IOException {
        String msg = PlainTextComponentSerializer.plainText().serialize(event.message());
        Smp.jda.getGuildById(Config.guildID).getTextChannelById(Config.chatLogChannelID)
                .sendMessage(event.getPlayer().getName() + ": `" + msg
                        + "` " + Config.timestampForLogs).queue();
        sendMessageToMinecraftChatChannel(msg, event);
    }

    public static void sendMessageToMinecraftChatChannel(String msg, AsyncChatEvent event) {
        Member member = AllowedPlayerList.getMemberFromUniqueID(event.getPlayer().getUniqueId().toString(), Smp.getPlugin());
        if (member == null) return;
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.setUsername(member.getEffectiveName());
        builder.setAvatarUrl(member.getEffectiveAvatarUrl());
        builder.setContent("[SERVER]: " + msg);
        Smp.client.send(builder.build());
    }

}
