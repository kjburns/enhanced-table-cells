/*
 * This file is based on a static package-private class in JTable. As such, this file is 
 * distributed under GPL2 with classpath exception. Oracle's original copyright notice is 
 * listed here:
 * 
 * Copyright (c) 1997, 2010, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.gmail.at.kevinburnseit.swing.util.table;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 * Base class for creating table cell editors which have the capability to validate their
 * contents. Logic for validation is my own.
 * 
 * @author Kevin J. Burns
 *
 */
public abstract class ValidatingCellEditor extends GenericEditor {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7951386012340703301L;
    private static final Border errorBorder = new LineBorder(Color.RED);
    private static int defaultHorizontalAlignment = JTextField.LEADING;
    private ArrayList<CellValidationErrorListener> errorListeners = new ArrayList<>();
    private boolean numeric = false;
    
    /**
     * Sole constructor. Creates a validating table cell editor.
     */
    public ValidatingCellEditor() {
        super();
    }
    
    /**
     * Adds a listener for table cell edit validation failures.
     * @param l Listener to notify
     */
    public void addValidationErrorListener(CellValidationErrorListener l) {
    	this.errorListeners.add(l);
    }
    
    /**
     * Removes a listener for table cell edit validation failures.
     * @param l Listener to notify.
     */
    public void removeValidationErrorListener(CellValidationErrorListener l) {
    	this.errorListeners.remove(l);
    }

    @Override
    public boolean stopCellEditing() {
    	if (!super.stopCellEditing()) return false;
    	
    	if (!this.performValidation(this.value)) {
    		this.setCellBorder(ValidatingCellEditor.errorBorder);
    		this.fireValidationFailed();
    		return false;
    	}
    	
    	return true;
    }

    private void fireValidationFailed() {
    	for (CellValidationErrorListener l : this.errorListeners) {
    		l.validationFailed();
    	}
	}

    /**
     * This function is called when it is necessary to validate a table cell.
     * @param value The value to validate
     * @return <code>true</code> if the value is OK; <code>false</code> otherwise.
     */
	protected abstract boolean performValidation(Object value);

	/**
	 * @param newBorder 
	 */
	private void setCellBorder(Border newBorder) {
		((JComponent)getComponent()).setBorder(newBorder);
	}

	/**
	 * @param orientation
	 */
	protected void setHorizontalAlignment(int orientation) {
		((JTextField)this.getComponent()).setHorizontalAlignment(orientation);
	}

	/**
	 * Fetches whether this table cell editor is intended to be numeric.
	 * @return
	 */
	public boolean isNumeric() {
		return numeric;
	}

	/**
	 * Sets a flag for whether this editor is intended to be numeric. The practical effect
	 * of this is that a numeric editor will be right-aligned, while a non-numeric
	 * editor will be leading-aligned.
	 * @param numeric
	 */
	public void setNumeric(boolean numeric) {
		this.numeric = numeric;
		
		this.setHorizontalAlignment(this.numeric ? 
				JTextField.RIGHT : 
				ValidatingCellEditor.defaultHorizontalAlignment);
	}
}
