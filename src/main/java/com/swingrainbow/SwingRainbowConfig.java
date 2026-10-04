package com.swingrainbow;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup(SwingRainbowConfig.GROUP)
public interface SwingRainbowConfig extends Config
{
	String GROUP = "swingrainbow";

	@ConfigItem(
		position = 0,
		keyName = "recolorKnownSwings",
		name = "Halberd & scythe swings",
		description = "Recolor the dragon halberd and scythe swing trails."
	)
	default boolean recolorKnownSwings()
	{
		return true;
	}

	@ConfigItem(
		position = 1,
		keyName = "extraGraphicIds",
		name = "Extra graphic IDs",
		description = "More swing graphics to recolor, comma separated (for example the crystal halberd). Use \"Show graphic IDs\" to find them."
	)
	default String extraGraphicIds()
	{
		return "";
	}

	@ConfigItem(
		position = 2,
		keyName = "rainbow",
		name = "Rainbow",
		description = "Cycle through the rainbow. Turn off to use the custom color instead."
	)
	default boolean rainbow()
	{
		return true;
	}

	@Range(min = 500, max = 20000)
	@ConfigItem(
		position = 3,
		keyName = "colorSpeed",
		name = "Color speed (ms)",
		description = "Milliseconds for the colors to make one full lap of the rainbow. Only used when Rainbow is on."
	)
	default int colorSpeed()
	{
		return 3000;
	}

	@ConfigItem(
		position = 4,
		keyName = "customColor",
		name = "Custom color",
		description = "The color used for every swing when Rainbow is off. The game only stores a limited palette, so the shade is approximate."
	)
	default Color customColor()
	{
		return Color.MAGENTA;
	}

	@ConfigItem(
		position = 5,
		keyName = "logGraphicIds",
		name = "Show graphic IDs",
		description = "Print the id of every graphic that appears next to you in chat. Turn on, swing your weapon, read the id, put it in Extra graphic IDs, then turn this off."
	)
	default boolean logGraphicIds()
	{
		return false;
	}
}
