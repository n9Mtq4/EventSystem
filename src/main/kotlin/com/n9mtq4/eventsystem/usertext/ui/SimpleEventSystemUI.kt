package com.n9mtq4.eventsystem.usertext.ui

import com.n9mtq4.eventsystem.core.EventSystem
import com.n9mtq4.eventsystem.core.event.AdditionEvent
import com.n9mtq4.eventsystem.core.event.RemovalEvent
import com.n9mtq4.eventsystem.core.listener.impl.AdditionListener
import com.n9mtq4.eventsystem.core.listener.impl.RemovalListener
import java.util.*

/**
 * An abstract class that implements some of the default
 * functionality that an [EventSystemUI] should have. 
 * 
 * Created by will on 1/5/2018 at 10:58 PM.
 * 
 * @since 6.0
 * @author Will "n9Mtq4" Bresnahan
 */
abstract class SimpleEventSystemUI : EventSystemUI, AdditionListener, RemovalListener {
	
	/**
	 * The list of linked event systems that this event system ui
	 * is added to.
	 * */
	protected open val linkedEventSystems = ArrayList<EventSystem>()
	
	/**
	 * Has this event system ui been initialized yet?
	 * */
	private var hasBeenInit: Boolean = false
	
	/**
	 * A function that is called only once when this
	 * [EventSystemUI] is added onto an [EventSystem]
	 * */
	abstract fun init()
	
	/**
	 * A function that is called when the [SimpleEventSystemUI]
	 * is being disposed.
	 * */
	abstract fun dispose()
	
	/**
	 * DON'T USE THIS METHOD
	 * 
	 * Methods that receive the raw [EventSystem] events and
	 * calls the init function.
	 * 
	 * @param additionEvent the addition event
	 * @param eventSystem the event system that is pushing this event
	 * */
	override fun onAddition(additionEvent: AdditionEvent, eventSystem: EventSystem) {
		
		if (eventSystem !in linkedEventSystems) linkedEventSystems.add(eventSystem)
		if (!hasBeenInit) init()
		
	}
	
	/**
	 * DON'T USE THIS METHOD
	 * 
	 * Methods that receive the raw [EventSystem] events and
	 * calls the dispose function.
	 * 
	 * @param removalEvent the addition event
	 * @param eventSystem the event system that is pushing this event
	 * */
	override fun onRemoval(removalEvent: RemovalEvent, eventSystem: EventSystem) {
		
		if (eventSystem in linkedEventSystems) linkedEventSystems.remove(eventSystem)
		if (linkedEventSystems.isEmpty()) dispose()
		
	}
	
	/**
	 * Makes a duplicate list of the [linkedEventSystems]
	 * so that you can modify the linkedEventSystem while
	 * iterating.
	 *
	 * @return a cloned [linkedEventSystems] list
	 * */
	protected fun cloneEventSystemList() = linkedEventSystems.toList()
	
}
