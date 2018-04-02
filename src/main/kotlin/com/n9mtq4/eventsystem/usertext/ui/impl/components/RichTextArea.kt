package com.n9mtq4.eventsystem.usertext.ui.impl.components

import java.awt.Color
import java.awt.Image
import javax.swing.ImageIcon
import javax.swing.JTextPane
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants
import javax.swing.text.StyleContext
import javax.swing.text.StyledDocument

/**
 * A text area that supports colors and images.
 * 
 * Created by will on 1/6/2018 at 9:37 AM.
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
open class RichTextArea : JTextPane {
	
	/**
	 * Is this Rich Text Area editable by
	 * the user?
	 * */
	var userEditable: Boolean = false
		set(value) {
			field = value
			if (!userEditable) super.setEditable(false)
		}
	
	constructor(doc: StyledDocument?) : super(doc)
	constructor() : super()
	
	/**
	 * Appends a string onto the Rich Text Area.
	 *
	 * @param msg the String to append
	 * @param c   the color that the string will be in
	 */
	fun append(msg: String, color: Color?) {
		
		val edit = super.isEditable()
		super.setEditable(true)
//		http://stackoverflow.com/a/9652143/5196460
		val sc = StyleContext.getDefaultStyleContext()
		var aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color ?: Color.BLACK)
		
		aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console")
		aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED)
		
		val len = this.document.length
		this.caretPosition = len
		this.setCharacterAttributes(aset, false)
		this.replaceSelection(msg)
		super.setEditable(edit)
		
	}
	
	/**
	 * Sets the text of the Rich Text Area
	 *
	 * @since v0.1
	 * @param string The new String to set the text to
	 */
	override fun setText(string: String?) {
		val edit = super.isEditable()
		super.setEditable(true)
		super.setText(string)
		super.setEditable(edit)
	}
	
	/**
	 * Adds an image to the component.
	 *
	 * @since v0.1
	 * @param image the picture to append.
	 */
	fun appendPicture(image: Image) {
		
		val len = this.document.length
		this.caretPosition = len
		this.insertIcon(ImageIcon(image))
		
	}
	
}
