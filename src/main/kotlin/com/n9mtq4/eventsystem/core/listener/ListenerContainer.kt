package com.n9mtq4.eventsystem.core.listener

import com.n9mtq4.eventsystem.core.EventSystem
import com.n9mtq4.eventsystem.core.annotation.Async
import com.n9mtq4.eventsystem.core.annotation.AsyncType
import com.n9mtq4.eventsystem.core.annotation.ListensFor
import com.n9mtq4.eventsystem.core.annotation.listensForInherit
import com.n9mtq4.eventsystem.core.event.BaseEvent
import com.n9mtq4.eventsystem.core.utils.MultiHashMap
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.reflect.Modifier
import kotlin.concurrent.thread
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.allSupertypes
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaMethod

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
open class ListenerContainer protected constructor(val listener: ListenerAttribute) {
	
	companion object {
		
		/**
		 * Turns a listener attribute into a listener container
		 * 
		 * @param listener the listener attribute
		 * @return the listener container
		 * */
		fun makeListenerEntry(listener: ListenerAttribute): ListenerContainer = ListenerContainer(listener)
		
	}
	
	/**
	 * The list of linked event systems that this container
	 * has been added to.
	 * Use [cloneEventSystemList] when iterating over it, so it doesn't 
	 * through a concurrent modification exception
	 * */
	protected open val linkedEventSystems = ArrayList<EventSystem>()
	
	/**
	 * If this [ListenerContainer] is enabled and willing
	 * to receive events
	 * */
	open var enabled = false
	
	/**
	 * If this [ListenerContainer] should still receive events, even if
	 * they have been disabled.
	 * */
	var ignoreCanceled = false
	
	protected open val listenerMethodLookup = MultiHashMap<KClass<*>, KFunction<*>>()
	protected open val listenerUnsupportedEvents = ArrayList<KClass<*>>()
	protected open val listenerMethodAsyncType = HashMap<KFunction<*>, AsyncType>()
	
	protected open var init = false
	
	/**
	 * Initializes this [ListenerContainer].
	 * Generates method caches for the future
	 * */
	protected open fun init() {
		
		if (init) return
		init = true
		initializeGenericListenerCache()
		
	}
	
	/**
	 * DO NOT USE THIS METHOD
	 * 
	 * Add this listener container to the [EventSystem]
	 * Requires the corresponding method from the [EventSystem] called
	 * in the correct order.
	 * 
	 * @param eventSystem the event system that is being added
	 * */
	internal open fun addToEventSystem(eventSystem: EventSystem) {
		
		init()
		if (eventSystem !in linkedEventSystems) linkedEventSystems.add(eventSystem)
		
	}
	
	/**
	 * DO NOT USE THIS METHOD
	 * 
	 * Removes this listener container from the [EventSystem]
	 * Requires the corresponding method from the [EventSystem] called
	 * in the correct order.
	 * 
	 * @param eventSystem the event system that is being removed
	 * */
	internal open fun removeFromEventSystem(eventSystem: EventSystem) {
		
		if (eventSystem in linkedEventSystems) linkedEventSystems.remove(eventSystem)
		
	}
	
	/**
	 * Pushes a generic event to the generic listeners.
	 * Finds the function corresponding to the event and
	 * will call it with the right async context.
	 * 
	 * @param event the generic event to send to the listener
	 * */
	open fun pushBaseEvent(event: BaseEvent) {
		
		// has to be a generic listener to get these events
		if (listener !is BaseListener) return
		
		findTargets(event)
				.map { it to getAsyncTypeForFunction(it) }
				.forEach { (target, type) ->
					when (type) {
						AsyncType.NONE -> callBaseEventReceiver(event, target)
						// TODO: should this be a GlobalScope?
						AsyncType.COROUTINE -> { GlobalScope.launch { callBaseEventReceiver(event, target) } }
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
	protected open fun callBaseEventReceiver(event: BaseEvent, function: KFunction<*>) {
		
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
	
	/**
	 * Searches the listener attribute for possible methods that
	 * have the [ListensFor] annotation. Adds them into the 
	 * [listenerMethodLookup] hash map.
	 * */
	protected open fun initializeGenericListenerCache() {
		
		if (listener !is BaseListener) return
		
		val classesToSearch = parentClasses(listener::class)
		// TODO: possible bug. distinct by toString isn't good. Ideally will go up subclass hierarchy
		val functions = classesToSearch
				.flatMap { it.members }
				.filterIsInstance<KFunction<*>>()
				.filterNot { Modifier.isStatic(it.javaMethod?.modifiers ?: return@filterNot true) }
				.distinctBy { it.toSignatureString() }
		
		val functionsAndListensFor = functions
				.map { it to it.allAnnotations().filterIsInstance<ListensFor>() }
				.filter { it.second.isNotEmpty() }
				.map { it.first to it.second.first() }
				.map { it.first to it.second.clazz }
		
		functionsAndListensFor.forEach { (function, clazz) ->
			
			val newClazz = if (clazz == listensForInherit) function.parameters[1].type.classifier as KClass<*>
			else clazz
			
			listenerMethodLookup.multiPutSingle(newClazz, function)
			
		}
		
	}
	
	/**
	 * Gets a list of the class along with any parent classes and parent annotations.
	 * 
	 * @return A list of KClass of the class and all supers
	 * */
	protected open fun parentClasses(kClass: KClass<*>): List<KClass<*>> = listOf(kClass) + 
			(kClass.allSupertypes
					.map { it.classifier }
					.map { it as KClass<*> })
	
	/**
	 * Gets all the annotations on a method, including those from
	 * inherited annotations.
	 * 
	 * @receiver the KFunction to get the annotations for
	 * @return a list of annotations
	 * */
	protected open fun <R> KFunction<R>.allAnnotations(): List<Annotation> {
		
		val allClasses = parentClasses(listener::class)
		val allFunctions = allClasses
				.flatMap { it.members }
				.filterIsInstance<KFunction<*>>()
		
		val ourSig = this.toSignatureString()
		val matchingFunctions = allFunctions.filter { it.toSignatureString() == ourSig }
		
		return matchingFunctions.flatMap { it.annotations }
		
	}
	
	/**
	 * Gets a string that should be unique to every function within a class
	 * 
	 * @receiver the KFunction to get the signature sting
	 * @return the signature string
	 * */
	protected open fun <R> KFunction<R>.toSignatureString(): String {
		
		// if there are no params, just go with name and return type
		if (parameters.isEmpty()) return "$name NO_PARAMS $returnType"
		
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
	protected open fun findTargets(event: BaseEvent): List<KFunction<*>> {
		
		/*
		* simple look up
		* try to find method in cached results. if it is
		* found, return it, otherwise continue the search
		* */
		listenerMethodLookup[event::class]?.let { return it }
		
		/*
		* Have we tried and failed to find this listener event
		* before? If so, remember that we have failed before and
		* don't spend time trying to search for it again
		* */
		if (event::class in listenerUnsupportedEvents) return emptyList()
		
		/*
		* Try a deeper search.
		* */
		val pair = event::class to listenerMethodLookup
				.filter { it.key.isInstance(event) }
				.flatMap { it.value }
		
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
		listenerMethodLookup.multiPutList(pair.first, pair.second)
		return pair.second
		
	}
	
	/**
	 * Gets the type of async the listener should have
	 * 
	 * @param function the listener's function
	 * @return the [AsyncType] of the function
	 * */
	protected open fun getAsyncTypeForFunction(function: KFunction<*>): AsyncType {
		
		// check the cache first
		listenerMethodAsyncType[function]?.let { return it }
		
		// if we didn't find it, figure out what async type it should be
		
		// get the async type
		val asyncType = function
				.allAnnotations() // search annotations
				.firstOrNull { it is Async } // find the async one
				?.let { it as Async }?.type ?: AsyncType.NONE // get the type. If its null, then type = none
		
		// we got the type, lets cache it
		listenerMethodAsyncType[function] = asyncType
		
		return asyncType
		
	}
	
	/**
	 * Makes a duplicate list of the [linkedEventSystems]
	 * so that you can modify the linkedEventSystem while
	 * iterating. 
	 * 
	 * @return a cloned [linkedEventSystems] list
	 * */
	fun cloneEventSystemList() = linkedEventSystems.toList()
	
}
