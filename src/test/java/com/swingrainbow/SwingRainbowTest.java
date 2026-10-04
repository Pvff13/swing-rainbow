package com.swingrainbow;

import java.awt.Color;
import java.util.Set;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class SwingRainbowTest
{
	@Test
	public void parseIdsSkipsBlanksAndJunk()
	{
		Set<Integer> ids = SwingGraphics.parseIds(" 1700, ,abc,1701,");

		assertEquals(2, ids.size());
		assertTrue(ids.contains(1700));
		assertTrue(ids.contains(1701));
		assertEquals(0, SwingGraphics.parseIds(null).size());
	}

	@Test
	public void buildCombinesKnownAndExtraAndCanDropKnown()
	{
		Set<Integer> both = SwingGraphics.build(true, "9999");
		assertTrue(both.contains(9999));
		assertTrue(both.contains(1231));
		assertTrue(both.contains(1898));
		assertEquals(SwingGraphics.KNOWN.length + 1, both.size());

		Set<Integer> extraOnly = SwingGraphics.build(false, "9999");
		assertEquals(1, extraOnly.size());
		assertFalse(extraOnly.contains(1231));
	}

	@Test
	public void rainbowColorsDifferByHash()
	{
		assertNotEquals(
			SwingColors.rainbow(0, 100, 3000),
			SwingColors.rainbow(37, 100, 3000));
	}

	@Test
	public void negativeHashStillWrapsIntoTheWheel()
	{
		Color c = SwingColors.rainbow(-123456789, 7, 3000);
		assertEquals(255, Math.max(c.getRed(), Math.max(c.getGreen(), c.getBlue())));
	}

	@Test
	public void colorCompletesALapAfterOnePeriod()
	{
		int period = SwingColors.periodCycles(3000);
		assertEquals(150, period);
		assertEquals(
			SwingColors.rainbow(0, 10, 3000),
			SwingColors.rainbow(0, 10 + period, 3000));
	}

	@Test
	public void packedHsbFitsSixteenBitsAndTracksHue()
	{
		int red = SwingColors.toRs2Hsb(Color.RED);
		int blue = SwingColors.toRs2Hsb(Color.BLUE);

		assertTrue(red >= 0 && red < 1 << 16);
		assertTrue(blue >= 0 && blue < 1 << 16);
		assertNotEquals(red, blue);
	}
}
