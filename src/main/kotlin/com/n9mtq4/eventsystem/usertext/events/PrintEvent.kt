package com.n9mtq4.eventsystem.usertext.events

import com.n9mtq4.eventsystem.core.EventSystem
import com.n9mtq4.eventsystem.core.event.BaseEvent
import com.n9mtq4.eventsystem.usertext.utils.Colour

/**
 * An event that is sent when something is to be printed to a listener
 * that receives these events or a [com.n9mtq4.eventsystem.core.ui.EventSystemUI].
 * 
 * @param initiatingEventSystem the event system that initiated this event
 * @param obj the object to print
 * @param newLine should we print a new line after it?
 * @param colour the color of the text
 * @since 6.0
 * @author Will "n9Mtq4" Bresnahan
 */
open class PrintEvent(override val initiatingEventSystem: EventSystem, val obj: Any?, val newLine: Boolean = false, val colour: Colour? = null) : BaseEvent {
	
	override var isCanceled: Boolean
		get() = false
		set(_) {} // can't set this event to be canceled
	
}
