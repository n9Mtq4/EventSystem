package com.n9mtq4.eventsystem.plugin

import com.n9mtq4.eventsystem.core.EventSystem
import com.n9mtq4.eventsystem.core.listener.ListenerAttribute
import com.n9mtq4.eventsystem.plugin.reflect.addFileToClasspath
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*
import java.util.zip.ZipFile
import kotlin.reflect.full.primaryConstructor

/**
 * Created by will on 1/24/2018 at 3:55 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

/**
 * Loads some plugins from a directory to an event system
 * 
 * @receiver the event system to load plugins to
 * @param directory the directory of plugins
 * */
@JvmOverloads
fun EventSystem.loadPlugins(directory: File = File("plugins/")) = loadPluginsToEventSystem(this, directory)

/**
 * Loads the specified plugin to the event system
 *
 * @receiver the event system to load plugins to
 * @param file the jar file of the pluginss
 * */
fun EventSystem.loadPlugin(file: File) = loadPluginToEventSystem(this, file)

/**
 * Loads all plugins from the specific directory to the event system.
 * 
 * @param eventSystem the event system to load plugins to
 * @param directory the directory to load plugins from
 * */
private fun loadPluginsToEventSystem(eventSystem: EventSystem, directory: File = File("plugins/")) {
	
	if (!directory.exists()) throw FileNotFoundException("The directory '${directory.absolutePath}' does not exist!")
	val plugins = directory.walkTopDown().filter { it.isValidPlugin }.toList()
	loadPluginsToEventSystem(eventSystem, plugins)
	
}

/**
 * Loads a plugin to the event system
 *
 * @param eventSystem the event system to load the plugin to
 * @param pluginFile the plugin file to load
 * */
private fun loadPluginToEventSystem(eventSystem: EventSystem, pluginFile: File) {
	
	if (!pluginFile.isValidPlugin) throw IOException("${pluginFile.absolutePath} is not a valid plugin")
	loadPluginsToEventSystem(eventSystem, listOf(pluginFile))
	
}

/**
 * Loads a list of plugin files to an event system
 * 
 * @param eventSystem the event system to load the plugins to
 * @param plugins the list of plugins to load
 * */
@Suppress("DEPRECATION")
private fun loadPluginsToEventSystem(eventSystem: EventSystem, plugins: List<File>) {
	
	// read through files to make sure they have a valid plugins.txt
	val pluginsParsed = plugins
			.filter { it.isValidPlugin }
			.mapNotNull { ignoreAndNull { it to parsePluginText(getPluginJsonText(it)) } }
	
	// add them to the classpath
	pluginsParsed
			.map { it.first }
			.forEach { addFileToClasspath(it) }
	
	// get a list of classes to add
	val listenerClasses = pluginsParsed
			.map { it.second }
			.flatMap { it.map { s -> Class.forName(s).kotlin } }
	
	// make an instance of the listener attributes
	val listenerAttributes = listenerClasses
			.mapNotNull { it.primaryConstructor?.call() }
			.map { it as ListenerAttribute }
	
	// add them to the event system
	val listenerContainers = listenerAttributes
			.map { eventSystem.addListenerAttribute(it, enable = false) }
	
	// enable the listeners
	listenerContainers.forEach { eventSystem.enableListenerContainer(it) }
	
}

/**
 * parses the text into a list of class names
 * */
private fun parsePluginText(txt: String): List<String> {
	
	return txt
			.split("\n") // into lines
			.map { it.trim() } // remove whitespace
			.filter { it.isNotBlank() } // no blanks
			.filterNot { it.startsWith("#") } // no comments
	
}

/**
 * Reads the plugin.json from a plugin jar or zip file.
 * 
 * @param file the plugin file
 * @return the string contents of the plugin.json
 * */
@Throws(Exception::class)
private fun getPluginJsonText(file: File): String {
	
	val zipFile = ZipFile(file) // file as a zip (works for jars too)
	val inputStream = zipFile.getInputStream(zipFile.getEntry("plugin.txt")) // get an is for the plugin json
	
	// quick way to read an input stream
	val scanner = Scanner(inputStream).useDelimiter("\\A") // scanner that reads everything
	val str = if (scanner.hasNext()) scanner.next() else "" // read it or empty string
	scanner.close() // close scanner
	
	inputStream.close() // close input stream. scanner should do it, but just in case
	return str
	
}

/**
 * Checks to see if the file is a valid plugin
 * 
 * Must not be a system (starting with a dot) or a hidden file
 * Must be a jar or a zip
 * @receiver the file to check
 * @return if this file is a valid plugin or not
 * */
private val File.isValidPlugin: Boolean 
	get() {
		val extension = this.extension.toLowerCase() 
		// can't be a hidden file or a system file (starting with a dot) 
		// must be a jar or a zip 
		return !(this.isDirectory) && !(this.name.startsWith(".") || this.isHidden) && (extension == "jar" || extension == "zip")
	}

/**
 * Method that surround a block with try catch,
 * ignoring the exception, and just returning null
 * if there is an exception
 * */
private inline fun <T> ignoreAndNull(body: () -> T): T? {
	try {
		return body()
	}catch (ignored: Throwable) {}
	return null
}
