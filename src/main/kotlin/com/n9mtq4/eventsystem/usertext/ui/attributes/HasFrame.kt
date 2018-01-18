package com.n9mtq4.eventsystem.usertext.ui.attributes

import javax.swing.JFrame

/**
 * An interface for event system ui's to indicate that
 * they have a jframe.
 * 
 * This interface should only be on a [com.n9mtq4.eventsystem.core.ui.EventSystemUI]
 * 
 * This is for comparability across many different libraries
 * with different event system uis. This way, there are
 * at least some standards that listeners can use to identify
 * frames of the corresponding uis.
 * 
 * Created by will on 1/6/2018 at 1:27 AM.
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
interface HasFrame {
	
	/**
	 * What is the instance of the JFrame of this event system ui?
	 * 
	 * @return the jframe of the event system ui
	 * */
	fun getJFrame(): JFrame
	
}
