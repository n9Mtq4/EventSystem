package com.n9mtq4.eventsystem.usertext.events

import com.n9mtq4.eventsystem.core.EventSystem
import com.n9mtq4.eventsystem.core.event.BaseEvent
import com.n9mtq4.eventsystem.usertext.utils.Colour

/**
 * Created by will on 1/4/2018 at 9:45 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class PrintEvent(override val initiatingEventSystem: EventSystem, val obj: Any, val newLine: Boolean = false, val colour: Colour? = null) : BaseEvent {
	
	override var isCanceled: Boolean
		get() = false
		set(value) {} // can't set this event to be canceled
	
}
