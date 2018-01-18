package com.n9mtq4.eventsystem.core.listener.impl

import com.n9mtq4.eventsystem.core.EventSystem
import com.n9mtq4.eventsystem.core.annotation.ListensFor
import com.n9mtq4.eventsystem.core.event.AdditionEvent
import com.n9mtq4.eventsystem.core.event.DisableEvent
import com.n9mtq4.eventsystem.core.event.EnableEvent
import com.n9mtq4.eventsystem.core.event.RemovalEvent
import com.n9mtq4.eventsystem.core.listener.BaseListener

/**
 * Interface for classes that wish to get notified when the listener is enabled.
 * 
 * @version 6.0
 * @author Will "n9Mtq4" Bresnahan
 */
interface EnableListener : BaseListener {
	
	/**
	 * This method is called when the listener has been enabled to
	 * the [EventSystem].
	 * 
	 * @since 6.0
	 * @param enableEvent the enable action event
	 * @param eventSystem the event system that pushed this event
	 * */
	@ListensFor
	fun onEnable(enableEvent: EnableEvent, eventSystem: EventSystem)
	
}

/**
 * Interface for classes that wish to get notified when the listener is added.
 *
 * @version 6.0
 * @author Will "n9Mtq4" Bresnahan
 */
interface AdditionListener : BaseListener {
	
	/**
	 * This method is called when the listener has been added to
	 * the [EventSystem].
	 *
	 * @since 6.0
	 * @param additionEvent the addition action event
	 * @param eventSystem the event system that pushed this event
	 * */
	@ListensFor
	fun onAddition(additionEvent: AdditionEvent, eventSystem: EventSystem)
	
}

/**
 * Interface for classes that wish to get notified when the listener is added.
 * 
 * @version 6.0
 * @author Will "n9Mtq4" Bresnahan
 * */
interface DisableListener : BaseListener {
	
	/**
	 * This method is called when the listener has been disabled to
	 * the [EventSystem]
	 * 
	 * @param disableEvent the disable action event
	 * @param eventSystem the event system that pushed this event
	 * */
	@ListensFor
	fun onDisable(disableEvent: DisableEvent, eventSystem: EventSystem)
	
}

/**
 * Interface for classes that wish to get notified when the listener is added.
 * 
 * @version 6.0
 * @author Will "n9Mtq4" Bresnahan
 * */
interface RemovalListener : BaseListener {
	
	/**
	 * This method is called when the listener has been removed to
	 * the [EventSystem]
	 * 
	 * @param removalEvent the disable action event
	 * @param eventSystem the event system that pushed this event
	 * */
	@ListensFor
	fun onRemoval(removalEvent: RemovalEvent, eventSystem: EventSystem)
	
}
