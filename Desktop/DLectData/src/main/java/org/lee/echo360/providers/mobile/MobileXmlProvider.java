/*
 *  Copyright (C) 2013 lee
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lee.echo360.providers.mobile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.client.HttpClient;
import org.lee.echo360.model.Blackboard;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author lee
 */
public interface MobileXmlProvider {

    public Document getLoginResponce(Blackboard b) throws IOException, SAXException, ParserConfigurationException;

    public Document getSubjects() throws IOException, SAXException, ParserConfigurationException;

    public String getLectureContentString(String subjCode, String contentID) throws IOException;

    public Document getLectureXML(final String code) throws ParserConfigurationException, IllegalStateException, IOException, SAXException;

    public URL getRootURL(String regex) throws MalformedURLException;

    public HttpClient getClient();
}
