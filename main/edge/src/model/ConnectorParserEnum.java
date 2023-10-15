package model;

public enum ConnectorParserEnum {
    EDGE_ID("EDGE Diagram File"),
    SAVE_ID("EdgeConvert Save File"),
    DELIM("|");

    private final String name;

    private ConnectorParserEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
