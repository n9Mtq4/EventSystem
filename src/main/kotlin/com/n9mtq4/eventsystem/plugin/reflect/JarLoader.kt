package com.n9mtq4.eventsystem.plugin.reflect

import java.io.File
import java.io.IOException
import java.net.URL
import java.net.URLClassLoader

/**
 * Created by will on 3/24/2018 at 9:57 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

/**
 * The parameters required for finding the 
 * correct method to reflect on
 * */
private val PARAMETERS = arrayOf<Class<*>>(URL::class.java)

/**
 * Adds a file to this classpath
 * 
 * @param file the file to add
 * */
@Throws(IOException::class)
internal fun addFileToClasspath(file: File) {
	addUrlToClasspath(file.toURI().toURL())
}

/**
 * Adds a url to this classpath
 * 
 * @param url the url to add
 * */
@Throws(IOException::class)
internal fun addUrlToClasspath(url: URL) {
	
	val sysloader: URLClassLoader = ClassLoader.getSystemClassLoader() as URLClassLoader
	val sysclass = URLClassLoader::class.java
	try {
		val method = sysclass.getDeclaredMethod("addURL", *PARAMETERS)
		method.isAccessible = true
		method.invoke(sysloader, url)
	}catch (t: Throwable) {
		throw IOException("Error, could not add URL to system classloader '$url'", t)
	}
	
}
