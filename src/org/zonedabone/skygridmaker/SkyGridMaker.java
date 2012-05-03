package org.zonedabone.skygridmaker;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class SkyGridMaker extends JavaPlugin implements Listener {
	
	public final static BlockFace[] faces = new BlockFace[]{BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		return new SkyGen(this);
	}
	
	@EventHandler
	public void onBlockFromTo(BlockFromToEvent e) {
		if (e.getBlock().getWorld().getGenerator() != null && e.getBlock().getWorld().getGenerator().getClass().getName().equals("org.zonedabone.skygridmaker.SkyGen")) {
			Block b = e.getBlock();
			boolean cancel = true;
			for (BlockFace bf : faces) {
				if (b.getRelative(bf).getType() != Material.AIR) {
					cancel = false;
				}
			}
			if (cancel) {
				e.setCancelled(true);
			}
		}
	}
}
