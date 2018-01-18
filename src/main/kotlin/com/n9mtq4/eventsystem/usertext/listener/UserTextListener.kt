package com.n9mtq4.eventsystem.usertext.listener

import com.n9mtq4.eventsystem.core.EventSystem
import com.n9mtq4.eventsystem.core.annotation.ListensFor
import com.n9mtq4.eventsystem.core.listener.BaseListener
import com.n9mtq4.eventsystem.usertext.events.UserTextEvent

/**
 * An interface for classes that want to listen
 * for text that the user enters.
 * 
 * Created by will on 1/6/2018 at 12:02 AM.
 * 
 * @since 6.0
 * @see UserTextEvent
 * @author Will "n9Mtq4" Bresnahan
 */
interface UserTextListener : BaseListener {
	
	/**
	 * A function that listens for entered user text.
	 * 
	 * @param event the [UserTextEvent] that is sent
	 * @param eventSystem the event system that pushed the event
	 * */
	@ListensFor
	fun receiveUserText(event: UserTextEvent, eventSystem: EventSystem)
	
}
