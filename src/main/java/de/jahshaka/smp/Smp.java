package de.jahshaka.smp;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import de.jahshaka.smp.commands.discord.CommandListener;
import de.jahshaka.smp.commands.server.PlaytimeCMD;
import de.jahshaka.smp.commands.server.PositionCMD;
import de.jahshaka.smp.listener.disocrd.ButtonClickListener;
import de.jahshaka.smp.listener.disocrd.MessageListener;
import de.jahshaka.smp.listener.disocrd.ShutDownListener;
import de.jahshaka.smp.listener.server.*;
import me.duncte123.botcommons.BotCommons;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.time.Instant;
import java.util.Objects;
import java.util.logging.Level;

public final class Smp extends JavaPlugin {

    public static JDA jda;

    public static WebhookClientBuilder builder;
    public static WebhookClient client;
    public static long startup;

    public static volatile boolean shutDown = false;


    public static Smp getPlugin() {
        return JavaPlugin.getPlugin(Smp.class);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        loadConfig();
        initDiscordBot();
        registerListener();
        registerCommands();
        getServer().getLogger().info("\n" +
                "   _____ __  __ _____        _____  _             _       \n" +
                "  / ____|  \\/  |  __ \\      |  __ \\| |           (_)      \n" +
                " | (___ | \\  / | |__) |_____| |__) | |_   _  __ _ _ _ __  \n" +
                "  \\___ \\| |\\/| |  ___/______|  ___/| | | | |/ _` | | '_ \\ \n" +
                "  ____) | |  | | |          | |    | | |_| | (_| | | | | |\n" +
                " |_____/|_|  |_|_|          |_|    |_|\\__,_|\\__, |_|_| |_|\n" +
                "                                             __/ |        \n" +
                "                                            |___/         \nby Jahshaka\n");

        this.getLogger().log(Level.INFO, "Successfully loaded plugin...");
        showTablist();
        startup = System.currentTimeMillis();
    }

    private void showTablist() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                final net.kyori.adventure.text.TextComponent footer = Component.text("Spieler online: " + getServer().getOnlinePlayers().size(), NamedTextColor.GRAY);
                Smp.getPlugin().getServer().sendPlayerListFooter(footer);
            }
        }, 60, 60);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().log(Level.INFO, Config.calculateUptime(startup));

        // Discord shutdown logic
        client.close();
        shutdownBot();
    }


    void loadConfig() {
        this.saveDefaultConfig();
        this.saveConfig();
    }

    void shutdownBot() {
        EmbedBuilder shutdown_embed = new EmbedBuilder().setColor(Color.red)
                .setTitle("Der Server wurde gestoppt!").setTimestamp(Instant.now())
                .setAuthor(jda.getSelfUser().getName(), jda.getSelfUser().getAvatarUrl(), jda.getSelfUser().getAvatarUrl());
        jda.getGuildById(Config.guildID).getTextChannelById(Config.announcementChannelID).sendMessageEmbeds(shutdown_embed.build()).queue();
        BotCommons.shutdown(jda);
        while (!shutDown) {
            try {
                Thread.sleep(1000);
                getLogger().log(Level.INFO, "JDA shutting down... please wait");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void initDiscordBot() {
        if (this.getConfig().get("discord_token") != null) {
            if (!this.getConfig().get("discord_token").toString().equals("<enter token here>")) {
                try {
                    jda = JDABuilder.createDefault(
                                    Objects.requireNonNull(this.getConfig().get("discord_token")).toString(),
                                    GatewayIntent.GUILD_MEMBERS,
                                    GatewayIntent.GUILD_MESSAGES
                            ).disableCache(CacheFlag.EMOTE, CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.VOICE_STATE)
                            .setActivity(Activity.playing("Minecraft!"))
                            .setMemberCachePolicy(MemberCachePolicy.ONLINE)
                            .addEventListeners(new CommandListener())
                            .addEventListeners(new ShutDownListener())
                            .addEventListeners(new ButtonClickListener())
                            .addEventListeners(new MessageListener())
                            .setMemberCachePolicy(MemberCachePolicy.ALL)
                            .setChunkingFilter(ChunkingFilter.ALL)
                            .build();
                    EmbedUtils.setEmbedBuilder(() -> new EmbedBuilder().setColor(Color.magenta).setFooter(Smp.jda.getSelfUser().getName(), Smp.jda.getSelfUser().getAvatarUrl()));
                } catch (LoginException e) {
                    e.printStackTrace();
                }
            } else {
                this.getLogger().log(Level.WARNING, "discord_token was not set in config.yml!");
            }
        } else {
            this.getConfig().set("discord_token", "<enter token here>");
            this.saveConfig();
            this.getLogger().log(Level.WARNING, "discord_token was not set in config.yml!");
        }

        builder = new WebhookClientBuilder(Config.webhookURL);
        builder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("test");
            thread.setDaemon(true);
            return thread;
        });
        builder.setWait(true);
        client = builder.build();

    }

    void registerListener() {
        getServer().getPluginManager().registerEvents(new ChatListenerServer(), this);
        getServer().getPluginManager().registerEvents(new PlayerConnectListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerBlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerChestInteractListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDieListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerSleepListener(), this);
        getServer().getPluginManager().registerEvents(new SpawnerInteractListener(), this);
    }

    void registerCommands() {
        getCommand("position").setExecutor(new PositionCMD());
        getCommand("playtime").setExecutor(new PlaytimeCMD());
    }


}
