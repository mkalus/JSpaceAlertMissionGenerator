/**
 * 
 */
package de.beimax.spacealert.render;

import de.beimax.spacealert.mission.Mission;

/**
 * Renderers print or save missions into stuff
 * @author mkalus
 */
public interface Renderer {
	/**
	 * print mission to standard output
	 * @param mission to be printed
	 * @return true if print is supported
	 */
	public boolean print(Mission mission);

	/**
	 * save mission to file (using filename prefix used in options)
	 * @param mission to be saved to file
	 * @return true if output is supported
	 */
	public boolean output(Mission mission);
}
