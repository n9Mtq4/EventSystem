package com.n9mtq4.eventsystem.usertext.ui.attributes

/**
 * A interface that flags a [com.n9mtq4.eventsystem.core.ui.EventSystemUI] as having
 * full control over the text that has been pushed to them and that the 
 * [com.n9mtq4.eventsystem.core.ui.EventSystemUI]
 * can set the text that has already been sent to it.
 *
 * This interface should only be on a [com.n9mtq4.eventsystem.core.ui.EventSystemUI]
 * 
 * <p>Created by will on 1/7/15.</p>
 *
 * @since 4.0
 * @version 6.0
 * @author Will "n9Mtq4" Bresnahan
 */
interface Textable {
	
	/**
	 * Gets the current text in a compatible {@link ConsoleUI}.
	 *
	 * @see #setText
	 * @since v4.0
	 * @return The text that the {@link ConsoleUI} has.
	 */
	fun getText(): String
	
	/**
	 * Sets the current text in a compatible {@link ConsoleUI}.
	 *
	 * @see #getText
	 * @since v4.0
	 * @param text The text to set the {@link ConsoleUI}'s view to.
	 */
	fun setText(text: String)
	
}
