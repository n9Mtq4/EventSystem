package com.n9mtq4.eventsystem.usertext.ui.impl

import com.n9mtq4.eventsystem.usertext.events.PrintEvent
import com.n9mtq4.eventsystem.usertext.events.UserTextEvent
import java.awt.BorderLayout
import java.awt.event.ActionEvent
import javax.swing.*
import javax.swing.text.DefaultCaret

/**
 * Created by will on 1/6/2018 at 1:29 AM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class GuiJFrameLightWeight : GuiJFrameTextArea() {
	
	private lateinit var noWrapPanel: JPanel
	private lateinit var area: JTextArea
	private lateinit var scrollArea: JScrollPane
	
	override fun init() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
		}catch (e: Exception) {
			e.printStackTrace()
		}
		
		frame = JFrame("Event System Log (Lite)")
		
		area = JTextArea().apply { isEditable = false }
		(area.caret as DefaultCaret).updatePolicy = DefaultCaret.ALWAYS_UPDATE
		
		noWrapPanel = JPanel(BorderLayout())
		noWrapPanel.add(area)
		scrollArea = JScrollPane(noWrapPanel).apply { 
			horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
			verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
		}
		
		field = JTextField()
		
		frame.run { 
			add(scrollArea, BorderLayout.CENTER)
			add(field, BorderLayout.SOUTH)
		}
		
		frame.run { 
			defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
			pack()
			setSize(360, 240)
			setLocationRelativeTo(null)
			isVisible = true
			setSize(360, 240)
			setLocationRelativeTo(null)
		}
		
		field.requestFocusInWindow()
		
		field.addFieldEnterHook()
		field.addHistorySupport()
		
	}
	
	override fun onFieldEnter(event: ActionEvent) {
		
		val source = event.source as JTextField
		val text = source.text
		updateHistory(text)
		if (text.isNotEmpty()) {
			source.text = ""
			cloneEventSystemList().forEach { it.pushEvent(UserTextEvent(it, text)) }
		}
		
	}
	
	override fun print(printEvent: PrintEvent) {
		
		(area.caret as DefaultCaret).updatePolicy = DefaultCaret.ALWAYS_UPDATE
		val ending = if (printEvent.newLine) "\n" else ""
		area.append("${printEvent.obj}$ending")
		
	}
	
	override fun getText(): String = area.text
	
	override fun setText(text: String) {
		area.text = text
	}
	
}
