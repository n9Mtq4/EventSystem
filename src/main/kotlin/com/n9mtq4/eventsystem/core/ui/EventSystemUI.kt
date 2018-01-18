package com.n9mtq4.eventsystem.core.ui

import com.n9mtq4.eventsystem.core.EventSystem
import com.n9mtq4.eventsystem.core.annotation.ListensFor
import com.n9mtq4.eventsystem.core.listener.BaseListener
import com.n9mtq4.eventsystem.usertext.events.PrintEvent

/**
 * An interface for implementing a listener that
 * acts as a user interface for the event system
 * 
 * Created by will on 1/4/2018 at 9:53 PM.
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
interface EventSystemUI : BaseListener {
	
	/**
	 * DON'T USE THIS METHOD
	 * 
	 * A method that receives the print event and sends it to the print
	 * method.
	 * */
	@Suppress("unused")
	@ListensFor
	fun receivePrintRequest(printEvent: PrintEvent, eventSystem: EventSystem) {
		print(printEvent)
	}
	
	/**
	 * Called when something wants to print
	 * something through the event system
	 * 
	 * @since 5.0
	 * @param printEvent the printing event
	 * */
	fun print(printEvent: PrintEvent)
	
}
