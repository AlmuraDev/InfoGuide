/*
 * This file is part of InfoGuide.
 *
 * Copyright (c) 2012, AlmuraDev <http://www.almuradev.com/>
 * InfoGuide is licensed under the Almura Development License.
 *
 * InfoGuide is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * InfoGuide is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License. If not,
 * see <http://www.gnu.org/licenses/> for the GNU General Public License.
 */
package net.dockter.infoguide;

import net.dockter.infoguide.gui.GUIGuide;
import net.dockter.infoguide.guide.GuideManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.keyboard.Keyboard;
import org.getspout.spoutapi.player.SpoutPlayer;
import org.mcstats.MetricsLite;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    private static Main instance;
    private static FileConfiguration bypass;
    public static String hotkeys = null;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        GuideManager.disable();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;
        FileConfiguration config = this.getConfig();
        config.addDefault("PromptTitle", "Info Guide");
        config.addDefault("TitleX", 190);
        config.addDefault("DisplayOnLogin", true);
        config.addDefault("Hot_Key", "KEY_F12");
        config.addDefault("GUITexture", "http://www.almuramc.com/images/playerplus.png");
        config.addDefault("DefaultGuide", "Initial");
        config.addDefault("GuestGuide", "Initial");
        config.addDefault("MemberGuide", "Initial");
        config.addDefault("SuperMemberGuide", "Initial");
        config.addDefault("ModeratorGuide", "Initial");
        config.options().copyDefaults(true);
        saveConfig();
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new GuideListener(this), this);
        File file = new File(this.getDataFolder() + File.separator + "users.yml");
        if (!file.exists()) {
            File init = new File(this.getDataFolder() + File.separator + "guides" + File.separator + "Initial.yml");
            init.getParentFile().mkdirs();
            try {
                init.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            YamlConfiguration yconf = YamlConfiguration.loadConfiguration(init);
            yconf.addDefault("Name", "Initial");
            yconf.addDefault("Date", "Future");
            yconf.addDefault("Pages.Nr1", "Page 1 of InfoGuide");
            yconf.options().copyDefaults(true);
            try {
                yconf.save(init);
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        bypass = YamlConfiguration.loadConfiguration(file);
        hotkeys = config.getString("Hot_Key");
        SpoutManager.getKeyBindingManager().registerBinding("InfoGuide", Keyboard.valueOf(Main.hotkeys), "Opens InfoGuide", new InfoGuideInputHandler(), Main.getInstance());
        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
        }
    }

    public static boolean isBypassing(String name) {
        return bypass.contains(name) && bypass.getBoolean(name);
    }

    public static boolean canBypass(String name) {
        return bypass.contains(name);
    }

    public void setBypass(String name, boolean value) {
        bypass.set(name, value);
        try {
            bypass.save(new File(this.getDataFolder() + File.separator + "users.yml"));
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {        
        if (cmd.getName().equalsIgnoreCase("infoguide")) {
            if (sender instanceof Player) {
                if (args.length == 0) {
                    ((SpoutPlayer) sender).getMainScreen().attachPopupScreen(new GUIGuide((SpoutPlayer) sender));
                    return true;
                } else if (args.length > 0) {
                    String arg = new String();

                    for (int i = 0; i < args.length; i++) {
                        arg += args[i] + " ";
                    }
                    if (GuideManager.getLoadedGuides().containsKey(arg.trim())) {
                        GUIGuide guide = new GUIGuide((SpoutPlayer) sender);
                        guide.setGuide(GuideManager.getLoadedGuides().get(arg.trim()));
                        ((SpoutPlayer) sender).getMainScreen().attachPopupScreen(guide);
                    } else {
                        ((SpoutPlayer) sender).sendMessage("Unable to open guide, did you spell it correctly?");
                    }
                    return true;
                }
            } else {
                sender.sendMessage("InfoGuide cannot be ran from the server console.");
            }
        }
        return false;
    }
}

