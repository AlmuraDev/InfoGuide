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

import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;

class Convertor {

	static void handle10(YamlConfiguration config, List<String> pgs) {
		addAndRemoveIfExists(config, pgs, "Pageone");
		addAndRemoveIfExists(config, pgs, "Pagetwo");
		addAndRemoveIfExists(config, pgs, "Pagethree");
		addAndRemoveIfExists(config, pgs, "Pagefour");
		addAndRemoveIfExists(config, pgs, "Pagefive");
		addAndRemoveIfExists(config, pgs, "Pagesix");
		addAndRemoveIfExists(config, pgs, "Pageseven");
		addAndRemoveIfExists(config, pgs, "Pageeight");
		addAndRemoveIfExists(config, pgs, "Pagenine");
		addAndRemoveIfExists(config, pgs, "Pageten");
		config.set("Text", null);
		config.createSection("Pages");
		for (Integer curPage = 1; curPage < pgs.size(); curPage++) {
			config.set("Pages.Nr" + curPage, pgs.get(curPage));
		}
	}

	private static void addAndRemoveIfExists(YamlConfiguration config, List<String> pgs, String pageone) {
		String value = config.getString(pageone);
		pgs.add(value);
		config.set(pageone, null);
	}
	
}
