package com.gmail.at.kevinburnseit.swing.util.table;


/**
 * Base class for a validating table cell editor which accepts integers.
 * @author Kevin J. Burns
 *
 */
public abstract class IntegerEditor extends ValidatingCellEditor {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8543699252414040612L;

	public IntegerEditor() {
		super();
		this.setNumeric(true);
	}

	protected abstract boolean performValidation(Object v);

	@Override
	protected void updateValue(String txt) throws Exception {
		this.value = Integer.valueOf(txt);
	}
}