package com.n9mtq4.eventsystem.core.utils

/**
 * A simple hash map that supports multiple values.
 * This is not complete and does not work in all situations.
 * This is ONLY meant for use in the [com.n9mtq4.eventsystem.core.listener.ListenerContainer].
 * There is a good chance that it will not work elsewhere.
 * 
 * Created by will on 1/4/2018 at 12:14 AM.
 * 
 * WARNING: This is a special use hashmap that can not have
 * values removed from it!
 *
 * @author Will "n9Mtq4" Bresnahan
 */
@Suppress("unused")
class MultiHashMap<K, V>: HashMap<K, MutableList<V>>() {
	
	/**
	 * Adds a single value into the multi hashmap
	 * 
	 * @param key the key to add the value to
	 * @param value the value to append into that key
	 * */
	fun multiPutSingle(key: K, value: V) = multiPutList(key, listOf(value))
	
	/**
	 * Adds a list of values into the multi hashmap
	 * 
	 * @param key the key to add the values to
	 * @param values a list of values to append into that key
	 * */
	fun multiPutList(key: K, values: List<V>) {
		
		// get the list for the key
		var list = get(key)
		
		// if the list doesn't exist make a new one now
		if (list == null) {
			list = mutableListOf()
			put(key, list)
		}
		
		// add the new values into the list
		list.addAll(values)
		
	}
	
	/**
	 * Removes the given value from the given key
	 * 
	 * @param key the key to remove the value from
	 * @param value the value to remove
	 * */
	fun multiRemoveSingle(key: K, value: V) = multiRemoveList(key, listOf(value))
	
	/**
	 * Removes the given values from the given key
	 * 
	 * @param key the key to remove the values from
	 * @param values the values to remove
	 * */
	fun multiRemoveList(key: K, values: List<V>) {
		
		val list = get(key)
		list?.removeAll(values)
		
	}
	
}
