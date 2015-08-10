package com.gmail.at.kevinburnseit.swing.util.table;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
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
public abstract class ValidatingCellEditor extends DefaultCellEditor {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7951386012340703301L;
    private static final Border errorBorder = new LineBorder(Color.RED);
    private static final Border normalBorder = new LineBorder(Color.black);
    private static int defaultHorizontalAlignment = JTextField.LEADING;
    private ArrayList<CellValidationErrorListener> errorListeners = new ArrayList<>();
    private boolean numeric = false;
    protected Object value;
    
    /**
     * Sole constructor. Creates a validating table cell editor.
     */
    public ValidatingCellEditor() {
        super(new JTextField());
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
    final public boolean stopCellEditing() {
    	this.value = (String)super.getCellEditorValue();
		try {
	    	this.updateValue((String)this.value);
	    	boolean ok;
			ok = this.performValidation(this.value);
			if (!ok) throw new Exception();
		} 
		catch (Exception e) {
    		this.setCellBorder(ValidatingCellEditor.errorBorder);
    		this.fireValidationFailed();
    		return false;
		}
    	
		return super.stopCellEditing();
    }

    @Override
	public Component getTableCellEditorComponent(JTable table, Object val,
			boolean isSelected, int row, int column) {
    	this.setCellBorder(normalBorder);
		return super.getTableCellEditorComponent(table, val, isSelected, row, column);
	}

	/**
     * Derived classes must convert the string to an object consistent with the derived 
     * class.  The object must be stored in
     * the member variable <code>value</code>. If the conversion process proves 
     * problematic, throw an exception.
     * @throws Exception
     */
    protected abstract void updateValue(String txt) throws Exception;

	private void fireValidationFailed() {
    	for (CellValidationErrorListener l : this.errorListeners) {
    		l.validationFailed();
    	}
	}

    /**
     * This function is called when it is necessary to validate a table cell.
     * @param v The value to validate. Assuming {@link #updateValue(String)} has been
     * executed correctly, derived classes should be able to directly cast this parameter
     * into the expected object type.
     * @return <code>true</code> if the value is OK; <code>false</code> otherwise.
     */
	protected abstract boolean performValidation(Object v);

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

	@Override
	public Object getCellEditorValue() {
		return this.value;
	}
}
