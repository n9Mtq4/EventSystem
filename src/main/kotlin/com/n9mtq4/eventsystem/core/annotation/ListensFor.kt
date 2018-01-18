package com.n9mtq4.eventsystem.core.annotation

import java.lang.annotation.Inherited
import kotlin.reflect.KClass

/**
 * Created by will on 1/3/2018 at 9:32 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

/**
 * The annotation that tells the [com.n9mtq4.logwindow.EventSystem]
 * that the method should be called when the specified event is sent.
 * 
 * @param clazz The class of the event that this method is receiving
 * @since 5.1
 * @version 6.0
 * @author Will "n9Mtq4" Bresnahan
 * */
@Inherited
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class ListensFor(val clazz: KClass<*> = ListensForInherit::class)

/**
 * This is just a shorthand for getting the ListensForInherit
 * class
 * */
internal val listensForInherit = ListensForInherit::class

/**
 * This class will tell the [com.n9mtq4.logwindow.EventSystem] to
 * inherit the type for listening from the first parameter type.
 * */
internal class ListensForInherit
