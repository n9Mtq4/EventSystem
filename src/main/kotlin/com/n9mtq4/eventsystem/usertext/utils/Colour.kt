package com.n9mtq4.eventsystem.usertext.utils

import java.awt.Color
import java.awt.color.ColorSpace

/**
 * A color class that has ansi color codes that correspond
 * with the colors defined as constants in this class.
 * 
 * Created by will on 1/5/2018 at 6:27 PM.
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
class Colour : Color {
	
	companion object {
		
		/**
		 * The Colour white.
		 */
		val WHITE = getColour(Color.WHITE)
		/**
		 * The Colour light gray.
		 */
		val LIGHT_GRAY = getColour(Color.LIGHT_GRAY)
		/**
		 * The Colour gray.
		 */
		val GRAY = getColour(Color.GRAY)
		/**
		 * The Colour red.
		 */
		val RED = getColour(Color.RED)
		/**
		 * The Colour pink.
		 */
		val PINK = getColour(Color.PINK)
		/**
		 * The Colour orange.
		 */
		val ORANGE = getColour(Color.ORANGE)
		/**
		 * The Colour yellow.
		 */
		val YELLOW = getColour(Color.YELLOW)
		/**
		 * The Colour green.
		 */
		val GREEN = getColour(Color.GREEN)
		/**
		 * The Colour magenta.
		 */
		val MAGENTA = getColour(Color.MAGENTA)
		/**
		 * The Colour blue.
		 */
		val BLUE = getColour(Color.BLUE)
		/**
		 * The Colour cyan.
		 */
		val CYAN = getColour(Color.CYAN)
		/**
		 * The Colour black.
		 */
		val BLACK = getColour(Color.BLACK)
		/**
		 * The Colour purple.
		 */
		val PURPLE = Colour(128, 0, 128)
		
		// ANSI TO COLOUR SEPARATOR
		/**
		 * The ANSI code for resetting the color.
		 */
		val ANSI_RESET = "\u001B[0m"
		/**
		 * The ANSI code for the color black.
		 */
		val ANSI_BLACK = "\u001B[30m"
		/**
		 * The ANSI code for the color red.
		 */
		val ANSI_RED = "\u001B[31m"
		/**
		 * The ANSI code for the color gree.
		 */
		val ANSI_GREEN = "\u001B[32m"
		/**
		 * The ANSI code for the color yellow.
		 */
		val ANSI_YELLOW = "\u001B[33m"
		/**
		 * The ANSI code for the color blue.
		 */
		val ANSI_BLUE = "\u001B[34m"
		/**
		 * The ANSI code for the color purple.
		 */
		val ANSI_PURPLE = "\u001B[35m"
		/**
		 * The ANSI code for the color cyan.
		 */
		val ANSI_CYAN = "\u001B[36m"
		/**
		 * The ANSI code for the color white.
		 */
		val ANSI_WHITE = "\u001B[37m"
		
		/**
		 * Converts [Color] into [Colour].
		 *
		 * @since v3.1
		 * @param color Java's [Color].
		 * @return The Colour version with ANSI support.
		 */
		fun getColour(color: Color): Colour {
			return Colour(color.red, color.green, color.blue, color.alpha)
		}
		
		/**
		 * Gives the color reset ANSI code.<br></br>
		 *
		 * @since v3.1
		 * @return The ANSI code for resetting the colors.
		 */
		fun getAnsiReset(): String {
			return ANSI_RESET
		}
		
		fun getANSI(colour: Colour): String = when(colour.rgb) {
			
			RED.rgb -> ANSI_RED
			YELLOW.rgb -> ANSI_YELLOW
			GREEN.rgb -> ANSI_GREEN
			BLUE.rgb -> ANSI_BLUE
			CYAN.rgb -> ANSI_CYAN
			BLACK.rgb -> ANSI_BLACK
			WHITE.rgb -> ANSI_WHITE
			PURPLE.rgb -> ANSI_PURPLE
			MAGENTA.rgb -> ANSI_PURPLE
			else -> "" // no ansi color, return nothing
			
		}
		
	}
	
	constructor(r: Int, g: Int, b: Int) : super(r, g, b)
	constructor(r: Int, g: Int, b: Int, a: Int) : super(r, g, b, a)
	constructor(rgb: Int) : super(rgb)
	constructor(rgba: Int, hasalpha: Boolean) : super(rgba, hasalpha)
	constructor(r: Float, g: Float, b: Float) : super(r, g, b)
	constructor(r: Float, g: Float, b: Float, a: Float) : super(r, g, b, a)
	constructor(cspace: ColorSpace?, components: FloatArray?, alpha: Float) : super(cspace, components, alpha)
	
	/**
	 * Turns this [Colour] into an ANSI code.<br></br>
	 * Note: only works for some constants. (won't work with custom RGB).<br></br>
	 *
	 * @since v3.1
	 * @return the ANSI string of the color for terminal colors.
	 */
	fun getANSI(): String {
		return getANSI(this)
	}
	
}
