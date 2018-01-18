package com.n9mtq4.eventsystem.core.event

import com.n9mtq4.eventsystem.core.EventSystem

/**
 * The interface for a generic event.
 * 
 * @since 5.1
 * @version 6.0
 * @author Will "n9Mtq4" Bresnahan
 */
interface BaseEvent {
	
	/**
	 * Gets the [EventSystem] that called this event.
	 * 
	 * @since 5.1
	 * @version 6.0
	 * */
	val initiatingEventSystem: EventSystem
	
	/**
	 * Has a listener indicated that this event has been completed / shouldn't
	 * continue iterating through listeners.
	 * */
	var isCanceled: Boolean
	
}
