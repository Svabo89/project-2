package edu.rit.edgeconverter.util;

/**
 * Enum representing various data types, each associated with a specific value, index, and default length.
 */
public enum DataType {
    // Enum constants representing different data types
    VARCHAR("Varchar", 0, 1),
    BOOLEAN("Boolean", 1),
    INTEGER("Integer", 2),
    DOUBLE("Double", 3);

    // Fields to store the properties of each data type
    private final String value;
    private final int index;
    private final int defaultLength;

    /**
     * Constructor for data types that don't have a default length.
     *
     * @param value  The string representation of the data type.
     * @param index  The index associated with the data type.
     */
    private DataType(String value, int index) {
        this(value, index, -1); // -1 is for data types without length
    }

    /**
     * Constructor for data types that have a default length.
     *
     * @param value          The string representation of the data type.
     * @param index          The index associated with the data type.
     * @param defaultLength  The default length of the data type.
     */
    private DataType(String value, int index, int defaultLength) {
        this.value = value;
        this.index = index;
        this.defaultLength = defaultLength;
    }

    /**
     * Get the string value of the data type.
     *
     * @return The string value.
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Get the index of the data type.
     *
     * @return The index.
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Get the default length of the data type.
     *
     * @return The default length, or -1 if the data type doesn't have a default length.
     */
    public int getDefaultLength() {
        return this.defaultLength;
    }
}
