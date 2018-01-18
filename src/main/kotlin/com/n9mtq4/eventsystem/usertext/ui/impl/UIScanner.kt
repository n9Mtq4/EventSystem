package com.n9mtq4.eventsystem.usertext.ui.impl

import com.n9mtq4.eventsystem.core.ui.SimpleEventSystemUI
import com.n9mtq4.eventsystem.usertext.events.PrintEvent
import com.n9mtq4.eventsystem.usertext.events.UserTextEvent
import com.n9mtq4.eventsystem.usertext.utils.Colour
import java.io.Console
import java.util.*
import kotlin.concurrent.thread

/**
 * Created by will on 1/5/2018 at 5:19 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
open class UIScanner(val forceAnsiColour: Boolean = false) : SimpleEventSystemUI() {
	
	private var scanner: Scanner? = null
	private var console: Console? = null
	
	private var shouldScan: Boolean = true
	private var ansi: Boolean = true
	
	override fun init() {
		
		initInput()
		ansi = !(System.getProperty("os.name").contains("window", ignoreCase = true))
		
	}
	
	private fun initInput() {
		if (System.console() == null) {
			initScanner()
		} else {
			initConsole()
		}
	}
	
	private fun initConsole() {
		
		this.console = System.console()
		this.shouldScan = true
		thread(start = true, name = "Console Input Listener") {
			
			while (shouldScan) {
				
				print("> ")
				val s = console!!.readLine()
				cloneEventSystemList().forEach { it.pushEvent(UserTextEvent(it, s)) }
				
			}
			
		}
		
	}
	
	private fun initScanner() {
		
		this.scanner = Scanner(System.`in`)
		this.shouldScan = true
		thread(start = true, name = "Scanner Input Listener") {
			
			while (shouldScan) {
				
				print("> ")
				val s = scanner!!.nextLine()
				cloneEventSystemList().forEach { it.pushEvent(UserTextEvent(it, s)) }
				
			}
			
		}
		
	}
	
	override fun dispose() {
		stopScan()
		if (scanner != null) scanner?.close()
	}
	
	private fun stopScan() {
		shouldScan = false
	}
	
	override fun print(printEvent: PrintEvent) {
		
		val obj = printEvent.obj
		val newLine = printEvent.newLine
		val colour = printEvent.colour
		
		val ending = if (newLine) "\n" else ""
		if ((ansi || forceAnsiColour) && colour != null) print("${colour.getANSI()}$obj$ending${Colour.getAnsiReset()}")
		else print("$obj$ending")
		
		if (newLine) print("> ")
		
	}
	
}
