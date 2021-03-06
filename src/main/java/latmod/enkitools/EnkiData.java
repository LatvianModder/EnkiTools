package latmod.enkitools;

import ftb.lib.FTBLib;
import latmod.lib.LMFileUtils;

import java.io.File;

public class EnkiData
{
	public static File folder;
	public static File ranks;
	public static File players;
	
	public static void init()
	{
		folder = new File(FTBLib.folderLocal, "enkitools/");
		ranks = LMFileUtils.newFile(new File(folder, "ranks.json"));
		players = LMFileUtils.newFile(new File(folder, "players.json"));
	}
}