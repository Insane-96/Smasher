package net.insane96mcp.smasher.lib;

import net.insane96mcp.smasher.Smasher;
import net.insane96mcp.smasher.utils.SmasherRecipesHelper;
import net.minecraft.stats.StatCrafting;
import net.minecraftforge.oredict.OreDictionary;

public class Properties {
	
	public static void Init() {
		General.Init();
		Upgrade.Init();
	}
	
	public static void PostInit() {
		General.PostInit();
	}
	
	public static class General{
		public static String[] smasher_recipes;
		public static int deafultCookTime;
		
		private static String[] smasher_recipes_default = new String[] {
			"oreIron,smasher:pulverized_iron#2,0.525,800",
			"oreGold,smasher:pulverized_gold#2,0.75,1200",
	        "minecraft:stone:0,minecraft:cobblestone,0.075,550",
	        "minecraft:cobblestone:0,minecraft:gravel,0.1,400",
	        "minecraft:gravel:0,minecraft:sand,0.1,250",
	        "minecraft:stone:2,minecraft:stone:1,0.0,550",
	        "minecraft:stone:4,minecraft:stone:3,0.0,550",
	        "minecraft:stone:6,minecraft:stone:5,0.0,550",
			"sandstone,minecraft:sand#4,0.0,250",
			"oreQuartz,minecraft:quartz#4,0.15,550",
			"blockQuartz,minecraft:quartz#4,0.0,250",
			"bone,minecraft:dye:15#4,0.075,150",
			"minecraft:blaze_rod,minecraft:blaze_powder#3,0.075,200",
			"minecraft:wool,minecraft:string#3,0.05,75"
		};
		
		public static void Init() {
			smasher_recipes = Config.LoadStringListProperty("general", "smasher_recipes", "List of recipes for the smasher\nThe format is modid:itemid_input:metadata,modid:itemid_output:metadata#count,experience_amount,smashTime_inTicks\nE.g. 'minecraft:sandstone:1,minecraft:sand#4,0.1,200' will make smooth sandstone grindable, will take 10 seconds (200 ticks) and output 4 sand with 0.1 experience per sand.\nOre dictionary can be used in input (e.g. oreDiamond,minecraft:diamond#2,1.0).\nIf metadata's omitted in input, every block/item metadata will be count. Instead in the output it will result in metadata=0\nEach line should contain a recipe\n", smasher_recipes_default);
			deafultCookTime = Config.LoadIntProperty("general", "default_cook_time", "Time it takes the smasher to smash a block / item if not specified by the block / item, in ticks (20 ticks = 1 second)", 900);
			
		}
		
		public static void PostInit() {
			for (int i = 0; i < smasher_recipes.length; i++) {
				String[] split = smasher_recipes[i].split(",");
				
				if (split.length < 3 || split.length > 4) {
					Smasher.logger.error("Failed to parse line {}", smasher_recipes[i]);
					continue;
				}
				
				//Input
				String inputId = split[0];
				String[] inputIdSplit = inputId.split(":");

				boolean isOreDict = false;
				String oreDict = "";
				String inputFullId = "";
				int inputMetadata = Short.MAX_VALUE;

				if (inputIdSplit.length == 1) {
					if (OreDictionary.doesOreNameExist(inputIdSplit[0])) {
						isOreDict = true;
						oreDict = inputIdSplit[0];
					}
					else {
						Smasher.logger.error("Failed to parse oreDict input {} from line {}", inputIdSplit[0], smasher_recipes[i]);
						continue;
					}
				}
				if ((inputIdSplit.length < 2 || inputIdSplit.length > 3) && !isOreDict) {
					Smasher.logger.error("Failed to parse input id {} from line {}", inputId, smasher_recipes[i]);
					continue;
				}
				
				if (!isOreDict) {
					inputFullId = inputIdSplit[0] + ":" + inputIdSplit[1];
					if (inputIdSplit.length == 3) {
						inputMetadata = Integer.parseInt(inputIdSplit[2]);
					}	
				}
				
				//output
				String output = split[1];
				String[] outputSplit = output.split("#");
				if (outputSplit.length < 1 || outputSplit.length > 2) {
					Smasher.logger.error("Failed to parse output {} from line {}", output, smasher_recipes[i]);
					continue;
				}
				
				int outputCount = 1;
				if (outputSplit.length == 2)
					outputCount = Integer.parseInt(outputSplit[1]);
				
				String outputId = outputSplit[0];
				String[] outputIdSplit = outputId.split(":");
				
				String outputFullId;
				int outputMetadata = 0;
				
				if (outputIdSplit.length < 2 || outputIdSplit.length > 3) {
					Smasher.logger.error("Failed to parse output id {} from line {}", outputId, smasher_recipes[i]);
					continue;
					
				}

				outputFullId = outputIdSplit[0] + ":" + outputIdSplit[1];
				if (outputIdSplit.length == 3) {
					outputMetadata = Integer.parseInt(outputIdSplit[2]);
				}
				
				//Experience
				float experience = Float.parseFloat(split[2]);
				
				//cookTime
				int cookTime = Properties.General.deafultCookTime;
				if (split.length == 4) 
					cookTime = Integer.parseInt(split[3]);
				if (isOreDict) {
					SmasherRecipesHelper.instance().addRecipeOreDict(oreDict, inputMetadata, outputFullId, outputCount, outputMetadata, experience, cookTime);
				}
				else {
					SmasherRecipesHelper.instance().addRecipe(inputFullId, inputMetadata, outputFullId, outputCount, outputMetadata, experience, cookTime);
				}
			}
		}
	}

	public static class Upgrade{
		private static String CATEGORY = "upgrade";
		private static String DESCRIPTION = "Set here every upgrade property";
		
		public static boolean enableUpgrades;
		
		public static void Init() {
			enableUpgrades = Config.LoadBoolProperty(CATEGORY, "enable_upgrades", "Enable / Disable all the upgrades", true);
			
			Speed.Init();
		}
		
		public static class Speed{
			private static String SUBCATEGORY = CATEGORY + ".speed";
			
			public static float speedBonus;
			public static float fuelConsumption;
			public static int maxUpgrades;
			
			public static void Init() {
				speedBonus = Config.LoadFloatProperty(SUBCATEGORY, "speed_bonus", "How much faster (in percentage) will be the smasher with each of this upgrade", 20f);
				fuelConsumption = Config.LoadFloatProperty(SUBCATEGORY, "fuel_consumption", "How much faster (in percentage) will the fuel be consumed with each of this upgrade.", 20f);
				maxUpgrades = Config.LoadIntProperty(SUBCATEGORY, "max_upgrades", "Maximum number of speed upgrades per smasher and max stack size", 5);
			}
		}
	}
}
