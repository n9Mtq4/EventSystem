package com.n9mtq4.eventsystem.core.ui.impl

import com.n9mtq4.eventsystem.core.ui.EventSystemUI
import com.n9mtq4.eventsystem.usertext.events.PrintEvent

/**
 * Created by will on 1/4/2018 at 10:06 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class UISysOut : EventSystemUI {
	
	override fun print(printEvent: PrintEvent) {
		
		print("${printEvent.obj}${(if (printEvent.newLine) "\n" else "")}")
		
	}
	
}