package edu.rit.edgeconverter.util;

import javax.swing.filechooser.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

public class Filter extends FileFilter {

  private static final Logger log = LogManager.getLogger("edu.rit");

  private static String TYPE_UNKNOWN = "Type Unknown";
  private static String HIDDEN_FILE = "Hidden File";

  private Hashtable filters = null;
  private String description = null;
  private String fullDescription = null;
  private boolean useExtensionsInDescription = true;

  public Filter() {
    this.filters = new Hashtable();
    log.debug("Filter instance created.");
  }

  public Filter(String extension) {
    this(extension, null);
  }

  public Filter(String extension, String description) {
    this();
    if (extension != null) addExtension(extension);
    if (description != null) setDescription(description);
  }

  public Filter(String[] filters) {
    this(filters, null);
  }

  public Filter(String[] filters, String description) {
    this();
    for (int i = 0; i < filters.length; i++) {
      addExtension(filters[i]);
    }
    if (description != null) setDescription(description);
  }

  public boolean accept(File f) {
    if (f != null) {
      if (f.isDirectory()) {
        log.info("Accepted a directory: " + f.getAbsolutePath());
        return true;
      }
      String extension = getExtension(f);
      if (extension != null && filters.get(getExtension(f)) != null) {
        log.info("Accepted a file: " + f.getName());
        return true;
      }
    }
    log.warn("Rejected file: " + (f != null ? f.getName() : "null"));
    return false;
  }

    /**
   * Get the extension of a file.
   *
   * @param f The file whose extension is to be extracted.
   * @return The file extension, or null if the file has no extension.
   */
  public String getExtension(File f) {
    if (f != null) {
      String filename = f.getName();
      int i = filename.lastIndexOf('.');
      if (i > 0 && i < filename.length() - 1) {
        return filename.substring(i + 1).toLowerCase();
      }
    }
    return null;
  }

  /**
   * Add an extension to the list of file extensions to be filtered.
   *
   * @param extension The file extension to be added to the filter.
   */
  public void addExtension(String extension) {
    if (filters == null) {
      filters = new Hashtable(5);
    }
    filters.put(extension.toLowerCase(), this);
    fullDescription = null;
    log.debug("Added extension: " + extension);
  }

  /**
   * Get the description of the file filter.
   *
   * @return The description of the file filter.
   */
  public String getDescription() {
    if (fullDescription == null) {
      if (description == null || isExtensionListInDescription()) {
        fullDescription = description == null ? "(" : description + " (";
        Enumeration extensions = filters.keys();
        if (extensions != null) {
          fullDescription += "." + (String) extensions.nextElement();
          while (extensions.hasMoreElements()) {
            fullDescription += ", ." + (String) extensions.nextElement();
          }
        }
        fullDescription += ")";
      } else {
        fullDescription = description;
      }
    }
    return fullDescription;
  }

  /**
   * Set whether the extension list should be included in the description of the file filter.
   *
   * @param b true to include the extension list in the description, false to exclude it.
   */
  public void setExtensionListInDescription(boolean b) {
    useExtensionsInDescription = b;
    fullDescription = null;
    log.debug("Including the list of extensions in the description: " + b);
  }
  /**
   * Set the description of the file filter.
   *
   * @param description The description to be set for the file filter.
   */
  public void setDescription(String description) {
    this.description = description;
    fullDescription = null;
    log.debug("Set description: " + description);
  }

  /**
   * Determine whether the extension list is included in the description of the file filter.
   *
   * @return true if the extension list is included in the description, false otherwise.
   */
  public boolean isExtensionListInDescription() {
    log.trace(
      "Verifying if the list of extensions is included in the description: " +
      useExtensionsInDescription
    );
    return useExtensionsInDescription;
  }

}

