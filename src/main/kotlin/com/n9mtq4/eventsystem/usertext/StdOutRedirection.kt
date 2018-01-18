package com.n9mtq4.eventsystem.usertext

import com.n9mtq4.eventsystem.core.EventSystem
import com.n9mtq4.eventsystem.core.event.AdditionEvent
import com.n9mtq4.eventsystem.core.event.DisableEvent
import com.n9mtq4.eventsystem.core.event.EnableEvent
import com.n9mtq4.eventsystem.core.listener.impl.AdditionListener
import com.n9mtq4.eventsystem.core.listener.impl.DisableListener
import com.n9mtq4.eventsystem.core.listener.impl.EnableListener
import java.io.PrintStream

/**
 * Created by will on 1/17/2018 at 8:20 PM.
 * 
 * A class that redirects System.out.print and prints them
 * to a [EventSystem]. It can also show the file and line number
 * that the print came from.
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
class StdOutRedirection private constructor(private val showLocation: Boolean) : 
		PrintStream(System.out), AdditionListener, EnableListener, DisableListener {
	
	companion object {
		
		/**
		 * Adds a new instance of StdOutRedirect to the given [EventSystem].
		 * Enables the show location if [showLocation] is set to true.
		 * 
		 * @param eventSystem the event system to add the redirection to
		 * @param showLocation should the redirection show the line of code the call came from?
		 * */
		@JvmOverloads
		fun addToEventSystem(eventSystem: EventSystem, showLocation: Boolean = false) {
			
			val redirection = StdOutRedirection(showLocation)
			eventSystem.addListenerAttribute(redirection)
			
		}
		
	}
	
	private var eventSystem: EventSystem? = null
	private var backup: PrintStream? = null
	private var on: Boolean = false
	
	/**
	 * Change Listener method that gets called when there is some change
	 * to the relationship with this listener and the [EventSystem]
	 * */
	override fun onAddition(additionEvent: AdditionEvent, eventSystem: EventSystem) {
		this.eventSystem = additionEvent.initiatingEventSystem
	}
	
	/**
	 * Change Listener method that gets called when there is some change
	 * to the relationship with this listener and the [EventSystem]
	 * */
	override fun onEnable(enableEvent: EnableEvent, eventSystem: EventSystem) {
		turnOn()
	}
	
	/**
	 * Change Listener method that gets called when there is some change
	 * to the relationship with this listener and the [EventSystem]
	 * */
	override fun onDisable(disableEvent: DisableEvent, eventSystem: EventSystem) {
		turnOff()
	}
	
	/**
	 * Enables the event system redirection.
	 * */
	fun turnOn() {
		this.on = true
		this.backup = System.out
		System.setOut(this)
	}
	
	/**
	 * Disable the event system redirection.
	 * */
	fun turnOff() {
		this.on = false
		System.setOut(backup)
	}
	
	/**
	 * Gets the location of where the print was called from.
	 * 
	 * @return the line number and file from where it was called 4 stacks ago
	 * */
	private fun getLocation(): String {
		val element = Thread.currentThread().stackTrace[3]
		return "(${element.fileName}:${element.lineNumber}): "
//		return MessageFormat.format("({0}:{1, number,#}): ", element.fileName, element.lineNumber)
	}
	
	override fun print(s: String?) {
		
		if (eventSystem == null) backup?.print(s)
		eventSystem?.let { es ->
			if (es.disposed) return
			val element = Thread.currentThread().stackTrace[2]
			if ("print" !in element.methodName) {
				if (showLocation) es.print(getLocation() + s)
				else es.print(s)
			}else {
				backup?.print(s)
			}
		}
		
	}
	
	override fun print(obj: Any?) {
		if (eventSystem == null) backup?.print(obj)
		eventSystem?.let { es ->
			if (es.disposed) return
			val element = Thread.currentThread().stackTrace[2]
			if ("print" !in element.methodName) {
				if (showLocation) es.print(getLocation() + obj)
				else es.print(obj)
			}else {
				backup?.print(obj)
			}
		}
	}
	
	override fun println(x: String?) {
		if (eventSystem == null) backup?.println(x)
		eventSystem?.let { es ->
			if (es.disposed) return
			val element = Thread.currentThread().stackTrace[2]
			if ("print" !in element.methodName) {
				if (showLocation) es.println(getLocation() + x)
				else es.println(x)
			}else {
				backup?.println(x)
			}
		}
	}
	
	override fun println(x: Any?) {
		if (eventSystem == null) backup?.println(x)
		eventSystem?.let { es ->
			if (es.disposed) return
			val element = Thread.currentThread().stackTrace[2]
			if ("print" !in element.methodName) {
				if (showLocation) es.println(getLocation() + x)
				else es.println(x)
			}else {
				backup?.println(x)
			}
		}
	}
	
}
