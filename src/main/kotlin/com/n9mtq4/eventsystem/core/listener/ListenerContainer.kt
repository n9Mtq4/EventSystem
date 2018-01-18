package com.n9mtq4.eventsystem.core.listener

import com.n9mtq4.eventsystem.core.EventSystem
import com.n9mtq4.eventsystem.core.annotation.Async
import com.n9mtq4.eventsystem.core.annotation.AsyncType
import com.n9mtq4.eventsystem.core.annotation.ListensFor
import com.n9mtq4.eventsystem.core.annotation.listensForInherit
import com.n9mtq4.eventsystem.core.event.BaseEvent
import com.n9mtq4.eventsystem.core.utils.MultiHashMap
import kotlinx.coroutines.experimental.launch
import kotlin.concurrent.thread
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.allSupertypes
import kotlin.reflect.jvm.isAccessible

private const val SET_ACCESSIBLE = false

/**
 * Created by will on 1/3/2018 at 10:26 PM.
 * 
 * A class that wraps a [ListenerAttribute] for use
 * on an [com.n9mtq4.logwindow.EventSystem]
 * 
 * @since 5.1
 * @version 6.0
 * @author Will "n9Mtq4" Bresnahan
 */
open class ListenerContainer private constructor(val listener: ListenerAttribute) {
	
	companion object {
		
		fun makeListenerEntry(listener: ListenerAttribute): ListenerContainer = ListenerContainer(listener)
		
		// TODO: implement stopDisable and stopRemoval
		
	}
	
	protected val linkedEventSystems = ArrayList<EventSystem>()
	
	var enabled = false
	var ignoreCanceled = false
	
	private val listenerMethodLookup = MultiHashMap<KClass<*>, KFunction<*>>()
	private val listenerUnsupportedEvents = ArrayList<KClass<*>>()
	private val listenerMethodAsyncType = HashMap<KFunction<*>, AsyncType>()
	
	private var init = false
	
	fun init() {
		
		if (init) return
		init = true
		initializeGenericListenerCache()
		
	}
	
	fun addToEventSystem(eventSystem: EventSystem) {
		
		init()
		if (eventSystem !in linkedEventSystems) linkedEventSystems.add(eventSystem)
		
	}
	
	fun removeFromEventSystem(eventSystem: EventSystem) {
		
		if (eventSystem in linkedEventSystems) linkedEventSystems.remove(eventSystem)
		
	}
	
	/**
	 * Pushes a generic event to the generic listeners.
	 * Finds the function corresponding to the event and
	 * will call it with the right async context.
	 * 
	 * @param event the generic event to send to the listener
	 * */
	fun pushBaseEvent(event: BaseEvent) {
		
		// has to be a generic listener to get these events
		if (listener !is BaseListener) return
		
		findTargets(event)
				.map { it to getAsyncTypeForFunction(it) }
				.forEach { (target, type) ->
					when (type) {
						AsyncType.NONE -> callBaseEventReceiver(event, target)
						AsyncType.COROUTINE -> { launch { callBaseEventReceiver(event, target) } }
						AsyncType.THREAD -> thread(start = true) { callBaseEventReceiver(event, target) }
					}
				}
		
	}
	
	/**
	 * Pushes a generic event to specified function.
	 * 
	 * @param event the generic event to send
	 * @param function the generic function listener that will get the event
	 * */
	private fun callBaseEventReceiver(event: BaseEvent, function: KFunction<*>) {
		
		// there could be multiple linked event systems
		cloneEventSystemList().forEach { eventSystem ->
			try {
				
				// call the function, once for each linked system
				if (SET_ACCESSIBLE) function.isAccessible = true
				function.call(listener, event, eventSystem)
				
			}catch (e: Exception) {
				
				// some error!
				println("There was an error running the generic listener $function")
				println("There might have been an issue calling it in which case:")
				println("Make sure it is public, and has two arguments, event and event system")
				println("This also might be an exception from inside the listener method that caused it")
				
				e.printStackTrace()
				println("${e.message}")
				
			}
		}
		
	}
	
	private fun initializeGenericListenerCache() {
		
		if (listener !is BaseListener) return
		
		val classesToSearch = parentClasses(listener::class)
		// TODO: possible bug. distinct by toString isn't good. Ideally will go up subclass hierarchy
		val functions = classesToSearch.flatMap { it.members }.filter { it is KFunction<*> }.map { it as KFunction<*> }.distinctBy { it.toSignatureString() }
		
		val functionsAndListensFor = functions
				.map { it to it.allAnnotations()
						.filter { it is ListensFor }
						.map { it as ListensFor } }
				.filter { it.second.isNotEmpty() }
				.map { it.first to it.second.first() }
				.map { it.first to it.second.clazz }
		
		functionsAndListensFor.forEach { (function, clazz) ->
			
			if (clazz == listensForInherit) {
				val newClazz = function.parameters[1].type.classifier as KClass<*>
				listenerMethodLookup.mput(newClazz, function)
			}else {
				listenerMethodLookup.mput(clazz, function)
			}
			
		}
		
	}
	
	private fun parentClasses(kClass: KClass<*>): List<KClass<*>> = listOf(kClass) + kClass.allSupertypes.map { it.classifier }.map { it as KClass<*> }
	
	private fun <R> KFunction<R>.allAnnotations(): List<Annotation> {
		
		val allClasses = parentClasses(listener::class)
		val allFunctions = allClasses
				.flatMap { it.members }
				.filter { it is KFunction<*> }
				.map { it as KFunction<*> }
		
		val ourSig = this.toSignatureString()
		val matchingFunctions = allFunctions.filter { it.toSignatureString() == ourSig }
		
		return matchingFunctions.flatMap { it.annotations }
		
	}
	
	private fun <R> KFunction<R>.toSignatureString(): String {
		
		// remove the first param (this) if needed
		val removeThisParam = if (parameters[0].kind == KParameter.Kind.INSTANCE) parameters.drop(1) else parameters
		val sigParams = removeThisParam.map { it.run { "$name $type $isOptional" } }
		return "$name $sigParams $returnType"
	}
	
	/**
	 * Finds the function that corresponds to the generic event
	 * 
	 * Tries to be fast using a hashmap as a cache
	 * 
	 * @param event the event
	 * @return the function that corresponds to the event
	 * */
	private fun findTargets(event: BaseEvent): List<KFunction<*>> {
		
		/*
		* simple look up
		* try to find method in cached results. if it is
		* found, return it, otherwise continue the search
		* */
		listenerMethodLookup[event::class]?.let { return it }
		
		/*
		* Have we tried and failed to find this listener event
		* before?
		* */
		if (event::class in listenerUnsupportedEvents) return emptyList()
		
		/*
		* Try a deeper search.
		* */
		val pair = event::class to listenerMethodLookup.filter { it.key.isInstance(event) }.flatMap { it.value }
		
		/*
		* If an item does not appear in our records, it does not exist
		* Remember that and don't spend the time searching for it again.
		* */
		if (pair.second.isEmpty()) {
			listenerUnsupportedEvents.add(pair.first)
			return emptyList()
		}
		
		/*
		* We found it. Let's remember it for
		* next time, and return it!
		* */
		listenerMethodLookup.aput(pair.first, pair.second)
		return pair.second
		
	}
	
	/**
	 * Gets the type of async the listener should have
	 * 
	 * @param function the listener's function
	 * @return the [AsyncType] of the function
	 * */
	private fun getAsyncTypeForFunction(function: KFunction<*>): AsyncType {
		
		// check the cache first
		listenerMethodAsyncType[function]?.let { return it }
		
		// if we didn't find it, figure out what async type it should be
		
		// get the async type
		val asyncType = function.allAnnotations() // search annotations
				.firstOrNull { it is Async } // find the async one
				?.let { it as Async }?.type ?: AsyncType.NONE // get the type. If its null, then type = none
		
		// we got the type, lets cache it
		listenerMethodAsyncType.put(function, asyncType)
		
		return asyncType
		
	}
	
	fun cloneEventSystemList() = linkedEventSystems.toList()
	
}
