/*
 * The face-colour encoding in toRs2Hsb is adapted from Rainbow Rave by geheur
 * (https://github.com/geheur/rainbow-rave), BSD 2-Clause; see LICENSE.
 */
package com.swingrainbow;

import java.awt.Color;

/** Colour maths for cycling swing trails: a hue wheel position over time, and the game's HSB packing. */
final class SwingColors
{
	/** Client cycles (20ms each) in one lap of the colour wheel for a given speed. */
	static int periodCycles(int colorSpeedMs)
	{
		return Math.max(1, colorSpeedMs / 20);
	}

	/**
	 * @param hash per-trail value so neighbouring trails differ
	 * @return a fully saturated colour for this point in the cycle
	 */
	static Color rainbow(int hash, int gameCycle, int colorSpeedMs)
	{
		int period = periodCycles(colorSpeedMs);
		float hue = Math.floorMod(hash + gameCycle, period) / (float) period;
		return Color.getHSBColor(hue, 1.0f, 1.0f);
	}

	/** Packs a colour into the 16-bit hue/saturation/brightness form model face colours use. */
	static int toRs2Hsb(Color color)
	{
		float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);

		// Avoid going white at full saturation, or dark at low saturation.
		hsb[2] -= Math.min(hsb[1], hsb[2] / 2);

		int hue = (int) (hsb[0] * 63);
		int saturation = (int) (hsb[1] * 7);
		int brightness = (int) (hsb[2] * 127);
		return (hue << 10) + (saturation << 7) + brightness;
	}

	private SwingColors()
	{
	}
}
