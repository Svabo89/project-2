package parsers;

import java.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class XMLFileParser extends FileParser {

    private static Logger logger = LogManager.getLogger(XMLFileParser.class);

    private File parseFile;

    public XMLFileParser(File xmlFile) {
        super();
        parseFile = xmlFile;
        this.openFile(parseFile);
    }

    @Override
    public void parseFile() {
        logger.info("XML File is being parsed");
    }

    @Override
    public void openFile(File inputFile) {
        super.openFile(inputFile);
        this.parseFile();
    }
}
