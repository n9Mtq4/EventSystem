package com.n9mtq4.eventsystem.usertext.modules

import com.n9mtq4.eventsystem.core.EventSystem
import com.n9mtq4.eventsystem.usertext.events.UserTextEvent
import com.n9mtq4.eventsystem.usertext.listener.UserTextListener
import com.n9mtq4.eventsystem.usertext.print
import com.n9mtq4.eventsystem.usertext.println
import com.n9mtq4.eventsystem.usertext.utils.Colour
import kotlin.reflect.jvm.jvmName

/**
 * A [UserTextListener] that lists all the added
 * listeners when the user types 'listener list'
 * 
 * Created by will on 3/31/18 at 6:23 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
@Suppress("unused")
class ModuleListenerList : UserTextListener {
	
	/**
	 * A method that listens for text that the user enters.
	 * If that text is "listener list", then it will print all
	 * the listeners currently on the [EventSystem].
	 * 
	 * @param event the [UserTextEvent]
	 * @param eventSystem the [EventSystem] that pushed this event
	 * */
	override fun receiveUserText(event: UserTextEvent, eventSystem: EventSystem) {
		
		// command must be "listener list"
		if (!event.msg.trim().equals("listener list", ignoreCase = true)) return
		
		// go through all listeners, and print out their class name.
		// print in green if they're enabled, red if disabled
		eventSystem
			.listenerContainerList
			.asSequence()
			.map { it.enabled to it.listener::class.jvmName }
			.forEachIndexed { index, (enabled, name) -> 
				eventSystem.print("[$index]: ")
				eventSystem.println(name, if (enabled) Colour.GREEN else Colour.RED) 
			}
		
	}
	
}
