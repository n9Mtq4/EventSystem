package com.n9mtq4.eventsystem.usertext.modules

import com.n9mtq4.eventsystem.core.EventSystem
import com.n9mtq4.eventsystem.core.annotation.Async
import com.n9mtq4.eventsystem.usertext.events.UserTextEvent
import com.n9mtq4.eventsystem.usertext.listener.UserTextListener
import com.n9mtq4.eventsystem.usertext.print
import com.n9mtq4.eventsystem.usertext.println
import com.n9mtq4.eventsystem.usertext.utils.Colour

/**
 * A [UserTextListener] that lists all the added
 * listeners when the user types 'listener list'
 * 
 * Created by will on 3/31/18 at 6:23 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class ModuleListenerList : UserTextListener {
	
	@Async
	override fun receiveUserText(event: UserTextEvent, eventSystem: EventSystem) {
		
		if (event.msg.trim().equals("listener list", ignoreCase = true)) {
			
			val listenerContainerList = eventSystem.cloneListenerContainerList()
			listenerContainerList
					.map { it.enabled to it.listener::class.qualifiedName }
					.forEachIndexed { index, (enabled, name) -> 
						eventSystem.print("[$index]: ")
						eventSystem.println(name, if (enabled) Colour.GREEN else Colour.RED) 
					}
			
		}
		
	}
	
}
