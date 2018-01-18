package com.n9mtq4.eventsystem.core.ui

import com.n9mtq4.eventsystem.core.EventSystem
import com.n9mtq4.eventsystem.core.event.AdditionEvent
import com.n9mtq4.eventsystem.core.event.RemovalEvent
import com.n9mtq4.eventsystem.core.listener.impl.AdditionListener
import com.n9mtq4.eventsystem.core.listener.impl.RemovalListener
import java.util.*

/**
 * Created by will on 1/5/2018 at 10:58 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
abstract class SimpleEventSystemUI : EventSystemUI, AdditionListener, RemovalListener {
	
	protected val linkedEventSystems = ArrayList<EventSystem>()
	protected var hasBeenInit: Boolean = false
	
	abstract fun init()
	abstract fun dispose()
	
	override fun onAddition(additionEvent: AdditionEvent, eventSystem: EventSystem) {
		
		if (eventSystem !in linkedEventSystems) linkedEventSystems.add(eventSystem)
		if (!hasBeenInit) init()
		
	}
	
	override fun onRemoval(removalEvent: RemovalEvent, eventSystem: EventSystem) {
		
		if (eventSystem in linkedEventSystems) linkedEventSystems.remove(eventSystem)
		if (linkedEventSystems.isEmpty()) dispose()
		
	}
	
	protected fun cloneEventSystemList() = linkedEventSystems.toList()
	
}
