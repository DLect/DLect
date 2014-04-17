/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.helper.provider;

import com.google.common.base.Objects;
import java.net.URI;

/**
 *
 * @author lee
 */
public class BlackboardProviderDetails {

    private final URI baseUrl;
    private final boolean httpAuth;
    private final boolean ssl;

    public BlackboardProviderDetails(URI baseUrl, boolean isHttpAuth, boolean isSsl) {
        this.baseUrl = baseUrl;
        this.httpAuth = isHttpAuth;
        this.ssl = isSsl;
    }

    public URI getBaseUrl() {
        return baseUrl;
    }

    public boolean isHttpAuth() {
        return httpAuth;
    }

    public boolean isSsl() {
        return ssl;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.baseUrl, this.httpAuth, this.ssl);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BlackboardProviderDetails other = (BlackboardProviderDetails) obj;
        return Objects.equal(this.baseUrl, other.baseUrl)
               && Objects.equal(this.httpAuth, other.httpAuth)
               && Objects.equal(this.ssl, other.ssl);
    }

    @Override
    public String toString() {
        return "ProviderDetails{" + "baseUrl=" + baseUrl + ", httpAuth=" + httpAuth + ", ssl=" + ssl + '}';
    }

}
