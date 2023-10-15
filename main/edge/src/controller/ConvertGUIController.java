package controller;

import view.ConvertGUI;

public class ConvertGUIController {

    private ConvertGUI convertGUI;

    public ConvertGUIController(ConvertGUI convertGUI) {

        this.convertGUI = convertGUI;

        setListeners();

    }

    public void setListeners() {

        convertGUI.addJbDTDefineRelationsListener(new JbDTDefineRelationsListener(convertGUI));
        convertGUI.addJbDTMoveButtonListeners(new JbDTMoveButtonListeners(convertGUI));
        convertGUI.addJbDTDefaultValueListener(new JbDTDefaultValueListener(convertGUI));
        convertGUI.addJbDTVarcharListener(new JbDTVarcharListener(convertGUI));
        convertGUI.addJbDRDefineTablesListener(new JbDRDefineTablesListener(convertGUI));
        convertGUI.addJbDRBindRelationListener(new JbDRBindRelationListener(convertGUI));
        convertGUI.addTableListSelectionListener(new TableListSelectionListener(convertGUI));
        convertGUI.addFieldTableListSelectionListener(new FieldTableListSelectionListener(convertGUI));
        convertGUI.addJcheckDTDisallowNullItemListener(new JcheckDTDisallowNullItemListener(convertGUI));
        convertGUI.addJcheckDTPrimaryKeyItemListener(new JcheckDTPrimaryKeyItemListener(convertGUI));
        convertGUI.addJlDRTablesRelationsListSelectionListener(new JlDRTablesRelationsListSelectionListener(convertGUI));
        convertGUI.addJlDRFieldsTablesRelationsListSelectionListener(new JlDRFieldsTablesRelationsListSelectionListener(convertGUI));
        convertGUI.addJlDRTablesRelatedToListSelectionListener(new JlDRTablesRelatedToListSelectionListener(convertGUI));
        convertGUI.addJlDRFieldsTablesRelationsListSelectionListener(new JlDRFieldsTablesRelationsListSelectionListener(convertGUI));

        convertGUI.addMenuListener(new MenuListener(convertGUI));
        convertGUI.addCreateDDLButtonListener(new CreateDDLButtonListener(convertGUI));
        convertGUI.addRadioButtonListener(new RadioButtonListener(convertGUI));
        convertGUI.addWindowListener(new CustomWindowListener(convertGUI));

    }

}
