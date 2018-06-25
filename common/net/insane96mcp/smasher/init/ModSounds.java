package net.insane96mcp.smasher.init;

import java.util.ArrayList;

import net.insane96mcp.smasher.Smasher;
import net.insane96mcp.smasher.lib.Names;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class ModSounds {

	public static SoundEvent smashSound = createSound(Names.SMASH_SOUND);
	
	public static ArrayList<SoundEvent> SOUNDS = new ArrayList<SoundEvent>();
	
	public static void Init() {
		SOUNDS.add(smashSound);
	}
	
	private static SoundEvent createSound(String soundName) {
		final ResourceLocation soundID = new ResourceLocation(Smasher.MOD_ID, soundName);
		return new SoundEvent(soundID).setRegistryName(soundID);
	}
}
