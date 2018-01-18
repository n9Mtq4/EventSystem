package com.n9mtq4.eventsystem.usertext.events

import com.n9mtq4.eventsystem.core.EventSystem
import com.n9mtq4.eventsystem.core.event.DefaultBaseEvent

/**
 * An event that is sent when a user enters some text as input.
 * Requires support from the individual [com.n9mtq4.eventsystem.core.ui.EventSystemUI]s
 * 
 * Created by will on 1/5/2018 at 5:30 PM.
 * 
 * @see com.n9mtq4.eventsystem.usertext.listener.UserTextListener
 * @since 6.0
 * @author Will "n9Mtq4" Bresnahan
 */
class UserTextEvent(initiatingEventSystem: EventSystem, val msg: String) : DefaultBaseEvent(initiatingEventSystem, false)
