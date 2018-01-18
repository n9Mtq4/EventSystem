package com.n9mtq4.eventsystem.usertext.listener

import com.n9mtq4.eventsystem.core.EventSystem
import com.n9mtq4.eventsystem.core.annotation.ListensFor
import com.n9mtq4.eventsystem.core.listener.BaseListener
import com.n9mtq4.eventsystem.usertext.events.UserTextEvent

/**
 * Created by will on 1/6/2018 at 12:02 AM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
interface UserTextListener : BaseListener {
	
	@ListensFor
	fun receiveUserText(event: UserTextEvent, eventSystem: EventSystem)
	
}
