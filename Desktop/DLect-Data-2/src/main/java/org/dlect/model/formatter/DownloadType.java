/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.model.formatter;

import javax.xml.bind.annotation.XmlEnum;

/**
 *
 * @author lee
 */
@XmlEnum(String.class)
public enum DownloadType {

    AUDIO, VIDEO;
}
