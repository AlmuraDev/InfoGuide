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

import java.util.HashMap;
import java.util.UUID;

import net.dockter.infoguide.gui.GUIGuide;
import net.dockter.infoguide.guide.GuideManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.getspout.spoutapi.event.input.KeyPressedEvent;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.keyboard.Keyboard;
import org.getspout.spoutapi.player.SpoutPlayer;

public class GuideListener implements Listener {

	private Main instance;

	public static HashMap<UUID, Integer> PLAYERS = new HashMap<UUID, Integer>();
			
	public GuideListener(Main aThis) {
		instance = aThis;
	}

	
	@EventHandler
	public void onSpoutCraftEnable(SpoutCraftEnableEvent event) {		
		if (Main.getInstance().getConfig().getBoolean("DisplayOnLogin", true)) {			
			if (!(event.getPlayer().hasPermission("infoguide.bypassall"))) {  //OP's have this.				
				if (!(event.getPlayer().hasPermission("infoguide.bypass"))
						&& !instance.isBypassing(event.getPlayer().getName())) {
					final SpoutPlayer splr = event.getPlayer();
					PLAYERS.put(splr.getUniqueId(), Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable() {
						public void run() {									
							if (splr.isPreCachingComplete()) {								
								openInfoGuide(splr);
							}							
						}
					}, 20L, 20L));
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		SpoutPlayer sPlayer = (SpoutPlayer) event.getPlayer();
		sPlayer.getMainScreen().removeWidgets(instance);
	}
	
	public static void openInfoGuide(SpoutPlayer player) {
		GuideManager.load();
		if (PLAYERS.containsKey(player.getUniqueId())) {
			Bukkit.getScheduler().cancelTask(PLAYERS.get(player.getUniqueId()));
		}
		player.getMainScreen().attachPopupScreen(new GUIGuide(player));
	}
}
