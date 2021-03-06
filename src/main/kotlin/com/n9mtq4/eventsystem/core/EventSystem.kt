package com.n9mtq4.eventsystem.core

import com.n9mtq4.eventsystem.core.event.*
import com.n9mtq4.eventsystem.core.listener.ListenerAttribute
import com.n9mtq4.eventsystem.core.listener.ListenerContainer
import java.util.*

private typealias ListenerContainerList = ArrayList<ListenerContainer>

/**
 * The main class of the LogWindowKt Library.
 * 
 * Created by will on 1/3/2018 at 9:22 PM.
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
open class EventSystem {
	
	/**
	 * The list of listener containers that have been added
	 * to this [EventSystem]
	 * */
	protected open val listenerContainers = ListenerContainerList()
	
	/**
	 * If this event system has been disposed.
	 * */
	open var disposed: Boolean = false
		protected set
	
	/**
	 * how many events are currently being pushed
	 * */
	protected open var pushing: Int = 0
	
	/**
	 * The queue of events waiting to be pushed
	 * */
	protected open val pushQueue = ArrayDeque<BaseEvent>(mutableListOf())
	
	/**
	 * Converts the ArrayList of [ListenerContainer]s to an immutable
	 * list of [ListenerContainer]s. Prevents concurrent modification
	 * exceptions when accessing listenerContainers.
	 * 
	 * Makes a duplicate list of the [listenerContainers]
	 * so that you can modify the listener containers while
	 * iterating.
	 * */
	open val listenerContainerList: List<ListenerContainer>
		get() = listenerContainers.toList()
	
	/**
	 * Disposes the Event System
	 * Disables and removes the all the listener containers
	 * 
	 * @since 6.0
	 * */
	open fun dispose() {
		if (disposed) throw IllegalStateException("EventSystem has been disposed.") // can only run this method once
		// remove every listener
		listenerContainerList.forEach { removeListenerContainer(it, DisableEvent.EVENT_SYSTEM_DISPOSE) }
		disposed = true // mark this method as already being run
	}
	
	/**
	 * Adds a listener attribute to this Event System.
	 * 
	 * @since 6.0
	 * @param listenerAttribute the listener attribute to add
	 * @param enable if the enable event should be sent (Default = true)
	 * @return the listener container that was added
	 * */
	@JvmOverloads
	open fun addListenerAttribute(listenerAttribute: ListenerAttribute, enable: Boolean = true): ListenerContainer {
		
		val listenerContainer = ListenerContainer.makeListenerEntry(listenerAttribute)
		addListenerContainer(listenerContainer, enable)
		return listenerContainer
		
	}
	
	/**
	 * Adds a listener container to this event system.
	 * 
	 * @since 6.0
	 * @param listenerContainer the listener container to add
	 * @param enable if the enable event should be send (Default = true)
	 * @return the listener container that was added (same as the listenerContainer argument)
	 * */
	@JvmOverloads
	open fun addListenerContainer(listenerContainer: ListenerContainer, enable: Boolean = true): ListenerContainer {
		
		if (disposed) throw IllegalStateException("EventSystem has been disposed.")
		
		if (listenerContainer !in listenerContainers || this !in listenerContainer.cloneEventSystemList()) {
			listenerContainers.add(listenerContainer)
			listenerContainer.addToEventSystem(this)
			listenerContainer.pushBaseEvent(AdditionEvent(this))
			if (enable) enableListenerContainer(listenerContainer)
		}
		return listenerContainer
		
	}
	
	/**
	 * Enables the specified listener container.
	 * Marks it as enabled, and allows it to receive events and
	 * also sends the enable event.
	 * 
	 * @since 6.0
	 * @param listenerContainer the listener container to enable
	 * */
	open fun enableListenerContainer(listenerContainer: ListenerContainer) {
		
		if (disposed) throw IllegalStateException("EventSystem has been disposed.")
		if (listenerContainer.enabled) return
		
		listenerContainer.enabled = true
		listenerContainer.pushBaseEvent(EnableEvent(this))
		
	}
	
	/**
	 * Removes the specified listener container.
	 * Will also disable it if [disable] is set to true.
	 * 
	 * @since 6.0
	 * @param listenerContainer the listener container to remove
	 * @param type the reason for the removal
	 * @param disable if this listener container should be disabled
	 * @return the listener container that was removed
	 * */
	@JvmOverloads
	open fun removeListenerContainer(listenerContainer: ListenerContainer, type: Int = DisableEvent.NOT_SPECIFIED, disable: Boolean = true): ListenerContainer {
		
		if (disposed) throw IllegalStateException("EventSystem has been disposed.")
		
		if (listenerContainer in listenerContainers || this in listenerContainer.cloneEventSystemList()) {
			if (disable) disableListenerContainer(listenerContainer, type)
			listenerContainer.pushBaseEvent(RemovalEvent(this, type))
			listenerContainers.remove(listenerContainer)
			listenerContainer.removeFromEventSystem(this)
		}
		
		return listenerContainer
		
	}
	
	/**
	 * Disables the specified listener container.
	 * Sends the disable event and marks it as not receiving events.
	 * 
	 * @since 6.0
	 * @param listenerContainer the listener container to be disabled
	 * @param type the reason for the disable.
	 * */
	@JvmOverloads
	open fun disableListenerContainer(listenerContainer: ListenerContainer, type: Int = DisableEvent.NOT_SPECIFIED) {
		
		if (disposed) throw IllegalStateException("EventSystem has been disposed.")
		if (!listenerContainer.enabled) return
		
		listenerContainer.enabled = false
		listenerContainer.pushBaseEvent(DisableEvent(this, type))
		
	}
	
	/**
	 * Pushes the event.
	 * 
	 * If it is the only event in the queue, it will
	 * be pushed right now. If there are other ahead of
	 * it, it will be added to the queue and pushed as
	 * soon as possible.
	 * 
	 * If pushing emediatly is needed, see [pushEventNow]
	 * 
	 * @see pushEventNow
	 * @since 6.0
	 * @param event the event to push
	 * */
	open fun pushEvent(event: BaseEvent) {
		
		if (currentlyPushing()) {
			addToPushQueue(event)
			requestNextPush()
		}else {
			pushEventNow(event)
		}
		
	}
	
	/**
	 * Pushes the event right now.
	 * 
	 * The event skips the queue. Other events waiting in the queue
	 * will have to wait for both events before being pushed.
	 * 
	 * @see pushEvent
	 * @since 6.0
	 * @param event the event to push now
	 * */
	open fun pushEventNow(event: BaseEvent) {
		
		if (disposed) throw IllegalStateException("EventSystem has been disposed.") // if disposed, don't run
		startPushing()
		
		val listenerClones = listenerContainerList
		
		listenerClones
			.asSequence() // we want to be lazy here
			.filter(ListenerContainer::enabled) // make sure listener is enabled
			.filter { !event.isCanceled || it.ignoreCanceled } // event canceled check
			.forEach { listenerContainer ->
				
				try {
					
					listenerContainer.pushBaseEvent(event)
					
				}catch (e: Exception) {
					
					/*
					* Wrap every listener in its own try so the program can continue if there is a crash.
					* catch anything that happens in a listener and stop it from
					* bubbling up and hurting the rest of the program
					* print some information
					* THIS SHOULD BE CAUGHT BEFORE THE EXCEPTION REACHES THIS POINT
					* */
					println("THIS SHOULD NOT HAPPEN")
					println("Listener ${listenerContainer.listener::class.qualifiedName} has an error!")
					e.printStackTrace()
					
				}
				
		}
		
		stopPushing()
		
	}
	
	/**
	 * Adds the event to the queue
	 * 
	 * @param event the event to add to the queue
	 * */
	protected open fun addToPushQueue(event: BaseEvent) = pushQueue.add(event)
	
	/**
	 * Checks to see if it should push an event now
	 * */
	protected open fun requestNextPush() {
		
		if (currentlyPushing()) return // already pushing, so stop
		if (pushQueue.isEmpty()) return // nothing to push, so stop
//		pushQueue.pop()?.let { pushEventNow(it) } // get next one and push it
		pushQueue.poll()?.let { pushEventNow(it) } // get the next one and push it
		
	}
	
	/**
	 * Is something being pushed currently?
	 * 
	 * @return if something is pushing currently
	 * */
	protected open fun currentlyPushing() = pushing > 0
	
	/**
	 * Indicates that something has started pushing.
	 * */
	protected open fun startPushing() {
		pushing++ // add a current pushing
	}
	
	/**
	 * Indicates that something has finished pushing.
	 * 
	 * Pushes the next thing if nothing else is being pushed.
	 * */
	protected open fun stopPushing() {
		pushing-- // remove a current pushing
		if (!currentlyPushing()) {
			// if there are no current pushing events
			requestNextPush()
		}
	}
	
}
