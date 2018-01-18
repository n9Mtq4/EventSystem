package com.n9mtq4.eventsystem.usertext.modules

import com.n9mtq4.eventsystem.core.EventSystem
import com.n9mtq4.eventsystem.usertext.events.UserTextEvent
import com.n9mtq4.eventsystem.usertext.listener.UserTextListener

/**
 * Created by will on 1/6/2018 at 12:00 AM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class ModuleExit : UserTextListener {
	
	override fun receiveUserText(event: UserTextEvent, eventSystem: EventSystem) {
		
		if (event.msg.trim().equals("exit", ignoreCase = true)) eventSystem.dispose()
		
		println("hi")
		
	}
	
}
