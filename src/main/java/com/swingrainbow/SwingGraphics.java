package com.swingrainbow;

import java.util.HashSet;
import java.util.Set;

/** Which graphics-object ids count as a weapon swing trail. */
final class SwingGraphics
{
	/**
	 * Dragon halberd special trails (SpotanimID.DRAGON_HALBERD_SPECIAL_*). Scythe swings reuse these,
	 * which is how Rainbow Rave recolours both: red 478/506/1172/1231, white 1232-1235,
	 * dark red 1891-1894, blue 1895-1898.
	 */
	static final int[] KNOWN = {
		478, 506, 1172,
		1231, 1232, 1233, 1234, 1235,
		1891, 1892, 1893, 1894, 1895, 1896, 1897, 1898,
	};

	/** Parses "1234, 5678" style input; blanks and non-numbers are skipped. */
	static Set<Integer> parseIds(String csv)
	{
		Set<Integer> ids = new HashSet<>();
		if (csv == null)
		{
			return ids;
		}
		for (String part : csv.split(","))
		{
			try
			{
				ids.add(Integer.parseInt(part.trim()));
			}
			catch (NumberFormatException ignored)
			{
				// skip blanks and typos
			}
		}
		return ids;
	}

	static Set<Integer> build(boolean includeKnown, String extraCsv)
	{
		Set<Integer> ids = parseIds(extraCsv);
		if (includeKnown)
		{
			for (int id : KNOWN)
			{
				ids.add(id);
			}
		}
		return ids;
	}

	private SwingGraphics()
	{
	}
}
