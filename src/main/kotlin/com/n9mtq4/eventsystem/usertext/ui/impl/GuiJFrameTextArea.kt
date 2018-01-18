package com.n9mtq4.eventsystem.usertext.ui.impl

import com.n9mtq4.eventsystem.core.ui.SimpleEventSystemUI
import com.n9mtq4.eventsystem.usertext.ui.attributes.HasFrame
import com.n9mtq4.eventsystem.usertext.ui.attributes.Textable
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JFrame
import javax.swing.JTextField

/**
 * Created by will on 1/6/2018 at 9:24 AM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
abstract class GuiJFrameTextArea : SimpleEventSystemUI(), Textable, HasFrame {
	
	protected lateinit var frame: JFrame
	protected lateinit var field: JTextField
	
	private val history = ArrayList<String>()
	private var historyIndex = 0
	
	protected abstract fun onFieldEnter(event: ActionEvent)
	
	override fun dispose() {
		// TODO: commented out in LogWindow. why?
//		frame.dispose()
	}
	
	override fun getJFrame(): JFrame = frame
	
	protected fun updateHistory(text: String) {
		history.add(text)
		historyIndex = history.size
	}
	
	protected fun JTextField.addFieldEnterHook() {
		this.addActionListener {
			onFieldEnter(it)
		}
	}
	
	protected fun JTextField.addHistorySupport() {
		this.addKeyListener(object : KeyListener {
			override fun keyTyped(e: KeyEvent?) {}
			override fun keyReleased(e: KeyEvent?) {}
			
			override fun keyPressed(e: KeyEvent) {
				when (e.keyCode) {
					KeyEvent.VK_UP -> {
						if (historyIndex > 0) {
							historyIndex--
							field.apply {
								text = history[historyIndex]
								caretPosition = text.length
							}
						}
					}
					KeyEvent.VK_DOWN -> {
						if (historyIndex < history.size - 1) {
							historyIndex++
							field.apply {
								text = history[historyIndex]
								caretPosition = text.length
							}
						}
					}
				}
				
			}
		})
	}
	
}
