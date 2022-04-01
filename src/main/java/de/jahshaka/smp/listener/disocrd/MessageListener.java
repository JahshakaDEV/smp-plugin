package de.jahshaka.smp.listener.disocrd;

import de.jahshaka.smp.Config;
import de.jahshaka.smp.Smp;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        User user = event.getAuthor();
        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }
        if (event.getChannel().getId().equals(Config.channelForMcChatUserID)) {
            String msg = event.getMessage().getContentRaw();
            String author = event.getMember().getEffectiveName();
            final TextComponent message = Component.text("[", NamedTextColor.GRAY)
                    .append(Component.text("DISCORD", NamedTextColor.BLUE))
                    .append(Component.text("] ", NamedTextColor.GRAY))
                    .append(Component.text(author + ": ", NamedTextColor.GRAY))
                    .append(Component.text(msg, NamedTextColor.WHITE));
            Smp.getPlugin().getServer().sendMessage(message);
        }
    }

}
