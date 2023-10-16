package edu.rit.edgeconverter.controller;

import edu.rit.edgeconverter.model.Field;
import edu.rit.edgeconverter.util.DataType;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A class to handle button click events for setting data types and default values.
 */
public class BtnListener implements ActionListener {

    private Field field;
    private TextField tfVarcharLength;
    private Button setVarcharLengthButton;
    private TextField tfDefaultValue;
    private boolean dataSaved;

    /**
     * Constructor to initialize the BtnListener with necessary UI components and field data.
     *
     * @param field The field data model.
     * @param tfVarcharLength The text field for varchar length input.
     * @param setVarcharLengthButton The button to set varchar length.
     * @param tfDefaultValue The text field for default value input.
     * @param dataSaved A flag indicating if the data is saved.
     */
    public BtnListener(
        Field field,
        TextField tfVarcharLength,
        Button setVarcharLengthButton,
        TextField tfDefaultValue,
        boolean dataSaved
    ) {
        this.field = field;
        this.tfVarcharLength = tfVarcharLength;
        this.setVarcharLengthButton = setVarcharLengthButton;
        this.tfDefaultValue = tfDefaultValue;
        this.dataSaved = dataSaved;
    }

    /**
     * Handles button click events to set data types and default values.
     *
     * @param e The action event triggered by button click.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Button button = (Button) e.getSource();

        // Loop through all data types to find the one that matches the button name
        for (DataType dataType : DataType.values()) {
            if (button.getName().equals(dataType.getValue())) {
                field.setDataType(dataType);
                break;
            }
        }

        // If the VARCHAR data type is selected, enable the varchar length text field and button
        if (button.getName().equals(DataType.VARCHAR.getValue())) {
            tfVarcharLength.setText(
                Integer.toString(DataType.VARCHAR.getDefaultLength())
            );
            setVarcharLengthButton.setEnabled(true);
        } else {
            // If another data type is selected, disable the varchar length text field and button
            tfVarcharLength.setText("");
            setVarcharLengthButton.setEnabled(false);
        }

        // Clear the default value text field and update the field data model
        tfDefaultValue.setText("");
        field.setDefaultValue("");
        dataSaved = false;
    }
}
