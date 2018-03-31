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

/**
 * Prints the stack trace of an exception as a string to the
 * [EventSystem].
 * 
 * @receiver the [EventSystem]
 * @param e the exception to print
 * */
fun EventSystem.printException(e: Exception) = this.println(e.toStackTraceString())

/**
 * Prints a new line to the [EventSystem].
 * 
 * @receiver the [EventSystem]
 * */
fun EventSystem.println() = this.print("", newLine = true)

/**
 * Prints an object (uses `Any.toString()`) to the [EventSystem].
 * 
 * Adds a newline after it.
 * 
 * @receiver the [EventSystem]
 * @param obj the object to print
 * @param colour the color (if supported) to print it in
 * */
@JvmOverloads
fun EventSystem.println(obj: Any?, colour: Colour? = null) {
	this.print(obj, newLine = true, colour = colour)
}

/**
 * Prints an object (uses `Any.toString()`) to the [EventSystem].
 *
 * @receiver the [EventSystem]
 * @param obj the object to print
 * @param newLine should it append a new line to the end?
 * @param colour the color (if supported) to print it in
 * */
@JvmOverloads
fun EventSystem.print(obj: Any?, newLine: Boolean = false, colour: Colour? = null) {
	pushEventNow(PrintEvent(this, obj, newLine, colour))
}
