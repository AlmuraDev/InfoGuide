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
package net.dockter.infoguide.guide;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import net.dockter.infoguide.Main;
import org.bukkit.Bukkit;

public class GuideManager {

	private static final Map<String, Guide> loadedGuides = new HashMap<String, Guide>();
	
	public static void disable() {
		for (Guide guide : loadedGuides.values()) {
			guide.save();
		}
	}

	public static Map<String, Guide> getLoadedGuides() {
		return loadedGuides;
	}

	public static void addGuide(Guide newGuide) {
		loadedGuides.put(newGuide.getName(), newGuide);
	}

	public static void removeLoadedGuide(String text) {
		if (!(loadedGuides.containsKey(text))) {
			return;
		}
		loadedGuides.get(text).delete();
		loadedGuides.remove(text);
	}

	public static void load() {
		loadedGuides.clear();
		File dir = new File("plugins" + File.separator + "InfoGuide" + File.separator + "guides");
		dir.mkdirs();
		for (File file : dir.listFiles()) {
			Guide g = Guide.load(file);
			loadedGuides.put(g.getName(), g);
		}
		if (loadedGuides.isEmpty()) {
			System.out.println("No guides detected, turning off!");
			Bukkit.getServer().getPluginManager().disablePlugin(Main.getInstance());
		}
	}
}
