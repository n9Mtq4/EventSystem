package com.n9mtq4.eventsystem.core.listener.impl

import com.n9mtq4.eventsystem.core.annotation.ListensFor
import com.n9mtq4.eventsystem.core.event.AdditionEvent
import com.n9mtq4.eventsystem.core.event.DisableEvent
import com.n9mtq4.eventsystem.core.event.EnableEvent
import com.n9mtq4.eventsystem.core.event.RemovalEvent
import com.n9mtq4.eventsystem.core.EventSystem
import com.n9mtq4.eventsystem.core.listener.BaseListener

/**
 * Created by will on 1/4/2018 at 9:03 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
interface EnableListener : BaseListener {
	
	@ListensFor
	fun onEnable(enableEvent: EnableEvent, eventSystem: EventSystem)
	
}

interface AdditionListener : BaseListener {
	
	@ListensFor
	fun onAddition(additionEvent: AdditionEvent, eventSystem: EventSystem)
	
}


interface DisableListener : BaseListener {
	
	@ListensFor
	fun onDisable(disableEvent: DisableEvent, eventSystem: EventSystem)
	
}


interface RemovalListener : BaseListener {
	
	@ListensFor
	fun onRemoval(removalEvent: RemovalEvent, eventSystem: EventSystem)
	
}
