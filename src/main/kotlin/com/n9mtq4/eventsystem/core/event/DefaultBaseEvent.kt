package com.n9mtq4.eventsystem.core.event

import com.n9mtq4.eventsystem.core.EventSystem

/**
 * Created by will on 1/3/2018 at 10:17 PM.
 * 
 * A [BaseEvent] with initiation event system and canceling implemented
 * 
 * @param initiatingEventSystem The event system that relayed this event
 * @param isCanceled has this event been canceled?
 * @since 5.1
 * @version 6.0
 * @author Will "n9Mtq4" Bresnahan
 */
open class DefaultBaseEvent(override val initiatingEventSystem: EventSystem, override var isCanceled: Boolean = false) : BaseEvent
