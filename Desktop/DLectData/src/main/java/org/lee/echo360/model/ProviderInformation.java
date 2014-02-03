/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.model;

/**
 *
 * @author lee
 */
public class ProviderInformation {

    private String name;
    private String b2Url;
    private int clientId;
    private boolean http_auth;
    private boolean ssl_login;

    public ProviderInformation(String name, String b2Url, int clientId, boolean http_auth, boolean ssl_login) {
        this.name = name;
        this.b2Url = b2Url;
        this.clientId = clientId;
        this.http_auth = http_auth;
        this.ssl_login = ssl_login;
    }

    public String getName() {
        return name;
    }

    public String getB2Url() {
        return b2Url;
    }

    public int getClientId() {
        return clientId;
    }

    public boolean isUsingHttpAuth() {
        return http_auth;
    }

    public boolean isUsingSSL() {
        return ssl_login;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 73 * hash + (this.b2Url != null ? this.b2Url.hashCode() : 0);
        hash = 73 * hash + this.clientId;
        hash = 73 * hash + (this.http_auth ? 1 : 0);
        hash = 73 * hash + (this.ssl_login ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProviderInformation other = (ProviderInformation) obj;
        if ((this.name == null) ? (other.getName() != null) : !this.name.equals(other.getName())) {
            return false;
        }
        if ((this.b2Url == null) ? (other.getB2Url() != null) : !this.b2Url.equals(other.getB2Url())) {
            return false;
        }
        if (this.clientId != other.getClientId()) {
            return false;
        }
        if (this.http_auth != other.isUsingHttpAuth()) {
            return false;
        }
        if (this.ssl_login != other.isUsingSSL()) {
            return false;
        }
        return true;
    }
}
