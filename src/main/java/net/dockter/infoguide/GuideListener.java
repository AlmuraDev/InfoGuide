package net.dockter.infoguide;

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
	
	public static int taskId = 0;
			
	public GuideListener(Main aThis) {
		instance = aThis;
	}

	
	@EventHandler
	public void onSpoutCraftEnable(SpoutCraftEnableEvent event) {
		System.out.println("onSpoutCraft Seen");
		if (Main.getInstance().getConfig().getBoolean("DisplayOnLogin", true)) {
			System.out.println("onSpoutCraft Step2");
			if (!(event.getPlayer().hasPermission("infoguide.bypassall"))) {  //OP's have this.
				System.out.println("onSpoutCraft Step3");
				if (!(event.getPlayer().hasPermission("infoguide.bypass"))
						&& !instance.isBypassing(event.getPlayer().getName())) {
					final SpoutPlayer splr = (SpoutPlayer) event.getPlayer();						
					taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance,
							new Runnable() {
						public void run() {									
							if (splr.isPreCachingComplete()) {								
								openInfoGuide(splr);
							}							
						}
					}, 20L, 20L);
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
		Bukkit.getScheduler().cancelTask(taskId);		
		player.getMainScreen().attachPopupScreen(new GUIGuide(player));
	}
}
