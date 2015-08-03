package com.gmail.at.kevinburnseit.swing.util.table;

/**
 * Listener for validation failures on {@link ValidatingCellEditor}.
 * @author Kevin J. Burns
 *
 */
public interface CellValidationErrorListener {
	/**
	 * Called when the validation of a table cell fails.
	 */
	void validationFailed();
}
