package de.jahshaka.smp.commands.discord;

import de.jahshaka.smp.Config;
import de.jahshaka.smp.Smp;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.awt.*;
import java.time.Instant;

public class CommandListener extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandListener.class);
    static Smp smp = Smp.getPlugin();
    private final CommandManager manager = new CommandManager();

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        JDA jda = Smp.jda;
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
        if (smp.getConfig().getInt("restartMessage") == 0) {
            return;
        }
        EmbedBuilder shutdown_embed = new EmbedBuilder().setColor(Color.green)
                .setTitle("Der Server wurde gestartet und ist nun online!").setTimestamp(Instant.now())
                .setAuthor(jda.getSelfUser().getName(), jda.getSelfUser().getAvatarUrl(), jda.getSelfUser().getAvatarUrl());
        jda.getGuildById(Config.guildID).getTextChannelById(Config.announcementChannelID).sendMessageEmbeds(shutdown_embed.build()).queue();
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        String prefix = Smp.getPlugin().getConfig().get("discord_prefix").toString();
        String raw = event.getMessage().getContentRaw();

        if (raw.equalsIgnoreCase(prefix + "shutdown")
                && user.getId().equals(Smp.getPlugin().getConfig().get("owner_id").toString())) {
            LOGGER.info("Shutting down");
            event.getJDA().shutdown();
            BotCommons.shutdown(event.getJDA());
            return;
        }

        if (raw.startsWith(prefix)) {
            manager.handle(event);
        }
    }

}