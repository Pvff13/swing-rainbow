package com.swingrainbow;

import com.google.inject.Provides;
import java.util.Collections;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.GraphicsObject;
import net.runelite.api.Model;
import net.runelite.api.Player;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.GraphicsObjectCreated;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
	name = "Swing Rainbow",
	description = "Cycles halberd and scythe swing trails through the rainbow.",
	tags = {"scythe", "halberd", "swing", "rainbow", "color"}
)
public class SwingRainbowPlugin extends Plugin
{
	/** Graphics within this many local units (128 per tile) of you are reported by "Show graphic IDs". */
	private static final int LOG_RADIUS = 3 * 128;

	@Inject
	private Client client;

	@Inject
	private SwingRainbowConfig config;

	@Inject
	private ChatMessageManager chatMessageManager;

	private volatile Set<Integer> swingIds = Collections.emptySet();

	@Provides
	SwingRainbowConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(SwingRainbowConfig.class);
	}

	@Override
	protected void startUp()
	{
		rebuildIds();
	}

	@Override
	protected void shutDown()
	{
		swingIds = Collections.emptySet();
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (SwingRainbowConfig.GROUP.equals(event.getGroup()))
		{
			rebuildIds();
		}
	}

	private void rebuildIds()
	{
		swingIds = SwingGraphics.build(config.recolorKnownSwings(), config.extraGraphicIds());
	}

	@Subscribe
	public void onClientTick(ClientTick event)
	{
		Set<Integer> ids = swingIds;
		if (ids.isEmpty())
		{
			return;
		}

		int cycle = client.getGameCycle();
		boolean rainbow = config.rainbow();
		int speed = config.colorSpeed();
		int custom = SwingColors.toRs2Hsb(config.customColor());
		for (GraphicsObject graphic : client.getTopLevelWorldView().getGraphicsObjects())
		{
			if (!ids.contains(graphic.getId()))
			{
				continue;
			}

			int packed = rainbow
				? SwingColors.toRs2Hsb(SwingColors.rainbow(graphic.getLocation().hashCode(), cycle, speed))
				: custom;
			recolorAllFaces(graphic.getModel(), packed);
		}
	}

	@Subscribe
	public void onGraphicsObjectCreated(GraphicsObjectCreated event)
	{
		if (!config.logGraphicIds() || client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		Player local = client.getLocalPlayer();
		LocalPoint where = event.getGraphicsObject().getLocation();
		if (local == null || local.getLocalLocation().distanceTo(where) > LOG_RADIUS)
		{
			return;
		}

		chatMessageManager.queue(QueuedMessage.builder()
			.type(ChatMessageType.CONSOLE)
			.runeLiteFormattedMessage("Swing Rainbow: graphic id " + event.getGraphicsObject().getId())
			.build());
	}

	private static void recolorAllFaces(Model model, int rs2hsb)
	{
		if (model == null)
		{
			return;
		}

		fill(model.getFaceColors1(), rs2hsb);
		fill(model.getFaceColors2(), rs2hsb);
		fill(model.getFaceColors3(), rs2hsb);
	}

	private static void fill(int[] faceColors, int value)
	{
		for (int i = 0; i < faceColors.length; i++)
		{
			faceColors[i] = value;
		}
	}
}
