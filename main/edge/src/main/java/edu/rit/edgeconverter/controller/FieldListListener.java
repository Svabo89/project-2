package edu.rit.edgeconverter.controller;

import edu.rit.edgeconverter.model.Field;
import edu.rit.edgeconverter.view.ConverterGUI;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Hey, meet FieldListListener! It's a cool listener that keeps an eye on the selected item in a list view.
 * When you pick something new, it updates another list view with the fields of the selected item. Neat, huh??
 */
public class FieldListListener implements ChangeListener<String> {

    private ConverterGUI gui;
    private MenuListener menuListener;

    /**
     * Constructor time! We need a ConverterGUI and a MenuListener to get the party started.
     */
    public FieldListListener(ConverterGUI gui, MenuListener menuListener) {
        this.gui = gui;
        this.menuListener = menuListener;
    }

    /**
     * So here's the deal. Whenever you select something new in the list view, this method jumps into action.
     * It grabs the fields associated with the selected item and updates another list view with them.
     * It's like magic, but it's just code. Cool code.
     */
    @Override
    public void changed(
            ObservableValue<? extends String> observable,
            String oldValue,
            String newValue
    ) {
        // If there's something new selected (not null), then let's roll!
        if (newValue != null) {
            // Grabbing the fields associated with the selected item. It's like a treasure hunt.
            ArrayList<Field> fieldArray = menuListener.getMappedTable().get(newValue);

            // Time to update the other list view with these shiny new fields. Ta-da!
            menuListener.populateListViewFieldList(
                    fieldArray.toArray(new Field[fieldArray.size()])
            );
        }
    }
}
//fieldlistener out