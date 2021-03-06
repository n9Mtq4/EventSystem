package com.n9mtq4.eventsystem.usertext.ui.impl

import com.n9mtq4.eventsystem.usertext.events.PrintEvent
import com.n9mtq4.eventsystem.usertext.events.UserTextEvent
import com.n9mtq4.eventsystem.usertext.ui.SimpleEventSystemUI
import com.n9mtq4.eventsystem.usertext.utils.Colour
import java.io.Console
import java.util.*
import kotlin.concurrent.thread

/**
 * The string that is printed before every input.
 * It won't print anything if this string is blank
 * */
private const val PROMPT_STRING = ""

/**
 * A [com.n9mtq4.eventsystem.core.ui.EventSystemUI] that has a uses a terminal/console.
 * It has the text being outputed and uses scanner or System.console to get input.
 * 
 * It supports a subset of colors.
 * 
 * Created by will on 1/5/2018 at 5:19 PM.
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
open class UIScanner(val forceAnsiColour: Boolean = false) : SimpleEventSystemUI() {
	
	protected open var scanner: Scanner? = null
	protected open var console: Console? = null
	
	protected open var shouldScan: Boolean = true
	protected open var ansi: Boolean = true
	
	/**
	 * Initializes this ui
	 * */
	override fun init() {
		
		initInput()
		ansi = !(System.getProperty("os.name").contains("window", ignoreCase = true))
		
	}
	
	/**
	 * Initializes the ui
	 * */
	protected open fun initInput() {
		if (System.console() == null) {
			initScanner()
		} else {
			initConsole()
		}
	}
	
	/**
	 * Initializes using the System.console() implementation
	 * */
	protected open fun initConsole() {
		
		this.console = System.console()
		this.shouldScan = true
		thread(start = true, name = "Console Input Listener") {
			
			while (shouldScan) {
				
				if (PROMPT_STRING.isNotEmpty()) print(PROMPT_STRING)
				val s = console!!.readLine()
				cloneEventSystemList().forEach { it.pushEvent(UserTextEvent(it, s)) }
				
			}
			
		}
		
	}
	
	/**
	 * Initializes using the Scanner implementation
	 * */
	protected open fun initScanner() {
		
		this.scanner = Scanner(System.`in`)
		this.shouldScan = true
		thread(start = true, name = "Scanner Input Listener") {
			
			while (shouldScan) {
				
				if (PROMPT_STRING.isNotEmpty()) print(PROMPT_STRING)
				val s = scanner!!.nextLine()
				cloneEventSystemList().forEach { it.pushEvent(UserTextEvent(it, s)) }
				
			}
			
		}
		
	}
	
	/**
	 * Dispose this ui
	 * */
	override open fun dispose() {
		stopScan()
		if (scanner != null) scanner?.close()
	}
	
	/**
	 * Stops scanning for new input
	 * might require an enter to take effect.
	 * */
	protected open fun stopScan() {
		shouldScan = false
	}
	
	/**
	 * Handles printing
	 * */
	override open fun print(printEvent: PrintEvent) {
		
		val obj = printEvent.obj
		val newLine = printEvent.newLine
		val colour = printEvent.colour
		
		val ending = if (newLine) "\n" else ""
		if ((ansi || forceAnsiColour) && colour != null) print("${colour.getANSI()}$obj$ending${Colour.getAnsiReset()}")
		else print("$obj$ending")
		
	}
	
}
