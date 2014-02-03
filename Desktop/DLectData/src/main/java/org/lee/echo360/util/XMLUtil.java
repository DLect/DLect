/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author lee
 */
public class XMLUtil {

    private static final XPath X_PATH = XPathFactory.newInstance().newXPath();

    public static Document getDocumentFromStream(InputStream stream) throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document parse = builder.parse(stream);
        return parse;
    }

    public static List<Node> convert(NodeList childNodes) {
        if (childNodes == null) {
            return Collections.emptyList();
        }
        List<Node> d = new ArrayList<Node>(childNodes.getLength());
        for (int i = 0; i < childNodes.getLength(); i++) {
            d.add(childNodes.item(i));
        }
        return Collections.unmodifiableList(d);
    }

    public static List<Node> getListByXPath(Node doc, String xpath) {
        try {
            XPathExpression expr = X_PATH.compile(xpath);
            final List<Node> convert = convert((NodeList) expr.evaluate(doc, XPathConstants.NODESET));
            return convert;
        } catch (XPathExpressionException ex) {
            ExceptionReporter.reportException(ex);
            return Collections.EMPTY_LIST;
        }
    }

    public static Node getNodeByXPath(Node node, String xpath) {
        try {
            XPathExpression expr = X_PATH.compile(xpath);
            return (Node) expr.evaluate(node, XPathConstants.NODE);
        } catch (XPathExpressionException ex) {
            ExceptionReporter.reportException(ex);
            return null;
        }
    }

    public static String getTextByXPath(Node node, String xpath) {
        try {
            XPathExpression expr = X_PATH.compile(xpath);
            return (String) expr.evaluate(node, XPathConstants.STRING);
        } catch (XPathExpressionException ex) {
            ExceptionReporter.reportException(ex);
            return null;
        }
    }

    public static double getDoubleByXPath(Node node, String xpath) {
        try {
            return (Double) X_PATH.evaluate(xpath, node, XPathConstants.NUMBER);
        } catch (XPathExpressionException ex) {
            ExceptionReporter.reportException(ex);
            return Double.NaN;
        }
    }

    public static long getLongByXPath(Node node, String xpath) {
        return Math.round(getDoubleByXPath(node, xpath));
    }

    /**
     * This function does not use the XPath boolean conversion, Instead it uses
     * XML/C booleans. The boolean is determined using the algorithm described
     * in pseudo code below
     *
     * <pre>
     *   if(value is a valid integer)
     *     return value.toInteger() != 0;
     *   else if(value is 'true' or 'false' ignoring case)
     *     return value.toBoolean()
     *   else
     *     return false
     * </pre>
     *
     * @param node
     * @param xpath
     * @return
     */
    public static boolean getBooleanByXPath(Node node, String xpath) {
        try {
            XPathExpression expr = X_PATH.compile(xpath);
            return Boolean.parseBoolean((String) expr.evaluate(node, XPathConstants.STRING));
        } catch (XPathExpressionException ex) {
            ExceptionReporter.reportException(ex);
            return false;
        }
    }

    public static String getTextFromAttrByXPath(Node node, String xpath) {
        return getTextByXPath(node, xpath);
    }

    public static double getDoubleFromAttrByXPath(Node node, String xpath) {
        try {
            return Double.parseDouble(getNodeByXPath(node, xpath).getTextContent());
        } catch (NumberFormatException ex) {
            ExceptionReporter.reportException(ex);
            return Double.NaN;
        }
    }

    public static long getLongFromAttrByXPath(Node node, String xpath) {
        return Math.round(getDoubleByXPath(node, xpath));
    }

    /**
     * This function does not use the XPath boolean conversion, Instead it uses
     * XML/C booleans. The boolean is determined using the algorithm described
     * in pseudo code below
     *
     * <pre>
     *   if(value is a valid integer)
     *     return value.toInteger() != 0;
     *   else if(value is 'true' or 'false' ignoring case)
     *     return value.toBoolean()
     *   else
     *     return false
     * </pre>
     *
     * @param node
     * @param xpath
     * @return
     */
    public static boolean getBooleanFromAttrByXPath(Node node, String xpath) {
        return Boolean.parseBoolean(getNodeByXPath(node, xpath).getTextContent());
    }

    public static String toXMLString(Node node) {
        StringWriter sw = new StringWriter();
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerException ex) {
            ExceptionReporter.reportException(ex);
        }
        return sw.toString();
    }

    private XMLUtil() {
    }
}
