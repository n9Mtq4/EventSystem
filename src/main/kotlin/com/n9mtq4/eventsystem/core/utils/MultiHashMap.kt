package com.n9mtq4.eventsystem.core.utils

/**
 * Created by will on 1/4/2018 at 12:14 AM.
 *
 * WARNING: This is a special use hashmap that can not have
 * values removed from it!
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class MultiHashMap<K, V>: HashMap<K, List<V>>() {
	
	// TODO: support removing values. Since removing isn't needed, it isn't implemented here
	
	/**
	 * @return the previous value(s) or an empty list if there weren't any
	 * */
	fun mput(key: K, value: V): List<V> {
		
		// if it isn't already there add a blank list
		val old = get(key)
		if (old == null) put(key, emptyList())
		
		val preList = old ?: emptyList() // a NPE should never happen, since we putIfAbsent right before
		val newList = preList + value
		
		put(key, newList.distinct())
		
		return preList
		
	}
	
	/**
	 * @return the previous value(s) or an empty list if there weren't any
	 * */
	fun aput(key: K, value: List<V>): List<V> {
		
		// if it isn't already there add a blank list
		val old = get(key)
		if (old == null) put(key, emptyList())
		
		val preList = old ?: emptyList() // a NPE should never happen, since we putIfAbsent right before
		val newList = preList + value
		
		put(key, newList.distinct())
		
		return preList
		
	}
	
}
