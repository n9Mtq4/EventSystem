package com.n9mtq4.eventsystem.usertext.modules

import com.n9mtq4.eventsystem.core.EventSystem
import com.n9mtq4.eventsystem.usertext.events.UserTextEvent
import com.n9mtq4.eventsystem.usertext.listener.UserTextListener
import com.n9mtq4.eventsystem.usertext.print
import com.n9mtq4.eventsystem.usertext.ui.attributes.Textable

/**
 * A [UserTextListener] that clears the screen when the user
 * types `clear`
 * 
 * Created by will on 12/9/18 at 10:35 PM.
 * 
 * @since 6.0
 * @author Will "n9Mtq4" Bresnahan
 */
@Suppress("unused")
class ModuleClear : UserTextListener {
	
	/**
	 * A method that listens for text that the user enters.
	 * If that text is "clear", then it will clear the screen
	 * of previously output text.
	 *
	 * @param event the [UserTextEvent]
	 * @param eventSystem the [EventSystem] that pushed this event
	 * */
	override fun receiveUserText(event: UserTextEvent, eventSystem: EventSystem) {
		
		// command is "clear"
		if (!event.msg.trim().equals("clear", ignoreCase = true)) return
		
		// ANSI sequence for clearing
		eventSystem.print("\u001B[2J\u001B[H")
		
		// if the event ui supports clearing the text, we can do so
		eventSystem
			.cloneListenerContainerList()
			.filterIsInstance<Textable>()
			.forEach { it.setText("") }
		
	}
	
}
