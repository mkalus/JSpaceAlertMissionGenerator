package de.beimax.spacealert.util;

// Class to handle console text coloring
public class ConsoleColorer {

	/**
	 * Adds ANSI color codes to a line of text based on its content,
	 * specifically looking for keywords like "threat", "serious", and zone names.
	 *
	 * @param line the line of text to color
	 * @return the colored line of text
	 */
	public static String colorLine(String line) {
		Options options = Options.getOptions();
		if (!options.colorConsoleText) {
			return line; // No coloring, return as is
		}
		StringBuilder sb = new StringBuilder();
		String lowerLine = line.toLowerCase();
		sb.append("\033[0"); // Start the ANSI escape sequence
		if (lowerLine.contains("threat")) {
			if (lowerLine.contains("serious")) {
				sb.append(";4"); // Underline serious threats
			}
			if (lowerLine.contains("red")) {
				sb.append(";91");
			} else if (lowerLine.contains("white")) {
				sb.append(";97");
			} else if (lowerLine.contains("blue")) {
				sb.append(";94");
			} else if (lowerLine.contains("internal")) {
				sb.append(";92");
			}
		} else {
			// Non-threat text. Make it gray.
			sb.append(";90");
		}
		sb.append("m"); // End the ANSI escape sequence
		sb.append(line); // Append the line content
		sb.append("\033[0m"); // Reset color at the end of the line
		return sb.toString();
	}

	/**
	 * Colors each line in the provided string based on its content.
	 * This method splits the input string into lines,
	 * colors each line using the colorLine method, and then
	 * joins them back together.
	 *
	 * @param lines the string containing multiple lines of text
	 * @return the colored lines of text
	 */
	public static String colorLines(String lines) {
		Options options = Options.getOptions();
		if (!options.colorConsoleText) {
			return lines; // No coloring, return as is
		}
		StringBuilder sb = new StringBuilder();
		String[] linesArray = lines.split("\n");
		for (String line : linesArray) {
			sb.append(ConsoleColorer.colorLine(line)).append("\n");
		}
		return sb.toString();
	}
}
