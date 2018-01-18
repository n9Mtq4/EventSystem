package com.n9mtq4.eventsystem.core

import com.n9mtq4.eventsystem.core.event.*
import com.n9mtq4.eventsystem.core.listener.ListenerAttribute
import com.n9mtq4.eventsystem.core.listener.ListenerContainer
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CopyOnWriteArrayList

/**
 * The main class of the LogWindowKt Library.
 * 
 * Created by will on 1/3/2018 at 9:22 PM.
 * 
 * @author Will "n9Mtq4" Bresnahan
 */

typealias ListenerContainerList = CopyOnWriteArrayList<ListenerContainer>

class EventSystem {
	
	val listenerContainers = ListenerContainerList()
	
	var disposed: Boolean = false
	
	var pushing: Int = 0
	val pushQueue = ConcurrentLinkedQueue<BaseEvent>(mutableListOf())
	
	fun dispose() {
		if (disposed) return
		cloneListenerContainerList().forEach { removeListenerContainer(it, DisableEvent.EVENT_SYSTEM_DISPOSE) }
		disposed = true
	}
	
	fun addListenerAttribute(listenerAttribute: ListenerAttribute, enable: Boolean = true): ListenerContainer {
		
		val listenerContainer = ListenerContainer.makeListenerEntry(listenerAttribute)
		addListenerContainer(listenerContainer, enable)
		return listenerContainer
		
	}
	
	fun addListenerContainer(listenerContainer: ListenerContainer, enable: Boolean = true): ListenerContainer {
		
		if (disposed) return listenerContainer
		
		if (listenerContainer !in listenerContainers || this !in listenerContainer.cloneEventSystemList()) {
			listenerContainers.add(listenerContainer)
			listenerContainer.addToEventSystem(this)
			listenerContainer.pushBaseEvent(AdditionEvent(this))
			if (enable) enableListenerContainer(listenerContainer)
		}
		return listenerContainer
		
	}
	
	fun enableListenerContainer(listenerContainer: ListenerContainer) {
		
		if (disposed) return
		if (listenerContainer.enabled) return
		
		listenerContainer.enabled = true
		listenerContainer.pushBaseEvent(EnableEvent(this))
		
	}
	
	fun removeListenerContainer(listenerContainer: ListenerContainer, type: Int = DisableEvent.NOT_SPECIFIED, disable: Boolean = true): ListenerContainer {
		
		if (disposed) return listenerContainer
		
		if (listenerContainer in listenerContainers || this in listenerContainer.cloneEventSystemList()) {
			if (disable) disableListenerContainer(listenerContainer, type)
			listenerContainer.pushBaseEvent(RemovalEvent(this, type))
			listenerContainers.remove(listenerContainer)
			listenerContainer.removeFromEventSystem(this)
		}
		
		return listenerContainer
		
	}
	
	fun disableListenerContainer(listenerContainer: ListenerContainer, type: Int = DisableEvent.NOT_SPECIFIED) {
		
		if (disposed) return
		if (!listenerContainer.enabled) return
		
		listenerContainer.enabled = false
		listenerContainer.pushBaseEvent(DisableEvent(this, type))
		
	}
	
	fun pushEvent(event: BaseEvent) {
		
		if (currentlyPushing()) {
			addToPushQueue(event)
			requestNextPush()
		}else {
			pushEventNow(event)
		}
		
	}
	
	fun pushEventNow(event: BaseEvent) {
		
		if (disposed) return // if disposed, don't run
		startPushing()
		
		val listenerClones = cloneListenerContainerList()
		
		listenerClones.forEach { listenerContainer ->
			
			try {
				
				// make sure we can push to the listeners
				if (listenerContainer.enabled && (!event.isCanceled || listenerContainer.ignoreCanceled)) {
					
					listenerContainer.pushBaseEvent(event)
					
				}
				
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
	
	private fun addToPushQueue(event: BaseEvent) = pushQueue.add(event)
	
	private fun requestNextPush() {
		
		if (currentlyPushing()) return // already pushing, so stop
		if (pushQueue.isEmpty()) return // nothing to push, so stop
//		pushQueue.pop()?.let { pushEventNow(it) } // get next one and push it
		pushQueue.poll()?.let { pushEventNow(it) } // get the next one and push it
		
	}
	
	private fun currentlyPushing() = pushing > 0
	
	private fun startPushing() {
		pushing++ // add a current pushing
	}
	
	private fun stopPushing() {
		pushing-- // remove a current pushing
		if (!currentlyPushing()) {
			// if there are no current pushing events
			requestNextPush()
		}
	}
	
	private fun cloneListenerContainerList() = listenerContainers.toList()
	
}
