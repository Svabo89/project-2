package view;

import parsers.FileParser;
import model.ExampleFileFilter;
import model.Table;
import model.Field;
import parsers.SaveFileParser;
import parsers.ConvertFileParser;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConvertGUI {

    private static final Logger logger = LogManager.getLogger();
    public static final int HORIZ_SIZE = 635;
    public static final int VERT_SIZE = 400;
    public static final int HORIZ_LOC = 100;
    public static final int VERT_LOC = 100;
    public static final String DEFINE_TABLES = "Define Tables";
    public static final String DEFINE_RELATIONS = "Define Relations";
    public static final String CANCELLED = "CANCELLED";
    private JFileChooser jfc;
    private ExampleFileFilter eff;
    private File parseFile;
    private File saveFile;
    private File outputDir;
    private File outputClassDir;
    private String truncatedFilename;
    private String databaseName;
    private PrintWriter pw;
    private Table[] tables; // master copy of Table objects
    private Field[] fields; // master copy of Field objects
    private Table currentDTTable;
    private Table currentDRTable1;
    private Table currentDRTable2;
    private Field currentDTField;
    private Field currentDRField1;
    private Field currentDRField2;
    private static boolean readSuccess = true; // this tells GUI whether to populate JList components or not
    private boolean dataSaved = true;
    private ArrayList<Object> alSubclasses;
    private ArrayList<Object> alProductNames;
    private String[] productNames;
    private Object[] objSubclasses;
    private String selectedFileType;
    private static final String CERTAINTY_INQUIRY = "Are you sure?";
    private static final String CREATE_MESSAGE = "Create DDL";

    // Define Tables screen objects
    private JFrame jfDT;
    private JButton jbDTCreateDDL;
    private JButton jbDTDefineRelations;
    private JButton jbDTVarchar;
    private JButton jbDTDefaultValue;
    private JButton jbDTMoveUp;
    private JButton jbDTMoveDown;
    private JRadioButton[] jrbDataType;
    private String[] strDataType;
    private JCheckBox jcheckDTDisallowNull;
    private JCheckBox jcheckDTPrimaryKey;
    private JTextField jtfDTVarchar;
    private JTextField jtfDTDefaultValue;
    private JList<Object> jlDTTablesAll;
    private JList<Object> jlDTFieldsTablesAll;
    private DefaultListModel<Object> dlmDTTablesAll;
    private DefaultListModel<Object> dlmDTFieldsTablesAll;
    private JMenuItem jmiDTOpenFile;
    private JMenuItem jmiDTSave;
    private JMenuItem jmiDTSaveAs;
    private JMenuItem jmiDTExit;
    private JMenuItem jmiDTOptionsShowProducts;
    private JMenuItem jmiDTHelpAbout;

    // Define Relations screen objects
    private JFrame jfDR;
    private JButton jbDRCreateDDL;
    private JButton jbDRDefineTables;
    private JButton jbDRBindRelation;
    private JList<Object> jlDRTablesRelations;
    private JList<Object> jlDRTablesRelatedTo;
    private JList<Object> jlDRFieldsTablesRelations;
    private JList<Object> jlDRFieldsTablesRelatedTo;
    private DefaultListModel<Object> dlmDRTablesRelations;
    private DefaultListModel<Object> dlmDRTablesRelatedTo;
    private DefaultListModel<Object> dlmDRFieldsTablesRelations;
    private DefaultListModel<Object> dlmDRFieldsTablesRelatedTo;
    private JMenuItem jmiDROpenFile;
    private JMenuItem jmiDRSave;
    private JMenuItem jmiDRSaveAs;
    private JMenuItem jmiDRExit;
    private JMenuItem jmiDROptionsOutputLocation;
    private JMenuItem jmiDROptionsShowProducts;
    private JMenuItem jmiDRHelpAbout;

    public ConvertGUI() {

        this.showGUI();

    } // ConvertGUI.ConvertGUI()

    public void showGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // use the OS native LAF, as opposed to
            // default Java LAF

        } catch (Exception e) {
            logger.error("Error setting native LAF", e);
        }
        createDTScreen();
        createDRScreen();
    } // showGUI()

    public void createDTScreen() {// create Define Tables screen
        jfDT = new JFrame(DEFINE_TABLES);
        jfDT.setLocation(HORIZ_LOC, VERT_LOC);
        jfDT.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        jfDT.getContentPane().setLayout(new BorderLayout());
        jfDT.setVisible(true);
        jfDT.setSize(HORIZ_SIZE + 150, VERT_SIZE);

        // setup menubars and menus
        JMenuBar jmbDTMenuBar = new JMenuBar();
        jfDT.setJMenuBar(jmbDTMenuBar);

        JMenu jmDTFile = new JMenu("File");
        jmDTFile.setMnemonic(KeyEvent.VK_F);
        jmbDTMenuBar.add(jmDTFile);
        jmiDTOpenFile = new JMenuItem("Open File");
        jmiDTOpenFile.setMnemonic(KeyEvent.VK_E);
        jmiDTSave = new JMenuItem("Save");
        jmiDTSave.setMnemonic(KeyEvent.VK_S);
        jmiDTSave.setEnabled(false);
        jmiDTSaveAs = new JMenuItem("Save As...");
        jmiDTSaveAs.setMnemonic(KeyEvent.VK_A);
        jmiDTSaveAs.setEnabled(false);
        jmiDTExit = new JMenuItem("Exit");
        jmiDTExit.setMnemonic(KeyEvent.VK_X);
        jmDTFile.add(jmiDTOpenFile);
        jmDTFile.add(jmiDTSave);
        jmDTFile.add(jmiDTSaveAs);
        jmDTFile.add(jmiDTExit);

        JMenu jmDTOptions = new JMenu("Options");
        jmDTOptions.setMnemonic(KeyEvent.VK_O);
        jmbDTMenuBar.add(jmDTOptions);
        jmiDTOptionsShowProducts = new JMenuItem("Show Database Products Available");
        jmiDTOptionsShowProducts.setMnemonic(KeyEvent.VK_H);
        jmiDTOptionsShowProducts.setEnabled(false);
        jmDTOptions.add(jmiDTOptionsShowProducts);

        JMenu jmDTHelp = new JMenu("Help");
        jmDTHelp.setMnemonic(KeyEvent.VK_H);
        jmbDTMenuBar.add(jmDTHelp);
        jmiDTHelpAbout = new JMenuItem("About");
        jmiDTHelpAbout.setMnemonic(KeyEvent.VK_A);
        jmDTHelp.add(jmiDTHelpAbout);

        jfc = new JFileChooser("./resources");
        JFileChooser jfcOutputDir = new JFileChooser();
        eff = new ExampleFileFilter(new String[]{"edg", "sav", "xml", "json"}, "Different Diagrammer Files");
        jfcOutputDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        JPanel jpDTBottom = new JPanel(new GridLayout(1, 2));

        jbDTCreateDDL = new JButton(CREATE_MESSAGE);
        jbDTCreateDDL.setEnabled(false);

        jbDTDefineRelations = new JButton(DEFINE_RELATIONS);
        jbDTDefineRelations.setEnabled(false);

        jpDTBottom.add(jbDTDefineRelations);
        jpDTBottom.add(jbDTCreateDDL);
        jfDT.getContentPane().add(jpDTBottom, BorderLayout.SOUTH);

        JPanel jpDTCenter = new JPanel(new GridLayout(1, 3));
        JPanel jpDTCenterRight = new JPanel(new GridLayout(1, 2));
        dlmDTTablesAll = new DefaultListModel<>();
        jlDTTablesAll = new JList<>(dlmDTTablesAll);
        dlmDTFieldsTablesAll = new DefaultListModel<>();
        jlDTFieldsTablesAll = new JList<>(dlmDTFieldsTablesAll);

        JPanel jpDTMove = new JPanel(new GridLayout(2, 1));
        jbDTMoveUp = new JButton("^");
        jbDTMoveUp.setEnabled(false);

        jbDTMoveDown = new JButton("v");
        jbDTMoveDown.setEnabled(false);

        jpDTMove.add(jbDTMoveUp);
        jpDTMove.add(jbDTMoveDown);

        JScrollPane jspDTTablesAll = new JScrollPane(jlDTTablesAll);
        JScrollPane jspDTFieldsTablesAll = new JScrollPane(jlDTFieldsTablesAll);
        JPanel jpDTCenter1 = new JPanel(new BorderLayout());
        JPanel jpDTCenter2 = new JPanel(new BorderLayout());
        JLabel jlabDTTables = new JLabel("All Tables", SwingConstants.CENTER);
        JLabel jlabDTFields = new JLabel("Fields List", SwingConstants.CENTER);
        jpDTCenter1.add(jlabDTTables, BorderLayout.NORTH);
        jpDTCenter2.add(jlabDTFields, BorderLayout.NORTH);
        jpDTCenter1.add(jspDTTablesAll, BorderLayout.CENTER);
        jpDTCenter2.add(jspDTFieldsTablesAll, BorderLayout.CENTER);
        jpDTCenter2.add(jpDTMove, BorderLayout.EAST);
        jpDTCenter.add(jpDTCenter1);
        jpDTCenter.add(jpDTCenter2);
        jpDTCenter.add(jpDTCenterRight);

        strDataType = Field.getStrDataType(); // get the list of currently supported data types
        jrbDataType = new JRadioButton[strDataType.length]; // create array of JRadioButtons, one for each supported
        // data type
        ButtonGroup bgDTDataType = new ButtonGroup();
        JPanel jpDTCenterRight1 = new JPanel(new GridLayout(strDataType.length, 1));
        for (int i = 0; i < strDataType.length; i++) {
            jrbDataType[i] = new JRadioButton(strDataType[i]); // assign label for radio button from String array
            jrbDataType[i].setEnabled(false);
            bgDTDataType.add(jrbDataType[i]);
            jpDTCenterRight1.add(jrbDataType[i]);
        }
        jpDTCenterRight.add(jpDTCenterRight1);

        jcheckDTDisallowNull = new JCheckBox("Disallow Null");
        jcheckDTDisallowNull.setEnabled(false);

        jcheckDTPrimaryKey = new JCheckBox("Primary Key");
        jcheckDTPrimaryKey.setEnabled(false);

        jbDTDefaultValue = new JButton("Set Default Value");
        jbDTDefaultValue.setEnabled(false);

        jtfDTDefaultValue = new JTextField();
        jtfDTDefaultValue.setEditable(false);

        jbDTVarchar = new JButton("Set Varchar Length");
        jbDTVarchar.setEnabled(false);

        jtfDTVarchar = new JTextField();
        jtfDTVarchar.setEditable(false);

        JPanel jpDTCenterRight2 = new JPanel(new GridLayout(6, 1));
        jpDTCenterRight2.add(jbDTVarchar);
        jpDTCenterRight2.add(jtfDTVarchar);
        jpDTCenterRight2.add(jcheckDTPrimaryKey);
        jpDTCenterRight2.add(jcheckDTDisallowNull);
        jpDTCenterRight2.add(jbDTDefaultValue);
        jpDTCenterRight2.add(jtfDTDefaultValue);
        jpDTCenterRight.add(jpDTCenterRight1);
        jpDTCenterRight.add(jpDTCenterRight2);
        jpDTCenter.add(jpDTCenterRight);
        jfDT.getContentPane().add(jpDTCenter, BorderLayout.CENTER);
        jfDT.validate();
    } // createDTScreen

    public void createDRScreen() {
        // create Define Relations screen
        jfDR = new JFrame(DEFINE_RELATIONS);
        jfDR.setSize(HORIZ_SIZE, VERT_SIZE);
        jfDR.setLocation(HORIZ_LOC, VERT_LOC);
        jfDR.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        jfDR.getContentPane().setLayout(new BorderLayout());
        JMenuBar jmbDRMenuBar = new JMenuBar();
        jfDR.setJMenuBar(jmbDRMenuBar);
        JMenu jmDRFile = new JMenu("File");
        jmDRFile.setMnemonic(KeyEvent.VK_F);
        jmbDRMenuBar.add(jmDRFile);
        jmiDROpenFile = new JMenuItem("Open File");
        jmiDROpenFile.setMnemonic(KeyEvent.VK_E);
        jmiDRSave = new JMenuItem("Save");
        jmiDRSave.setMnemonic(KeyEvent.VK_S);
        jmiDRSave.setEnabled(false);
        jmiDRSaveAs = new JMenuItem("Save As...");
        jmiDRSaveAs.setMnemonic(KeyEvent.VK_A);
        jmiDRSaveAs.setEnabled(false);
        jmiDRExit = new JMenuItem("Exit");
        jmiDRExit.setMnemonic(KeyEvent.VK_X);
        jmDRFile.add(jmiDROpenFile);
        jmDRFile.add(jmiDRSave);
        jmDRFile.add(jmiDRSaveAs);
        jmDRFile.add(jmiDRExit);
        JMenu jmDROptions = new JMenu("Options");
        jmDROptions.setMnemonic(KeyEvent.VK_O);
        jmbDRMenuBar.add(jmDROptions);
        jmiDROptionsOutputLocation = new JMenuItem("Set Output File Definition Location");
        jmiDROptionsOutputLocation.setMnemonic(KeyEvent.VK_S);
        jmiDROptionsShowProducts = new JMenuItem("Show Database Products Available");
        jmiDROptionsShowProducts.setMnemonic(KeyEvent.VK_H);
        jmiDROptionsShowProducts.setEnabled(false);
        jmDROptions.add(jmiDROptionsOutputLocation);
        jmDROptions.add(jmiDROptionsShowProducts);

        JMenu jmDRHelp = new JMenu("Help");
        jmDRHelp.setMnemonic(KeyEvent.VK_H);
        jmbDRMenuBar.add(jmDRHelp);
        jmiDRHelpAbout = new JMenuItem("About");
        jmiDRHelpAbout.setMnemonic(KeyEvent.VK_A);
        jmDRHelp.add(jmiDRHelpAbout);

        JPanel jpDRCenter = new JPanel(new GridLayout(2, 2));
        JPanel jpDRCenter1 = new JPanel(new BorderLayout());
        JPanel jpDRCenter2 = new JPanel(new BorderLayout());
        JPanel jpDRCenter3 = new JPanel(new BorderLayout());
        JPanel jpDRCenter4 = new JPanel(new BorderLayout());

        dlmDRTablesRelations = new DefaultListModel<>();
        jlDRTablesRelations = new JList<>(dlmDRTablesRelations);

        dlmDRFieldsTablesRelations = new DefaultListModel<>();
        jlDRFieldsTablesRelations = new JList<>(dlmDRFieldsTablesRelations);

        dlmDRTablesRelatedTo = new DefaultListModel<>();
        jlDRTablesRelatedTo = new JList<>(dlmDRTablesRelatedTo);

        dlmDRFieldsTablesRelatedTo = new DefaultListModel<>();
        jlDRFieldsTablesRelatedTo = new JList<>(dlmDRFieldsTablesRelatedTo);

        JScrollPane jspDRTablesRelations = new JScrollPane(jlDRTablesRelations);
        JScrollPane jspDRFieldsTablesRelations = new JScrollPane(jlDRFieldsTablesRelations);
        JScrollPane jspDRTablesRelatedTo = new JScrollPane(jlDRTablesRelatedTo);
        JScrollPane jspDRFieldsTablesRelatedTo = new JScrollPane(jlDRFieldsTablesRelatedTo);
        JLabel jlabDRTablesRelations = new JLabel("Tables With Relations", SwingConstants.CENTER);
        JLabel jlabDRFieldsTablesRelations = new JLabel("Fields in Tables with Relations", SwingConstants.CENTER);
        JLabel jlabDRTablesRelatedTo = new JLabel("Related Tables", SwingConstants.CENTER);
        JLabel jlabDRFieldsTablesRelatedTo = new JLabel("Fields in Related Tables", SwingConstants.CENTER);
        jpDRCenter1.add(jlabDRTablesRelations, BorderLayout.NORTH);
        jpDRCenter2.add(jlabDRFieldsTablesRelations, BorderLayout.NORTH);
        jpDRCenter3.add(jlabDRTablesRelatedTo, BorderLayout.NORTH);
        jpDRCenter4.add(jlabDRFieldsTablesRelatedTo, BorderLayout.NORTH);
        jpDRCenter1.add(jspDRTablesRelations, BorderLayout.CENTER);
        jpDRCenter2.add(jspDRFieldsTablesRelations, BorderLayout.CENTER);
        jpDRCenter3.add(jspDRTablesRelatedTo, BorderLayout.CENTER);
        jpDRCenter4.add(jspDRFieldsTablesRelatedTo, BorderLayout.CENTER);
        jpDRCenter.add(jpDRCenter1);
        jpDRCenter.add(jpDRCenter2);
        jpDRCenter.add(jpDRCenter3);
        jpDRCenter.add(jpDRCenter4);
        jfDR.getContentPane().add(jpDRCenter, BorderLayout.CENTER);
        JPanel jpDRBottom = new JPanel(new GridLayout(1, 3));

        jbDRDefineTables = new JButton(DEFINE_TABLES);

        jbDRBindRelation = new JButton("Bind/Unbind Relation");
        jbDRBindRelation.setEnabled(false);

        jbDRCreateDDL = new JButton(CREATE_MESSAGE);
        jbDRCreateDDL.setEnabled(false);

        jpDRBottom.add(jbDRDefineTables);
        jpDRBottom.add(jbDRBindRelation);
        jpDRBottom.add(jbDRCreateDDL);
        jfDR.getContentPane().add(jpDRBottom, BorderLayout.SOUTH);
    } // createDRScreen

    public static void setReadSuccess(boolean value) {

        readSuccess = value;

    }

    public boolean getReadSuccess() {

        return readSuccess;
    }

    private void setCurrentDTTable(String selText) {
        for (int tIndex = 0; tIndex < tables.length; tIndex++) {
            if (selText.equals(tables[tIndex].getName())) {
                currentDTTable = tables[tIndex];
                return;
            }
        }
    }

    private void setCurrentDTField(String selText) {
        for (int fIndex = 0; fIndex < fields.length; fIndex++) {
            if (selText.equals(fields[fIndex].getName())
                    && fields[fIndex].getTableID() == currentDTTable.getNumFigure()) {
                currentDTField = fields[fIndex];
                return;
            }
        }
    }

    private void setCurrentDRTable1(String selText) {
        for (int tIndex = 0; tIndex < tables.length; tIndex++) {
            if (selText.equals(tables[tIndex].getName())) {
                currentDRTable1 = tables[tIndex];
                return;
            }
        }
    }

    private void setCurrentDRTable2(String selText) {
        for (int tIndex = 0; tIndex < tables.length; tIndex++) {
            if (selText.equals(tables[tIndex].getName())) {
                currentDRTable2 = tables[tIndex];
                return;
            }
        }
    }

    private void setCurrentDRField1(String selText) {
        for (int fIndex = 0; fIndex < fields.length; fIndex++) {
            if (selText.equals(fields[fIndex].getName())
                    && fields[fIndex].getTableID() == currentDRTable1.getNumFigure()) {
                currentDRField1 = fields[fIndex];
                return;
            }
        }
    }

    private void setCurrentDRField2(String selText) {
        for (int fIndex = 0; fIndex < fields.length; fIndex++) {
            if (selText.equals(fields[fIndex].getName())
                    && fields[fIndex].getTableID() == currentDRTable2.getNumFigure()) {
                currentDRField2 = fields[fIndex];
                return;
            }
        }
    }

    private String getTableName(int numFigure) {
        for (int tIndex = 0; tIndex < tables.length; tIndex++) {
            if (tables[tIndex].getNumFigure() == numFigure) {
                return tables[tIndex].getName();
            }
        }
        return "";
    }

    private String getFieldName(int numFigure) {
        for (int fIndex = 0; fIndex < fields.length; fIndex++) {
            if (fields[fIndex].getNumFigure() == numFigure) {
                return fields[fIndex].getName();
            }
        }
        return "";
    }

    private void enableControls() {
        for (int i = 0; i < strDataType.length; i++) {
            jrbDataType[i].setEnabled(true);
        }
        jcheckDTPrimaryKey.setEnabled(true);
        jcheckDTDisallowNull.setEnabled(true);
        jbDTVarchar.setEnabled(true);
        jbDTDefaultValue.setEnabled(true);
    }

    private void disableControls() {
        for (int i = 0; i < strDataType.length; i++) {
            jrbDataType[i].setEnabled(false);
        }
        jcheckDTPrimaryKey.setEnabled(false);
        jcheckDTDisallowNull.setEnabled(false);
        jbDTDefaultValue.setEnabled(false);
        jtfDTVarchar.setText("");
        jtfDTDefaultValue.setText("");
    }

    private void clearDTControls() {
        jlDTTablesAll.clearSelection();
        jlDTFieldsTablesAll.clearSelection();
    }

    private void clearDRControls() {
        jlDRTablesRelations.clearSelection();
        jlDRTablesRelatedTo.clearSelection();
        jlDRFieldsTablesRelations.clearSelection();
        jlDRFieldsTablesRelatedTo.clearSelection();
    }

    private void depopulateLists() {
        dlmDTTablesAll.clear();
        dlmDTFieldsTablesAll.clear();
        dlmDRTablesRelations.clear();
        dlmDRFieldsTablesRelations.clear();
        dlmDRTablesRelatedTo.clear();
        dlmDRFieldsTablesRelatedTo.clear();
    }

    private void populateLists() {
        if (readSuccess) {
            jfDT.setVisible(true);
            jfDR.setVisible(false);
            disableControls();
            depopulateLists();
            for (int tIndex = 0; tIndex < tables.length; tIndex++) {
                String tempName = tables[tIndex].getName();
                dlmDTTablesAll.addElement(tempName);
                int[] relatedTables = tables[tIndex].getRelatedTablesArray();
                if (relatedTables.length > 0) {
                    dlmDRTablesRelations.addElement(tempName);
                }
            }
        }
        setReadSuccess(true);
    }

    private void saveAs() {
        int returnVal;
        jfc.addChoosableFileFilter(eff);
        returnVal = jfc.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            saveFile = jfc.getSelectedFile();
            if (saveFile.exists()) {
                int response = JOptionPane.showConfirmDialog(null, "Overwrite existing file?", "Confirm Overwrite",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }
            if (!saveFile.getName().endsWith("sav")) {

                String cleanFile = (saveFile.getAbsolutePath().substring(0, saveFile.getAbsolutePath().indexOf(".")));

                String temp = cleanFile + ".sav";

                saveFile = new File(temp);
            }
            jmiDTSave.setEnabled(true);
            truncatedFilename = saveFile.getName().substring(saveFile.getName().lastIndexOf(File.separator) + 1);

            jfDT.setTitle(DEFINE_TABLES + " - " + truncatedFilename);
            jfDR.setTitle(DEFINE_RELATIONS + " - " + truncatedFilename);
        } else {
            return;
        }
        writeSave();
    }

    private void writeSave() {
        if (saveFile != null) {
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(saveFile, false)));
                // write the identification line
                pw.println(ConvertFileParser.SAVE_ID);
                // write the tables
                pw.println("#Tables#");
                for (int i = 0; i < tables.length; i++) {
                    pw.println(tables[i]);
                }
                // write the fields
                pw.println("#Fields#");
                for (int i = 0; i < fields.length; i++) {
                    pw.println(fields[i]);
                }
                // close the file
                pw.close();
            } catch (IOException ioe) {
                logger.error(ioe);
            }
            dataSaved = true;
        }
    }

    private void setOutputDir() {
        alSubclasses = new ArrayList<>();
        alProductNames = new ArrayList<>();
        outputDir = new File("./resources");
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }
        getOutputClasses();

        if (alProductNames.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "The path:\n" + outputDir + "\ncontains no valid output definition files.");
            return;
        }

        if ((parseFile != null || saveFile != null) && outputDir != null) {
            jbDTCreateDDL.setEnabled(true);
            jbDRCreateDDL.setEnabled(true);
        }

        JOptionPane.showMessageDialog(null,
                "The available products to create DDL statements are:\n" + displayProductNames());
        jmiDTOptionsShowProducts.setEnabled(true);
        jmiDROptionsShowProducts.setEnabled(true);
    }

    private String displayProductNames() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < productNames.length; i++) {
            sb.append(productNames[i] + "\n");
        }
        return sb.toString();
    }

    private void getOutputClasses() {
        File[] resultFiles;
        Class<Object> resultClass = null;
        Class[] paramTypes = {Table[].class, Field[].class};
        Constructor<Object> conResultClass;
        Object[] args = {tables, fields};
        Object objOutput = null;
        String superclassName;
        if (outputClassDir == null) {
            outputClassDir = new File("./build/classes/generator");
        }
        resultFiles = outputClassDir.listFiles();
        alProductNames.clear();
        alSubclasses.clear();
        try {
            for (int i = 0; i < resultFiles.length; i++) {
                if (!resultFiles[i].getName().endsWith(".class")) {
                    continue; // ignore all files that are not .class files
                }
                resultClass = (Class<Object>) Class
                        .forName("generator." + resultFiles[i].getName().substring(0, resultFiles[i].getName().lastIndexOf(".")));
                superclassName = resultClass.getSuperclass().getName();
                if (superclassName.equals("generator.CreateDDL")) { // only interested in classes that extend ConvertCreateDDL
                    if (!(parseFile == null && saveFile == null)) {
                        conResultClass = resultClass.getConstructor(paramTypes);
                        objOutput = conResultClass.newInstance(args);
                    }
                    alSubclasses.add(objOutput);
                    Method getProductName = resultClass.getMethod("getProductName", null);
                    String productName = (String) getProductName.invoke(objOutput, null);
                    alProductNames.add(productName);
                }
            }
        } catch (InstantiationException ie) {
            logger.error("Error Instantiation Exception", ie);
        } catch (ClassNotFoundException cnfe) {
            logger.error("Error Class Not Found Exception", cnfe);
        } catch (IllegalAccessException iae) {
            logger.error("Error Illegal Access Exception Exception", iae);
        } catch (NoSuchMethodException nsme) {
            logger.error("Error NoSuchMethodException", nsme);
        } catch (InvocationTargetException ite) {
            logger.error("Error InvocationTargetException", ite);
        }
        if (!alProductNames.isEmpty() && !alSubclasses.isEmpty()) { // do not recreate productName and objSubClasses
            // arrays if the new path is empty of valid files
            productNames = alProductNames.toArray(new String[alProductNames.size()]);
            objSubclasses = alSubclasses.toArray(new Object[alSubclasses.size()]);
        }
    }

    private String getDBStatements() {
        String strDBString = "";
        String response = (String) JOptionPane.showInputDialog(
                null,
                "Select a product:",
                CREATE_MESSAGE,
                JOptionPane.PLAIN_MESSAGE,
                null,
                productNames,
                null);

        if (response == null) {
            return ConvertGUI.CANCELLED;
        }

        int selected;
        for (selected = 0; selected < productNames.length; selected++) {
            if (response.equals(productNames[selected])) {
                selectedFileType = productNames[selected];
                break;
            }
        }
        try {
            Class<Object> selectedSubclass;
            selectedSubclass = (Class<Object>) objSubclasses[selected].getClass();
            Method getSQLString = selectedSubclass.getMethod("getSQLString", null);
            Method getDatabaseName = selectedSubclass.getMethod("getDatabaseName", null);
            strDBString = (String) getSQLString.invoke(objSubclasses[selected], null);
            databaseName = (String) getDatabaseName.invoke(objSubclasses[selected], null);
        } catch (IllegalAccessException iae) {
            logger.error(iae);
        } catch (NoSuchMethodException nsme) {
            logger.error(nsme);
        } catch (InvocationTargetException ite) {
            logger.error(ite);

        }

        return strDBString;
    }

    private void write(String output, String selectedFileType) {
        String extension = "";
        File outputFile;
        if (selectedFileType.equals("Oracle")) {
            extension = ".dbs";
        } else if (selectedFileType.equals("MySQL")) {
            extension = ".sql";
        }

        jfc.resetChoosableFileFilters();
        if (parseFile != null) {
            outputFile = new File(parseFile.getAbsolutePath().substring(0,
                    (parseFile.getAbsolutePath().lastIndexOf(File.separator) + 1)) + databaseName + extension);
        } else {
            outputFile = new File(saveFile.getAbsolutePath().substring(0,
                    (saveFile.getAbsolutePath().lastIndexOf(File.separator) + 1)) + databaseName + extension);
        }
        if (databaseName.equals("")) {
            return;
        }
        jfc.setSelectedFile(outputFile);
        int returnVal = jfc.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            outputFile = jfc.getSelectedFile();
            if (outputFile.exists()) {
                int response = JOptionPane.showConfirmDialog(null, "Overwrite existing file?", "Confirm Overwrite",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(outputFile, false)));
                // write the statements
                pw.println(output);
                // close the file
                pw.close();
            } catch (IOException ioe) {
                logger.error(ioe);
            }
        }

    }

    public void showDefineRelationsScreen() {

        jfDT.setVisible(false);
        jfDR.setVisible(true); // show the Define Relations screen
        clearDTControls();
        dlmDTFieldsTablesAll.removeAllElements();

    }

    public void changeCurrentDTTable(ActionEvent ae) {

        Object buttonName = ae.getSource();
        int i = 0;

        int selection = jlDTFieldsTablesAll.getSelectedIndex();
        if (buttonName == "") {
            currentDTTable.moveFieldUp(selection);
            i = -1;
        } else {
            currentDTTable.moveFieldDown(selection);
            i = 1;
        }
        // repopulate Fields List
        int[] currentNativeFields = currentDTTable.getNativeFieldsArray();
        jlDTFieldsTablesAll.clearSelection();
        dlmDTFieldsTablesAll.removeAllElements();
        for (int fIndex = 0; fIndex < currentNativeFields.length; fIndex++) {
            dlmDTFieldsTablesAll.addElement(getFieldName(currentNativeFields[fIndex]));
        }
        jlDTFieldsTablesAll.setSelectedIndex(selection + i);
        dataSaved = false;

    }

    public void addDefaultValues() {

        String prev = jtfDTDefaultValue.getText();
        boolean goodData = false;
        int i = currentDTField.getDataType();
        do {
            String result = (String) JOptionPane.showInputDialog(
                    null,
                    "Enter the default value:",
                    "Default Value",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    prev);

            if ((result == null)) {
                jtfDTDefaultValue.setText(prev);
                return;
            }
            switch (i) {
                case 0: // varchar
                    if (result.length() <= Integer.parseInt(jtfDTVarchar.getText())) {
                        jtfDTDefaultValue.setText(result);
                        goodData = true;
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "The length of this value must be less than or equal to the Varchar length specified.");
                        logger.warn(
                                "The length of this value is not less than or equal to the Varchar length specified");
                    }
                    break;
                case 1: // boolean
                    String newResult = result.toLowerCase();
                    if (newResult.equals("true") || newResult.equals("false")) {
                        jtfDTDefaultValue.setText(newResult);
                        goodData = true;
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "You must input a valid boolean value (\"true\" or \"false\").");
                    }
                    break;
                case 2: // Integer
                    try {
                    jtfDTDefaultValue.setText(result);
                    goodData = true;
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "\"" + result
                            + "\" is not an integer or is outside the bounds of valid integer values.");
                }
                break;
                case 3: // Double
                    try {
                    jtfDTDefaultValue.setText(result);
                    goodData = true;
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null,
                            "\"" + result + "\" is not a double or is outside the bounds of valid double values.");
                }
                break;
                case 4: // Timestamp
                    try {
                    jtfDTDefaultValue.setText(result);
                    goodData = true;
                } catch (Exception e) {
                    logger.error(e);
                }
                break;
                default:
                    break;
            }
        } while (!goodData);
        int selIndex = jlDTFieldsTablesAll.getSelectedIndex();
        if (selIndex >= 0) {
            String selText = dlmDTFieldsTablesAll.getElementAt(selIndex).toString();
            setCurrentDTField(selText);
            currentDTField.setDefaultValue(jtfDTDefaultValue.getText());
        }
        dataSaved = false;

    }

    public void startInputFilter() {

        String prev = jtfDTVarchar.getText();
        String result = (String) JOptionPane.showInputDialog(
                null,
                "Enter the varchar length:",
                "Varchar Length",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                prev);
        if ((result == null)) {
            jtfDTVarchar.setText(prev);
            return;
        }
        int varchar;
        try {
            if (result.length() > 5) {
                JOptionPane.showMessageDialog(null,
                        "Varchar length must be greater than 0 and less than or equal to 65535.");
                jtfDTVarchar.setText(Integer.toString(Field.VARCHAR_DEFAULT_LENGTH));
                logger.warn("Varchar must be greater than 0 and less or equal to 65535");
                return;
            }
            varchar = Integer.parseInt(result);
            if (varchar > 0 && varchar <= 65535) { // max length of varchar is 255 before v5.0.3
                jtfDTVarchar.setText(Integer.toString(varchar));
                currentDTField.setVarcharValue(varchar);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Varchar length must be greater than 0 and less than or equal to 65535.");
                jtfDTVarchar.setText(Integer.toString(Field.VARCHAR_DEFAULT_LENGTH));
                logger.warn("Varchar must be greater than 0 and less or equal to 65535");
                return;
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "\"" + result + "\" is not a number");
            jtfDTVarchar.setText(Integer.toString(Field.VARCHAR_DEFAULT_LENGTH));
            logger.error(String.format("\"%s\" is not a number%n", result), nfe);
            return;

        }
        dataSaved = false;

    }

    public void defineTables() {

        jfDT.setVisible(true); // show the Define Tables screen
        jfDR.setVisible(false);
        clearDRControls();
        depopulateLists();
        populateLists();

    }

    public void bindRelations() {

        int nativeIndex = jlDRFieldsTablesRelations.getSelectedIndex();
        int relatedField = currentDRField2.getNumFigure();
        if (currentDRField1.getFieldBound() == relatedField) { // the selected fields are already bound to each other
            int answer = JOptionPane.showConfirmDialog(null, "Do you wish to unbind the relation on field "
                    + currentDRField1.getName() + "?",
                    CERTAINTY_INQUIRY, JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                currentDRTable1.setRelatedField(nativeIndex, 0); // clear the related field
                currentDRField1.setTableBound(0); // clear the bound table
                currentDRField1.setFieldBound(0); // clear the bound field
                jlDRFieldsTablesRelatedTo.clearSelection(); // clear the listbox selection
            }
            return;
        }
        if (currentDRField1.getFieldBound() != 0) { // field is already bound to a different field
            int answer = JOptionPane.showConfirmDialog(null, "There is already a relation defined on field "
                    + currentDRField1.getName() + ", do you wish to overwrite it?",
                    CERTAINTY_INQUIRY, JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.NO_OPTION || answer == JOptionPane.CLOSED_OPTION) {
                jlDRTablesRelatedTo.setSelectedValue(getTableName(currentDRField1.getTableBound()), true); // revert
                // selections
                // to saved
                // settings
                jlDRFieldsTablesRelatedTo.setSelectedValue(getFieldName(currentDRField1.getFieldBound()), true); // revert
                // selections
                // to
                // saved
                // settings
                return;
            }
        }
        if (currentDRField1.getDataType() != currentDRField2.getDataType()) {
            JOptionPane.showMessageDialog(null, "The datatypes of " + currentDRTable1.getName() + "."
                    + currentDRField1.getName() + " and " + currentDRTable2.getName()
                    + "." + currentDRField2.getName() + " do not match.  Unable to bind this relation.");
            return;
        }
        if ((currentDRField1.getDataType() == 0) && (currentDRField2.getDataType() == 0) && currentDRField1.getVarcharValue() != currentDRField2.getVarcharValue()) {
            JOptionPane.showMessageDialog(null, "The varchar lengths of " + currentDRTable1.getName() + "."
                    + currentDRField1.getName() + " and " + currentDRTable2.getName()
                    + "." + currentDRField2.getName() + " do not match.  Unable to bind this relation.");
            return;
        }
        currentDRTable1.setRelatedField(nativeIndex, relatedField);
        currentDRField1.setTableBound(currentDRTable2.getNumFigure());
        currentDRField1.setFieldBound(currentDRField2.getNumFigure());
        JOptionPane.showMessageDialog(null, "Table " + currentDRTable1.getName() + ": native field "
                + currentDRField1.getName() + " bound to table " + currentDRTable2.getName()
                + " on field " + currentDRField2.getName());
        dataSaved = false;

    }

    public void ddlBtnActionPerformed() {

        while (outputDir == null) {
            setOutputDir();
        }
        getOutputClasses(); // in case outputDir was set before a file was loaded and Table/Field
        // objects created
        String dbString = getDBStatements();
        if (dbString.equals(ConvertGUI.CANCELLED)) {
            return;
        }
        write(dbString, selectedFileType);
    }

    public void tableValueChanged() {
        int selIndex = jlDTTablesAll.getSelectedIndex();
        if (selIndex >= 0) {
            String selText = dlmDTTablesAll.getElementAt(selIndex).toString();
            setCurrentDTTable(selText); // set pointer to the selected table
            int[] currentNativeFields = currentDTTable.getNativeFieldsArray();
            jlDTFieldsTablesAll.clearSelection();
            dlmDTFieldsTablesAll.removeAllElements();
            jbDTMoveUp.setEnabled(false);
            jbDTMoveDown.setEnabled(false);
            for (int fIndex = 0; fIndex < currentNativeFields.length; fIndex++) {
                dlmDTFieldsTablesAll.addElement(getFieldName(currentNativeFields[fIndex]));
            }
        }
        disableControls();
    }

    public void fieldTableValueChanged() {
        int selIndex = jlDTFieldsTablesAll.getSelectedIndex();
        boolean enabled;
        if (selIndex >= 0) {
            if (selIndex == 0) {
                enabled = false;
            } else {
                enabled = true;
            }
            jbDTMoveUp.setEnabled(enabled);
            if (selIndex == (dlmDTFieldsTablesAll.getSize() - 1)) {
                enabled = false;
            } else {
                enabled = true;
            }
            jbDTMoveDown.setEnabled(enabled);
            String selText = dlmDTFieldsTablesAll.getElementAt(selIndex).toString();
            setCurrentDTField(selText); // set pointer to the selected field
            enableControls();
            jrbDataType[currentDTField.getDataType()].setSelected(true); // select the appropriate radio
            // button, based on value of
            // dataType
            if (jrbDataType[0].isSelected()) { // this is the Varchar radio button
                jbDTVarchar.setEnabled(true); // enable the Varchar button
                jtfDTVarchar.setText(Integer.toString(currentDTField.getVarcharValue())); // fill text
                // field with
                // varcharValue
            } else { // some radio button other than Varchar is selected
                jtfDTVarchar.setText(""); // clear the text field
                jbDTVarchar.setEnabled(false); // disable the button
            }
            jcheckDTPrimaryKey.setSelected(currentDTField.getIsPrimaryKey()); // clear or set Primary
            // Key checkbox
            jcheckDTDisallowNull.setSelected(currentDTField.getDisallowNull()); // clear or set Disallow
            // Null checkbox
            jtfDTDefaultValue.setText(currentDTField.getDefaultValue()); // fill text field with
            // defaultValue
        }
    }

    public void jcheckDTDisallowNullItemStateChanged() {
        currentDTField.setDisallowNull(jcheckDTDisallowNull.isSelected());
        dataSaved = false;
    }

    public void jcheckDTPrimaryKeyItemStateChanged() {
        currentDTField.setIsPrimaryKey(jcheckDTPrimaryKey.isSelected());
        dataSaved = false;
    }

    public void tablesRelationsValueChanged() {
        int selIndex = jlDRTablesRelations.getSelectedIndex();
        if (selIndex >= 0) {
            String selText = dlmDRTablesRelations.getElementAt(selIndex).toString();
            setCurrentDRTable1(selText);
            int[] currentNativeFields;
            int[] currentRelatedTables;
            currentNativeFields = currentDRTable1.getNativeFieldsArray();
            currentRelatedTables = currentDRTable1.getRelatedTablesArray();
            jlDRFieldsTablesRelations.clearSelection();
            jlDRTablesRelatedTo.clearSelection();
            jlDRFieldsTablesRelatedTo.clearSelection();
            dlmDRFieldsTablesRelations.removeAllElements();
            dlmDRTablesRelatedTo.removeAllElements();
            dlmDRFieldsTablesRelatedTo.removeAllElements();
            for (int fIndex = 0; fIndex < currentNativeFields.length; fIndex++) {
                dlmDRFieldsTablesRelations.addElement(getFieldName(currentNativeFields[fIndex]));
            }
            for (int rIndex = 0; rIndex < currentRelatedTables.length; rIndex++) {
                dlmDRTablesRelatedTo.addElement(getTableName(currentRelatedTables[rIndex]));
            }
        }
    }

    public void fieldsTablesRelationsValueChanged() {
        int selIndex = jlDRFieldsTablesRelations.getSelectedIndex();
        if (selIndex >= 0) {
            String selText = dlmDRFieldsTablesRelations.getElementAt(selIndex).toString();
            setCurrentDRField1(selText);
            if (currentDRField1.getFieldBound() == 0) {
                jlDRTablesRelatedTo.clearSelection();
                jlDRFieldsTablesRelatedTo.clearSelection();
                dlmDRFieldsTablesRelatedTo.removeAllElements();
            } else {
                jlDRTablesRelatedTo.setSelectedValue(getTableName(currentDRField1.getTableBound()),
                        true);
                jlDRFieldsTablesRelatedTo
                        .setSelectedValue(getFieldName(currentDRField1.getFieldBound()), true);
            }
        }
    }

    public void jlDRTablesRelatedToValueChanged() {
        int selIndex = jlDRTablesRelatedTo.getSelectedIndex();
        if (selIndex >= 0) {
            String selText = dlmDRTablesRelatedTo.getElementAt(selIndex).toString();
            setCurrentDRTable2(selText);
            int[] currentNativeFields = currentDRTable2.getNativeFieldsArray();
            dlmDRFieldsTablesRelatedTo.removeAllElements();
            for (int fIndex = 0; fIndex < currentNativeFields.length; fIndex++) {
                dlmDRFieldsTablesRelatedTo.addElement(getFieldName(currentNativeFields[fIndex]));
            }
        }
    }

    public void jlDRFieldsTablesRelatedToValueChanged() {
        int selIndex = jlDRFieldsTablesRelatedTo.getSelectedIndex();
        if (selIndex >= 0) {
            String selText = dlmDRFieldsTablesRelatedTo.getElementAt(selIndex).toString();
            setCurrentDRField2(selText);
            jbDRBindRelation.setEnabled(true);
        } else {
            jbDRBindRelation.setEnabled(false);
        }
    }

    public void addJbDTDefineRelationsListener(ActionListener jbDTDefineRelationsListener) {

        jbDTDefineRelations.addActionListener(jbDTDefineRelationsListener);

    }

    public void addJbDTMoveButtonListeners(ActionListener jbDTMoveButtonListeners) {

        jbDTMoveUp.addActionListener(jbDTMoveButtonListeners);
        jbDTMoveDown.addActionListener(jbDTMoveButtonListeners);

    }

    public void addJbDTDefaultValueListener(ActionListener jbDTDefaultValueListener) {

        jbDTDefaultValue.addActionListener(jbDTDefaultValueListener);

    }

    public void addJbDTVarcharListener(ActionListener jbDTVarcharListener) {

        jbDTVarchar.addActionListener(jbDTVarcharListener);

    }

    public void addJbDRDefineTablesListener(ActionListener jbDRDefineTablesListener) {

        jbDRDefineTables.addActionListener(jbDRDefineTablesListener);

    }

    public void addJbDRBindRelationListener(ActionListener jbDRBindRelationListener) {

        jbDRBindRelation.addActionListener(jbDRBindRelationListener);

    }

    public void addCreateDDLButtonListener(ActionListener createDDLListener) {

        jbDTCreateDDL.addActionListener(createDDLListener);
        jbDRCreateDDL.addActionListener(createDDLListener);

    }

    public void addTableListSelectionListener(ListSelectionListener tableListSelectionListener) {

        jlDTTablesAll.addListSelectionListener(tableListSelectionListener);

    }

    public void addFieldTableListSelectionListener(ListSelectionListener fieldTableListSelectionListener) {

        jlDTFieldsTablesAll.addListSelectionListener(fieldTableListSelectionListener);

    }

    public void addJcheckDTDisallowNullItemListener(ItemListener jcheckDTDisallowNullItemListener) {

        jcheckDTDisallowNull.addItemListener(jcheckDTDisallowNullItemListener);

    }

    public void addJcheckDTPrimaryKeyItemListener(ItemListener jcheckDTPrimaryKeyItemListener) {

        jcheckDTPrimaryKey.addItemListener(jcheckDTPrimaryKeyItemListener);

    }

    public void addJlDRTablesRelationsListSelectionListener(ListSelectionListener jlDRTablesRelationsListSelectionListener) {

        jlDRTablesRelations.addListSelectionListener(jlDRTablesRelationsListSelectionListener);

    }

    public void addJlDRFieldsTablesRelationsListSelectionListener(ListSelectionListener jlDRFieldsTablesRelationsListSelectionListener) {

        jlDRFieldsTablesRelations.addListSelectionListener(jlDRFieldsTablesRelationsListSelectionListener);

    }

    public void addJlDRTablesRelatedToListSelectionListener(ListSelectionListener jlDRTablesRelatedToListSelectionListener) {

        jlDRTablesRelatedTo.addListSelectionListener(jlDRTablesRelatedToListSelectionListener);

    }

    public void addJlDRFieldsTablesRelatedToListSelectionListener(ListSelectionListener jlDRFieldsTablesRelatedToListSelectionListener) {

        jlDRFieldsTablesRelatedTo.addListSelectionListener(jlDRFieldsTablesRelatedToListSelectionListener);

    }

    public void addMenuListener(ActionListener menuListener) {

        jmiDTOpenFile.addActionListener(menuListener);
        jmiDTSave.addActionListener(menuListener);
        jmiDTSaveAs.addActionListener(menuListener);
        jmiDTExit.addActionListener(menuListener);
        jmiDTOptionsShowProducts.addActionListener(menuListener);
        jmiDTHelpAbout.addActionListener(menuListener);
        jmiDROpenFile.addActionListener(menuListener);
        jmiDRSave.addActionListener(menuListener);
        jmiDRSaveAs.addActionListener(menuListener);
        jmiDRExit.addActionListener(menuListener);
        jmiDROptionsOutputLocation.addActionListener(menuListener);
        jmiDROptionsShowProducts.addActionListener(menuListener);
        jmiDRHelpAbout.addActionListener(menuListener);

    }

    public void menuActionPerformed(ActionEvent ae) {

        int returnVal;
        FileParser fp;
        if ((ae.getSource() == jmiDTOpenFile) || (ae.getSource() == jmiDROpenFile)) {
            if (!dataSaved) {
                int answer = JOptionPane.showConfirmDialog(null, "You currently have unsaved data. Continue?",
                        CERTAINTY_INQUIRY, JOptionPane.YES_NO_OPTION);
                if (answer != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            jfc.addChoosableFileFilter(eff);
            returnVal = jfc.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                parseFile = jfc.getSelectedFile();
                String extension = "";
                int a = parseFile.getName().lastIndexOf('.');
                if (a > 0) {
                    extension = parseFile.getName().substring(a + 1);
                }
                if (extension.equalsIgnoreCase("edg")) {
                    fp = new ConvertFileParser(parseFile);
                } else if (extension.equalsIgnoreCase("xml")) {
                    return;
                } else if (extension.equalsIgnoreCase("sav")) {
                    fp = new SaveFileParser(parseFile);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "You have entered wrong kind of file unfortunately. The system will not load it.");
                    return;
                }
                tables = fp.getTables();
                for (int i = 0; i < tables.length; i++) {
                    tables[i].makeArrays();
                }
                fields = fp.getFields();
                populateLists();
                saveFile = null;
                jmiDTSave.setEnabled(false);
                jmiDRSave.setEnabled(false);
                jmiDTSaveAs.setEnabled(true);
                jmiDRSaveAs.setEnabled(true);
                jbDTDefineRelations.setEnabled(true);

                jbDTCreateDDL.setEnabled(true);
                jbDRCreateDDL.setEnabled(true);

                truncatedFilename = parseFile.getName().substring(parseFile.getName().lastIndexOf(File.separator) + 1);
                jfDT.setTitle(DEFINE_TABLES + " - " + truncatedFilename);
                jfDR.setTitle(DEFINE_RELATIONS + " - " + truncatedFilename);
            } else {
                return;
            }
            dataSaved = true;
        }

        if ((ae.getSource() == jmiDTOptionsShowProducts) || (ae.getSource() == jmiDROptionsShowProducts)) {
            JOptionPane.showMessageDialog(null,
                    "The available products to create DDL statements are:\n" + displayProductNames());
        }

        if ((ae.getSource() == jmiDTHelpAbout) || (ae.getSource() == jmiDRHelpAbout)) {
            JOptionPane.showMessageDialog(null, "EdgeConvert ERD To DDL Conversion Tool\n"
                    + "by Stephen A. Capperell\n"
                    + "© 2007-2008");
        }

        if ((ae.getSource() == jmiDTExit) || (ae.getSource() == jmiDRExit)) {
            if (!dataSaved) {
                int answer = JOptionPane.showOptionDialog(null,
                        "You currently have unsaved data. Would you like to save?",
                        CERTAINTY_INQUIRY,
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, null, null);
                if (answer == JOptionPane.YES_OPTION && saveFile == null) {
                    saveAs();
                }
                if ((answer == JOptionPane.CANCEL_OPTION) || (answer == JOptionPane.CLOSED_OPTION)) {
                    return;
                }
            }
            System.exit(0); //No was selected
        }

        if ((ae.getSource() == jmiDTSaveAs) || (ae.getSource() == jmiDRSaveAs)
                || (ae.getSource() == jmiDTSave) || (ae.getSource() == jmiDRSave)) {
            if ((ae.getSource() == jmiDTSaveAs) || (ae.getSource() == jmiDRSaveAs)) {
                saveAs();
            } else {
                writeSave();
            }
        }

    }

    public void addRadioButtonListener(ActionListener radioButtonListener) {

        for (int i = 0; i < strDataType.length; i++) {
            jrbDataType[i].addActionListener(radioButtonListener);
        }

    }

    public void radioButtonListenerActionPerformed() {

        for (int i = 0; i < jrbDataType.length; i++) {
            if (jrbDataType[i].isSelected()) {
                currentDTField.setDataType(i);
                break;
            }
        }
        if (jrbDataType[0].isSelected()) {
            jtfDTVarchar.setText(Integer.toString(Field.VARCHAR_DEFAULT_LENGTH));
            jbDTVarchar.setEnabled(true);
        } else {
            jtfDTVarchar.setText("");
            jbDTVarchar.setEnabled(false);
        }
        jtfDTDefaultValue.setText("");
        currentDTField.setDefaultValue("");
        dataSaved = false;

    }

    public void addWindowListener(WindowListener windowListener) {

        jfDR.addWindowListener(windowListener);
        jfDT.addWindowListener(windowListener);

    }

    public void windowClosing(WindowEvent we) {

        if (!dataSaved) {
            int answer = JOptionPane.showOptionDialog(null,
                    "You currently have unsaved data. Would you like to save?",
                    CERTAINTY_INQUIRY,
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, null, null);
            if (answer == JOptionPane.YES_OPTION) {
                if (saveFile == null) {
                    saveAs();
                }
                writeSave();
            }
            if ((answer == JOptionPane.CANCEL_OPTION) || (answer == JOptionPane.CLOSED_OPTION)) {
                if (we.getSource() == jfDT) {
                    jfDT.setVisible(true);
                }
                if (we.getSource() == jfDR) {
                    jfDR.setVisible(true);
                }

                return;
            }
        }
        System.exit(0); // No was selected

    }

} // ConvertGUI
