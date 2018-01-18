package com.n9mtq4.eventsystem.core.utils

import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter

/**
 * Created by will on 1/5/2018 at 2:45 AM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

/**
 * Turns an [Exception]'s stack trace into a string.
 * Takes e.printStackTrace() and returns the string.
 * 
 * @receiver the exception
 * @return the string stack trace
 * */
fun Exception.toStackTraceString(): String {
	
	val sw = StringWriter()
	val pw = PrintWriter(sw)
	this.printStackTrace(pw)
	val str = sw.toString()
	pw.close()
	try {
		sw.close()
	} catch (e: IOException) {
		e.printStackTrace()
	}
	
	return str
	
}
