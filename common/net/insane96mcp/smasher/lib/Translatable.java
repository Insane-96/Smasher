package net.insane96mcp.smasher.lib;

public class Translatable {	
	
	public static class General{
		public static String shiftForMore = "translatable.general.shift_for_more";
	}
	
	public static class Smasher{
		private static String name = "translatable.smasher.";
	}
	
	public static class Upgrade{
		private static String name = "translatable.upgrade.";

		public static String speedUpgradeIncrease = name + "speed_upgrade_tooltip_increase";
		public static String speedUpgradeFuel = name + "speed_upgrade_tooltip_fuel";
		
		public static String maxUpgradeCount = name + "max_upgrade_count";
		public static String upgradesDisabled = name + "upgrades_disabled";
	}
}
