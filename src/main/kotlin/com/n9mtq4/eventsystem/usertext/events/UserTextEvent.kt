package com.n9mtq4.eventsystem.usertext.events

import com.n9mtq4.eventsystem.core.EventSystem
import com.n9mtq4.eventsystem.core.event.DefaultBaseEvent

/**
 * Created by will on 1/5/2018 at 5:30 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class UserTextEvent(initiatingEventSystem: EventSystem, val msg: String) : DefaultBaseEvent(initiatingEventSystem, false)
