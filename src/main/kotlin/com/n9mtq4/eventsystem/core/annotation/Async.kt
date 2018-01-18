package com.n9mtq4.eventsystem.core.annotation

import java.lang.annotation.Inherited

/**
 * Created by will on 1/3/2018 at 9:22 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

/**
 * This annotation is added to methods in the ConsoleListener interface.
 * Adding this annotation makes the method run in its own coroutine or thread.
 * 
 * @param type of backing process for async
 * @since 4.3
 * @version 6.0
 * @author Will "n9Mtq4" Bresnahan
 * @see AsyncType
 * */
@Inherited
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Async(val type: AsyncType = AsyncType.COROUTINE)

/**
 * The backing implementation of the Async annotation.
 *
 * @since 6.0
 * @version 6.0
 * @author Will "n9Mtq4" Bresnahan
 * @see Async
 * */
enum class AsyncType {
	
	/**
	 * Don't use any form of asynchronous processing
	 * */
	NONE,
	
	/**
	 * Use coroutines for asynchronous listeners
	 * */
	COROUTINE,
	
	/**
	 * Use a new thread for asynchronous listeners
	 * */
	THREAD
	
}
