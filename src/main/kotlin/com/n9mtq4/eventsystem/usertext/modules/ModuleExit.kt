package com.n9mtq4.eventsystem.usertext.modules

import com.n9mtq4.eventsystem.core.EventSystem
import com.n9mtq4.eventsystem.usertext.events.UserTextEvent
import com.n9mtq4.eventsystem.usertext.listener.UserTextListener

/**
 * A [UserTextEvent] that disposes the [EventSystem] if the user
 * types 'exit'
 * 
 * Created by will on 1/6/2018 at 12:00 AM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class ModuleExit : UserTextListener {
	
	/**
	 * The function that listens for the text.
	 * Disposes the event system if the text equals 'exit'
	 * 
	 * @param event the [UserTextEvent]
	 * @param eventSystem the event system that pushed this event
	 * */
	override fun receiveUserText(event: UserTextEvent, eventSystem: EventSystem) {
		
		if (event.msg.trim().equals("exit", ignoreCase = true)) eventSystem.dispose()
		
	}
	
}
