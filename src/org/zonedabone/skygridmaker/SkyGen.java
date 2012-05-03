package org.zonedabone.skygridmaker;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SkyGen extends ChunkGenerator {
	
	@Override
	public Location getFixedSpawnLocation(World world, Random random) {
		return world.getBlockAt(0, 129, 0).getLocation();
	}
	
	@Override
	public boolean canSpawn(World world, int x, int z) {
		return (x % 4 == 0 && z % 4 == 0);
	}
	byte[] blocks = new byte[]{1, 2, 3, 4, 7, 8, 10, 12, 13, 14, 15, 16, 17, 18, 21, 24, 35, 54};
	byte[] naturals = new byte[]{6, 37, 38, 39, 40};
	private SkyGridMaker plugin;
	
	public SkyGen(SkyGridMaker plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public byte[][] generateBlockSections(World world, Random random, int cx, int cz, BiomeGrid biomes) {
		byte[][] result = new byte[world.getMaxHeight() / 16][];
		for (int i = 0; i < result.length; i++) {
			result[i] = new byte[4096];
		}
		for (int x = 0; x < 16; x += 4) {
			for (int y = 0; y < world.getMaxHeight(); y += 4) {
				for (int z = 0; z < 16; z += 4) {
					byte b = blocks[(int) (Math.random() * blocks.length)];
					setBlock(result, x, y, z, b);
					if (b == 2 || b == 3) {
						try {
							setBlock(result, x, y + 1, z, naturals[(int) (Math.random() * naturals.length)]);
						} catch (ArrayIndexOutOfBoundsException e) {
						}
					} else if (b == 54) {
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new ChestFiller(cx, cz, x, y, z, plugin, world));
					} else if (b == 35) {
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new WoolMaker(cx, cz, x, y, z, plugin, world));
					}
				}
			}
		}
		return result;
	}
	
	void setBlock(byte[][] result, int x, int y, int z, byte blkid) {
		if (result[y >> 4] == null) {
			result[y >> 4] = new byte[4096];
		}
		result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = blkid;
	}
	
	byte getBlock(byte[][] result, int x, int y, int z) {
		if (result[y >> 4] == null) {
			return (byte) 0;
		}
		return result[y >> 4][((y & 0xF) << 8) | (z << 4) | x];
	}
	private class ChestFiller implements Runnable {
		
		private int cx, cz, x, y, z;
		private World w;
		
		public ChestFiller(int cx, int cz, int x, int y, int z, SkyGridMaker plugin, World w) {
			this.cx = cx;
			this.cz = cz;
			this.x = x;
			this.y = y;
			this.z = z;
			this.w = w;
		}
		
		@Override
		public void run() {
			Inventory inv = ((Chest) w.getChunkAt(cx, cz).getBlock(x, y, z).getState()).getBlockInventory();
			for (int i = 0; i < Math.random() * inv.getSize(); i++) {
				inv.setItem((int) (Math.random() * inv.getSize()), new ItemStack((int) (Math.random() * 100), (int) (Math.random() * 10)));
			}
		}
	}
	private class WoolMaker implements Runnable {
		
		private int cx, cz, x, y, z;
		private World w;
		
		public WoolMaker(int cx, int cz, int x, int y, int z, SkyGridMaker plugin, World w) {
			this.cx = cx;
			this.cz = cz;
			this.x = x;
			this.y = y;
			this.z = z;
			this.w = w;
		}
		
		@Override
		public void run() {
			w.getChunkAt(cx, cz).getBlock(x, y, z).setData((byte) (Math.random() * 15));
		}
	}
}
