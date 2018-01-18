package com.n9mtq4.eventsystem.usertext

import com.n9mtq4.eventsystem.core.EventSystem
import com.n9mtq4.eventsystem.core.utils.toStackTraceString
import com.n9mtq4.eventsystem.usertext.events.PrintEvent
import com.n9mtq4.eventsystem.usertext.utils.Colour

/**
 * Created by will on 1/6/2018 at 12:07 AM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

fun EventSystem.printException(e: Exception) = this.println(e.toStackTraceString())

fun EventSystem.println() = this.print("", newLine = true)

fun EventSystem.println(obj: Any, colour: Colour? = null) {
	this.print(obj, newLine = true, colour = colour)
}

fun EventSystem.print(obj: Any, newLine: Boolean = false, colour: Colour? = null) {
	pushEventNow(PrintEvent(this, obj, newLine, colour))
}
