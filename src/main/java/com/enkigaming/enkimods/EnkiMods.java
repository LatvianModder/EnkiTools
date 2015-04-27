package com.enkigaming.enkimods;

import latmod.core.*;
import latmod.core.util.*;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import com.enkigaming.enkimods.cmd.*;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.*;

@Mod(modid = EnkiMods.MODID, name = "EnkiMods", version = "1.0", acceptableRemoteVersions = "*", dependencies = "required-after:LatCoreMC")
public class EnkiMods
{
	protected static final String MODID = "EnkiMods";
	
	@Mod.Instance(EnkiMods.MODID)
	public static EnkiMods inst;
	
	public static LMMod mod;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		EnkiData.init();
		
		mod = new LMMod(e, new EnkiModsConfig(), null);
		
		LatCoreMC.addEventHandler(EnkiModsEventHandler.instance, true, false, true);
		LatCoreMC.addEventHandler(EnkiModsTickHandler.instance, false, true, false);
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e)
	{
		EnkiModsConfig.Login.loadStartingInv();
		//EnkiWorldEdit.postInit();
	}
	
	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent e)
	{
		e.registerServerCommand(new CmdNear());
		e.registerServerCommand(new CmdMail());
		e.registerServerCommand(new CmdVanish());
		e.registerServerCommand(new CmdMotd());
		e.registerServerCommand(new CmdRules());
		e.registerServerCommand(new CmdSpawn());
		e.registerServerCommand(new CmdSethome());
		e.registerServerCommand(new CmdHome());
		e.registerServerCommand(new CmdDelhome());
		e.registerServerCommand(new CmdRestartTimer());
		e.registerServerCommand(new CmdAdmin());
		e.registerServerCommand(new CmdSetRank());
		e.registerServerCommand(new CmdGetRank());
		e.registerServerCommand(new CmdClaim());
		e.registerServerCommand(new CmdBack());
		e.registerServerCommand(new CmdHead());
		e.registerServerCommand(new CmdTop());
		e.registerServerCommand(new CmdTplast());
		
		if(EnkiModsConfig.General.overrideCommands)
		{
			e.registerServerCommand(new CmdTpOverride());
			e.registerServerCommand(new CmdListOverride());
			e.registerServerCommand(new CmdGamemodeOverride());
			e.registerServerCommand(new CmdGameruleOverride());
		}
	}
	
	@Mod.EventHandler
	public void serverStarted(FMLServerStartedEvent e)
	{ EnkiModsTickHandler.instance.resetTimer(true); }
	
	@Mod.EventHandler
	public void serverStopped(FMLServerStoppedEvent e)
	{ EnkiModsTickHandler.instance.resetTimer(false); }
	
	public static boolean isSpawnChunk(World w, int cx, int cz)
	{
		if(w.provider.dimensionId != 0 || EnkiModsConfig.WorldCategory.spawnDistance <= 0F) return false;
		double x = cx * 16D + 8.5D;
		double z = cz * 16D + 8.5D;
		double dist = EnkiModsConfig.WorldCategory.spawnDistance;
		ChunkCoordinates c = w.getSpawnPoint();
		double x1 = ((int)(c.posX / 16D)) * 16D + 8.5D;
		double z1 = ((int)(c.posZ / 16D)) * 16D + 8.5D;
		return MathHelperLM.distSq(x, 0D, z, x1, 0D, z1) <= dist * dist;
	}
	
	public static boolean isSpawnChunkD(World w, double x, double z)
	{ return isSpawnChunk(w, MathHelperLM.chunk(x), MathHelperLM.chunk(z)); }
	
	public static boolean isOutsideWorldBorder(World w, double x, double z)
	{
		if(!EnkiModsConfig.WorldCategory.enableWorldBorder) return false;
		
		int dist = EnkiModsConfig.WorldCategory.getWorldBorder(w.provider.dimensionId);
		
		if(dist <= 0) return false;
		
		if(EnkiModsConfig.WorldCategory.worldBorderAt0x0)
			return (x < - dist || x > dist) || (z < - dist || z > + dist);
		
		Vertex c = LatCoreMC.getSpawnPoint(w);
		return (x < c.x - dist || x > c.x + dist) || (z < c.z - dist || z > c.z + dist);
	}
}