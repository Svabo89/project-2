package model;

import parsers.ConvertFileParser;
import java.util.StringTokenizer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Connector {

    private int numConnector;
    private int endPoint1;
    private int endPoint2;
    private String endStyle1;
    private String endStyle2;
    private boolean isEP1Field;
    private boolean isEP2Field;
    private boolean isEP1Table;
    private boolean isEP2Table;
    private static final Logger logger = LogManager.getLogger();

    public Connector(String inputString) {
        StringTokenizer st = new StringTokenizer(inputString, ConvertFileParser.DELIM);
        numConnector = Integer.parseInt(st.nextToken());
        endPoint1 = Integer.parseInt(st.nextToken());
        endPoint2 = Integer.parseInt(st.nextToken());
        endStyle1 = st.nextToken();
        endStyle2 = st.nextToken();
        isEP1Field = false;
        isEP2Field = false;
        isEP1Table = false;
        isEP2Table = false;
        logger.info("Connector constructor start");
        logger.warn("Closes the constructor");
        logger.info("Connector constructor end");
    }

    public int getNumConnector() {
        logger.info("getNumCOnnector start");
        logger.info("getNumCOnnector end");
        return numConnector;
    }

    public int getEndPoint1() {
        logger.info("getEndPoint1 start");
        logger.info("getEndPoint1 end");
        return endPoint1;
    }

    public int getEndPoint2() {
        logger.info("getEndPoint2 start");
        logger.info("getEndPoint2 end");
        return endPoint2;
    }

    public String getEndStyle1() {
        logger.info("getEndStyle1 start");
        logger.info("getEndStyle1 end");
        return endStyle1;
    }

    public String getEndStyle2() {
        logger.info("getEndStyle2 start");
        logger.info("getEndStyle2 end");
        return endStyle2;
    }

    public boolean getIsEP1Field() {
        logger.info("getIsEP1Field start");
        logger.info("getIsEP1Field end");
        return isEP1Field;
    }

    public boolean getIsEP2Field() {
        logger.info("getIsEP2Field start");
        logger.info("getIsEP2Field end");
        return isEP2Field;
    }

    public boolean getIsEP1Table() {
        logger.info("getIsEP1Table start");
        logger.info("getIsEP1Table end");
        return isEP1Table;
    }

    public boolean getIsEP2Table() {
        logger.info("getIsEP2Table start");
        logger.info("getIsEP2Table end");
        return isEP2Table;
    }

    public void setIsEP1Field(boolean value) {
        logger.info("setIsEP1Field start");
        logger.info("setIsEP1Field end");
        isEP1Field = value;
    }

    public void setIsEP2Field(boolean value) {
        logger.info("setIsEP2Field start");
        logger.info("setIsEP2Field end");
        isEP2Field = value;
    }

    public void setIsEP1Table(boolean value) {
        logger.info("setIsEP1Table start");
        logger.info("setIsEP1Table end");
        isEP1Table = value;
    }

    public void setIsEP2Table(boolean value) {
        logger.info("setIsEP2Table start");
        logger.info("setIsEP2Table end");
        isEP2Table = value;
    }
}
