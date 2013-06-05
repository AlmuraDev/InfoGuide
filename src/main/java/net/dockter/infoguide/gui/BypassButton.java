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
package net.dockter.infoguide.gui;

import net.dockter.infoguide.Main;
import org.bukkit.ChatColor;
import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.player.SpoutPlayer;

public class BypassButton extends GenericButton {

	private GUIGuide plugin;
	private SpoutPlayer splr;

	public BypassButton(SpoutPlayer player, GUIGuide plugin) {
		super();
		setTooltip("If this button is green, the guide screen will you login!");
		splr = player;
		ChatColor ccolor;
		if (Main.getInstance().isBypassing(splr.getName())) {
			ccolor = ChatColor.GREEN;
		} else {
			ccolor = ChatColor.WHITE;
		}
		setText(ccolor + "B");
		this.plugin = plugin;
	}

	@Override
	public void onButtonClick(ButtonClickEvent event) {
		Main.getInstance().setBypass(splr.getName(), !Main.getInstance().isBypassing(splr.getName()));
		ChatColor ccolor;
		if (Main.getInstance().isBypassing(splr.getName())) {
			ccolor = ChatColor.GREEN;
		} else {
			ccolor = ChatColor.WHITE;
		}
		setText(ccolor + "B");
		setDirty(true);
	}
}
