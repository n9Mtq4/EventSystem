package com.n9mtq4.eventsystem.core.event

import com.n9mtq4.eventsystem.core.EventSystem

/**
 * Created by will on 1/4/2018 at 9:01 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

/**
 * This event is given to the [com.n9mtq4.logwindow.listener.EnableListener]
 * when the listener is enabled on an [EventSystem]
 * 
 * @param initiatingEventSystem the event system that is pushing this event
 * @see com.n9mtq4.logwindow.listener.EnableListener
 * @since 0.2
 * @version 6.0
 * @author Will "n9Mtq4" Bresnahan
 * */
open class EnableEvent(initiatingEventSystem: EventSystem) : DefaultBaseEvent(initiatingEventSystem)

/**
 * This event is given to the [com.n9mtq4.logwindow.listener.AdditionListener]
 * when the listener is added on an [EventSystem]
 * 
 * @param initiatingEventSystem the event system that is pushing this event
 * @see com.n9mtq4.logwindow.listener.AdditionListener
 * @since 0.2
 * @version 6.0
 * @author Will "n9Mtq4" Bresnahan
 * */
open class AdditionEvent(initiatingEventSystem: EventSystem) : DefaultBaseEvent(initiatingEventSystem)

/**
 * This event is given to the [com.n9mtq4.logwindow.listener.DisableListener]
 * when the listener is disabled on an [EventSystem]
 * 
 * @param initiatingEventSystem the event system that is pushing this event
 * @param type the reason for the disable
 * @see com.n9mtq4.logwindow.listener.DisableListener
 * @since 0.2
 * @version 6.0
 * @author Will "n9Mtq4" Bresnahan
 * */
open class DisableEvent(initiatingEventSystem: EventSystem, val type: Int = NOT_SPECIFIED) : DefaultBaseEvent(initiatingEventSystem) {
	
	companion object {
		const val NOT_SPECIFIED = -1
		const val EVENT_SYSTEM_DISPOSE = 0
		const val CODE_CLOSE = 1
		const val USER_CLOSE = 2
		const val OTHER_CLOSE = 10
	}
	
}

/**
 * This event is given to the [com.n9mtq4.logwindow.listener.RemovalListener]
 * when the listener is removed on an [EventSystem]
 * 
 * @param initiatingEventSystem the event system that is pushing this event
 * @param type the reason for the removal
 * @see com.n9mtq4.logwindow.listener.RemovalListener
 * @since 0.2
 * @version 6.0
 * @author Will "n9Mtq4" Bresnahan
 * */
open class RemovalEvent(initiatingEventSystem: EventSystem, val type: Int = NOT_SPECIFIED) : DefaultBaseEvent(initiatingEventSystem) {
	
	companion object {
		const val NOT_SPECIFIED = -1
		const val EVENT_SYSTEM_DISPOSE = 0
		const val CODE_CLOSE = 1
		const val USER_CLOSE = 2
		const val OTHER_CLOSE = 10
	}
	
}
