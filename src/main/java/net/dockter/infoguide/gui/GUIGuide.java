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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import net.dockter.infoguide.Main;
import net.dockter.infoguide.guide.Guide;
import net.dockter.infoguide.guide.GuideManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.gui.CheckBox;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.ComboBox;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTextField;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.Screen;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

public class GUIGuide extends GenericPopup {

	final GenericTextField guideField, guideInvisible;
	final GenericLabel guideName, guideDate, pagelabel, pagetext;
	final public static HashMap<Player, Guide> map = new HashMap<Player, Guide>();
	public int pageno = 1;
	final GenericButton close, newb, saveb, deleteb, pd, pu, editb, deletepb;
	final ComboBox box;
	final CheckBox checkBox;
	private final SpoutPlayer player;
	public final Logger log = Logger.getLogger("Minecraft");
	private static Color redText = new Color(0.69f,0.09f,0.12f,1f);
	private static Color whiteText = new Color(1f,1f,1f,1f);

	public GUIGuide(SpoutPlayer player) {
		super();
		this.player = player;

		GenericLabel label = new GenericLabel();
		label.setText(Main.getInstance().getConfig().getString("PromptTitle"));
		label.setAnchor(WidgetAnchor.CENTER_CENTER);
		label.shiftXPos(-35).shiftYPos(-122);	
		label.setScale(1.2F).setWidth(-1).setHeight(-1);

		guideName = new GenericLabel("TheGuideNameHere");
		guideName.setWidth(-1).setHeight(-1);
		guideName.setAnchor(WidgetAnchor.CENTER_CENTER);
		guideName.shiftXPos(-200).shiftYPos(-100);
		guideName.setScale(1.3F);

		guideInvisible = new GenericTextField();
		guideInvisible.setWidth(150).setHeight(16);
		guideInvisible.setAnchor(WidgetAnchor.CENTER_CENTER);
		guideInvisible.shiftXPos(-200).shiftYPos(-102);
		guideInvisible.setMaximumCharacters(30);
		guideInvisible.setMaximumLines(1);
		guideInvisible.setVisible(false);

		guideDate = new GenericLabel("Updated: " + new SimpleDateFormat("HH:mm dd-MM").format(Calendar.getInstance().getTime()));
		guideDate.setWidth(-1).setHeight(-1);
		guideDate.setAnchor(WidgetAnchor.CENTER_CENTER);
		guideDate.shiftXPos(-200).shiftYPos(90);

		box = new MyCombo(this);
		box.setText("Guides");
		box.setAnchor(WidgetAnchor.CENTER_CENTER);
		box.setWidth(GenericLabel.getStringWidth("12345678901234567890123459"));
		box.setHeight(18);
		box.shiftXPos(25).shiftYPos(-105);
		box.setAuto(true);		
		box.setPriority(RenderPriority.Low);
		refreshItems();

		GenericTexture border = new GenericTexture(Main.getInstance().getConfig().getString("GUITexture"));
		border.setAnchor(WidgetAnchor.CENTER_CENTER);
		border.setPriority(RenderPriority.High);
		border.setWidth(626).setHeight(240);
		border.shiftXPos(-220).shiftYPos(-128);

		guideField = new GenericTextField();
		guideField.setText("first guide goes here"); // The default text
		guideField.setAnchor(WidgetAnchor.CENTER_CENTER);
		guideField.setBorderColor(new Color(1.0F, 1.0F, 1.0F, 1.0F)); // White border
		guideField.setMaximumCharacters(1200);
		guideField.setMaximumLines(13);
		guideField.setHeight(160).setWidth(377);
		guideField.shiftXPos(-195).shiftYPos(-83);
		guideField.setMargin(0);
		guideField.setFocus(true);

		close = new CloseButton(this);
		close.setAuto(true);
		close.setAnchor(WidgetAnchor.CENTER_CENTER);
		close.setHeight(18).setWidth(40);
		close.shiftXPos(142).shiftYPos(85);  // New Y

		pu = new PageUpButton(this);
		pu.setAuto(true).setText("<<<");
		pu.setAnchor(WidgetAnchor.CENTER_CENTER);
		pu.setHeight(18).setWidth(40);
		pu.shiftXPos(17).shiftYPos(85);

		pagelabel = new GenericLabel();
		pagelabel.setText(Integer.toString(pageno));
		pagelabel.setAnchor(WidgetAnchor.CENTER_CENTER);
		pagelabel.shiftXPos(66).shiftYPos(92);
		pagelabel.setPriority(RenderPriority.Normal);
		pagelabel.setWidth(5).setHeight(18);
		
		pagetext = new GenericLabel();
		pagetext.setText("Page");
		pagetext.setAnchor(WidgetAnchor.CENTER_CENTER);
		pagetext.shiftXPos(62).shiftYPos(85);
		pagetext.setPriority(RenderPriority.Normal);
		pagetext.setWidth(5).setHeight(18);
		pagetext.setScale(0.6F);

		pd = new PageDownButton(this);
		pd.setAuto(true).setText(">>>");
		pd.setAnchor(WidgetAnchor.CENTER_CENTER);
		pd.setHeight(18).setWidth(40);
		pd.shiftXPos(82).shiftYPos(85);

		checkBox = new BypassCheckBox(player, this);
		checkBox.setText("Bypass");
		checkBox.setAnchor(WidgetAnchor.CENTER_CENTER);
		checkBox.setHeight(20).setWidth(19);
		checkBox.shiftXPos(-52).shiftYPos(83);
		checkBox.setAuto(true);

		this.setTransparent(true);
		attachWidget(Main.getInstance(), label);
		attachWidget(Main.getInstance(), border);
		attachWidget(Main.getInstance(), guideField);
		attachWidget(Main.getInstance(), close);
		attachWidget(Main.getInstance(), pu);
		attachWidget(Main.getInstance(), pagelabel);
		attachWidget(Main.getInstance(), pagetext);
		attachWidget(Main.getInstance(), pd);
		attachWidget(Main.getInstance(), guideName);
		attachWidget(Main.getInstance(), guideInvisible);
		attachWidget(Main.getInstance(), guideDate);
		attachWidget(Main.getInstance(), box);
		if (Main.getInstance().canBypass(player.getName()) || player.hasPermission("infoguide.bypass") || player.hasPermission("infoguide.admin")) {
			attachWidget(Main.getInstance(), checkBox);
		}

		if (player.hasPermission("infoguide.edit") || player.hasPermission("infoguide.admin")) {
			saveb = new SaveButton(this);
			saveb.setAnchor(WidgetAnchor.CENTER_CENTER);
			saveb.setAuto(true).setHeight(18).setWidth(40).shiftXPos(-195).shiftYPos(85);
			attachWidget(Main.getInstance(), saveb);
		} else {
			saveb = null;
		}

		if (player.hasPermission("infoguide.create") || player.hasPermission("infoguide.edit") || player.hasPermission("infoguide.admin")) {
			guideDate.setVisible(false);
			newb = new NewButton(this);			
			newb.setAnchor(WidgetAnchor.CENTER_CENTER);
			newb.setAuto(true).setHeight(13).setWidth(35).shiftXPos(-200).shiftYPos(-120);
			attachWidget(Main.getInstance(), newb);
		} else {
			newb = null;
			guideDate.setVisible(true);
		}

		if (player.hasPermission("infoguide.edit") || player.hasPermission("infoguide.admin")) {
			editb = new EditButton(this);			
			editb.setAnchor(WidgetAnchor.CENTER_CENTER);
			editb.setAuto(true).setHeight(13).setWidth(55).shiftXPos(-162).shiftYPos(-120);			
			attachWidget(Main.getInstance(), editb);
		} else {
			editb = null;
		}
		
		if (player.hasPermission("infoguide.delete") || player.hasPermission("infoguide.admin")) {
			deleteb = new DeleteButton(this);			
			deleteb.setAnchor(WidgetAnchor.CENTER_CENTER);
			deleteb.setAuto(true).setHeight(13).setWidth(45).shiftXPos(-103).shiftYPos(-120);		
			
			deletepb = new DeletePageButton(this);
			deletepb.setAnchor(WidgetAnchor.CENTER_CENTER);
			deletepb.setAuto(true).setHeight(18).setWidth(70).shiftXPos(-140).shiftYPos(85);
			deletepb.setVisible(false);
			
			attachWidget(Main.getInstance(), deleteb);
			attachWidget(Main.getInstance(), deletepb);
		} else {
			deleteb = null;
			deletepb = null;
		}
		
		if (player.hasPermission("infoguide.moderatorguide")) {
			setGuide(GuideManager.getLoadedGuides().get(Main.getInstance().getConfig().getString("ModeratorGuide")));
			return;
		} else if (player.hasPermission("infoguide.supermemberguide")) {
			setGuide(GuideManager.getLoadedGuides().get(Main.getInstance().getConfig().getString("SuperMemberGuide")));
			return;
		} else if (player.hasPermission("infoguide.memberguide")) {
			setGuide(GuideManager.getLoadedGuides().get(Main.getInstance().getConfig().getString("MemberGuide")));
			return;
		} else if (player.hasPermission("infoguide.guestguide")) {
			setGuide(GuideManager.getLoadedGuides().get(Main.getInstance().getConfig().getString("GuestGuide")));
			return;
		} else {
			setGuide(GuideManager.getLoadedGuides().get(Main.getInstance().getConfig().getString("DefaultGuide")));
		}		
	}
	private Guide guide;

	public void setGuide(Guide guide) {
		buttonResets();
		if (guide == null) {
			return;
		}
		this.guide = guide;
		guideDate.setText("Updated: " + guide.getDate());
		guideName.setText(guide.getName()).setWidth(-1);
		guideInvisible.setText(guide.getName());
		map.put(player, guide);
		pageno = 1;
		pagelabel.setText(Integer.toString(pageno));
		guideField.setText(guide.getPage(1));
		guideField.setFocus(true);
		pd.setVisible(true);		
		if (pageno == 1) {
			pu.setVisible(false);		
		}
		if (pageno == guide.getPages()) {
			if (player.hasPermission("infoguide.edit") || player.hasPermission("infoguide.admin")) {
				pd.setText("+");
				pd.setDirty(true);
				pagelabel.setVisible(true);
				pagetext.setVisible(true);
			} else {
			
			pd.setVisible(false);
			pd.setDirty(true);
			pagelabel.setVisible(false);
			pagetext.setVisible(false);
			}
		} else {
			pd.setText(">>>");
			pd.setVisible(true);
			pd.setDirty(true);
			pagelabel.setVisible(true);
			pagetext.setVisible(true);
		}		
	}

	public void pageUp() {
		buttonResets();
		pageno = pageno - 1;
		if (pageno == 0) {
			pageno = 1;
		}
		if (pageno == 1) {
			pu.setVisible(false);
			if (player.hasPermission("infoguide.edit") || player.hasPermission("infoguide.admin")) {
				deletepb.setVisible(false);
			}
		} else {
			pu.setVisible(true);			
		}
		Guide gguide = map.get(player);
		if (pageno != gguide.getPages()) {
			pd.setText(">>>");
			pd.setVisible(true);			
		} else {
			if (player.hasPermission("infoguide.edit") || player.hasPermission("infoguide.admin")) {				
				pd.setText("+");
				pd.setVisible(true);
				pd.setDirty(true);
				if (pageno == 1) {
					deletepb.setVisible(false);
					deletepb.setDirty(true);
				} else {
					deletepb.setVisible(true);
					deletepb.setDirty(true);
				}
			}
		}
		pd.setDirty(true);
		guideField.setText(gguide.getPage(pageno));
		pagelabel.setText(Integer.toString(pageno));
		guideField.setFocus(true);
	}

	public void pageDown() {
		buttonResets();
		pageno++;
		Guide gguide = map.get(player);
		if (pd.getText().equalsIgnoreCase("+")) {			
			if (player.hasPermission("infoguide.edit") || player.hasPermission("infoguide.admin")) {
				gguide.addPage();				
				pd.setText(">>>");
				pd.setVisible(false);
			}
			pd.setDirty(true);			
		}
		if (pageno == guide.getPages()) {
			if (player.hasPermission("infoguide.edit") || player.hasPermission("infoguide.admin")) {
				pd.setText("+");
				pd.setDirty(true);
				deletepb.setVisible(true);
				deletepb.setDirty(true);
			} else {
			pd.setVisible(false);
			pd.setDirty(true);
			}
		} else {
			pd.setText(">>>");
			pd.setVisible(true);
			pd.setDirty(true);			
		}
		pu.setVisible(true);
		pu.setDirty(true);
		guideField.setText(gguide.getPage(pageno));
		pagelabel.setText(Integer.toString(pageno));
		guideField.setFocus(true);
	}

	public void onNewClick() {
		buttonResets();
		setGuide(new Guide("", "", new ArrayList<String>()));
		guideName.setVisible(false);
		guideInvisible.setVisible(true);
		guideInvisible.setText("New Guide Name Here");
	}

	void onSaveClick(String playerName) {
		buttonResets();
		Guide gguide = map.get(player);
				
		gguide.setPage(pageno, guideField.getText());

		gguide.setDate(new SimpleDateFormat("HH:mm dd-MM").format(Calendar.getInstance().getTime()));
		if (guideInvisible.isVisible()) {
			gguide.setName(guideInvisible.getText());
			guideName.setText(guideInvisible.getText()).setWidth(-1);
			guideInvisible.setVisible(false);
			guideName.setVisible(true);
			GuideManager.addGuide(gguide);
		}
		
		Bukkit.broadcastMessage(ChatColor.GOLD + player.getDisplayName() + ChatColor.YELLOW + " updated the guide " + ChatColor.GOLD + guide.getName() + ChatColor.YELLOW + " on page "+pageno+"!");
		gguide.save();
		refreshItems();
		guide.prepareForLoad();
		map.put(player, guide);
		pagelabel.setText(Integer.toString(pageno));
		guideField.setText(guide.getPage(pageno));
		
	}

	void onDeleteClick() {
		if (deleteb.getText().equalsIgnoreCase("delete")) {
			deleteb.setText("Confirm?");
			deleteb.setColor(redText);
			deleteb.setDirty(true);
		} else {		
			if (box.getItems().size() == 1) {
				return;
			}
			GuideManager.removeLoadedGuide(guideName.getText());
			refreshItems();
			setGuide(GuideManager.getLoadedGuides().get(box.getItems().get(0)));	
			buttonResets();			
		}
	}
	
	void onDeletePage() {
		if (deletepb.getText().equalsIgnoreCase("delete page")) {			
			deletepb.setText("Confirm?");
			deletepb.setColor(redText);
			deletepb.setDirty(true);
		} else {		
			Guide gguide = map.get(player);
			gguide.deletePage(pageno);			
			gguide.save();	
			gguide.prepareForLoad();
			pageUp();
		}
	}

	void onCloseClick() {
		Screen screen = ((SpoutPlayer) player).getMainScreen();
		screen.removeWidget(this);
		//player.getMainScreen().closePopup();
		player.closeActiveWindow();

	}

	void onSelect(int i, String text) {
		setGuide(GuideManager.getLoadedGuides().get(text));
	}
	
	private void refreshItems() {		
		List<String> items = new ArrayList<String>();
		for (String gguide : GuideManager.getLoadedGuides().keySet()) {
			if (player.hasPermission("infoguide.view." + gguide) || player.hasPermission("infoguide.view")) {
				items.add(gguide);  // Add All Guides that are available to the user.
			}
			if (player.hasPermission("infoguide.hide." + gguide)) {
				if (items.contains(gguide)) {					
					items.remove(gguide); // Remove Specific Guides that are suppose to be hidden.
				}
			}
			if (player.hasPermission("infoguide.view." + gguide)  || player.hasPermission("infoguide.admin")) {
				if (!items.contains(gguide)) {
					items.add(gguide); // Override inherited Hide Permissions				
				}
			}
				
			
		}
		Collections.sort(items, String.CASE_INSENSITIVE_ORDER);
		box.setItems(items);
		box.setDirty(true);
	}

	public void onEditClick() {
		buttonResets();
		if (!guideInvisible.isVisible()) { 
			// Edit Name
			guideName.setVisible(false);
			guideInvisible.setVisible(true);
			saveb.setVisible(false);
			editb.setText("Save");	
			editb.setDirty(true);
		} else {
			// Save Name
			Guide gguide = map.get(player);
			GuideManager.removeLoadedGuide(guideName.getText());
			Bukkit.broadcastMessage(ChatColor.GOLD + player.getDisplayName() + ChatColor.YELLOW + " has renamed " + ChatColor.GOLD + guideName.getText() + ChatColor.YELLOW + " to " + ChatColor.GOLD + guideInvisible.getText());
			gguide.setName(guideInvisible.getText());		
			GuideManager.addGuide(gguide);
			gguide.save();
			refreshItems();
			guide.prepareForLoad();
			map.put(player, guide);
			pagelabel.setText(Integer.toString(pageno));
			guideField.setText(guide.getPage(pageno));
			
			guideName.setText(guideInvisible.getText());
			guideName.setVisible(true);
			guideInvisible.setVisible(false);
			editb.setText("Edit");	
			editb.setDirty(true);
			saveb.setVisible(true);
		}
	}
	
	void buttonResets() {  // Resets modified buttons to their original states.
		if (player.hasPermission("infoguide.delete") || player.hasPermission("infoguide.admin")) {
			deleteb.setText("Delete");
			deleteb.setColor(whiteText);
			deleteb.setDirty(true);
			
			deletepb.setText("Delete Page");
			deletepb.setColor(whiteText);
			deletepb.setVisible(false);			
			deletepb.setDirty(true);		
		}
	}
}
