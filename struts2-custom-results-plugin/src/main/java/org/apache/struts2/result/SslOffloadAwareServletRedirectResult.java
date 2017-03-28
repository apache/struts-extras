/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.struts2.result;

import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.result.ServletRedirectResult;
import org.apache.struts2.views.util.UrlHelper;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.config.entities.ResultConfig;
import com.opensymphony.xwork2.inject.Inject;

public class SslOffloadAwareServletRedirectResult extends ServletRedirectResult {
    private static final long serialVersionUID = -5384946213381645549L;
    private static final Logger LOG = LogManager.getLogger(SslOffloadAwareServletRedirectResult.class);
    private static final Pattern FORWARDED_PROTO_PARAM_HTTPS = Pattern.compile("[^;]proto=https[$;]");

    private UrlHelper urlHelper;

    @Inject
    public void setUrlHelper(UrlHelper urlHelper) {
        this.urlHelper = urlHelper;
    }

    public SslOffloadAwareServletRedirectResult() {
        super();
    }

    public SslOffloadAwareServletRedirectResult(String location) {
        this(location, null);
    }

    public SslOffloadAwareServletRedirectResult(String location, String anchor) {
        super(location, anchor);
    }

    /**
     * Redirects to the location specified by calling
     * {@link HttpServletResponse#sendRedirect(String)}.
     * 
     * @param finalLocation
     *            the location to redirect to.
     * @param invocation
     *            an encapsulation of the action execution state.
     * @throws Exception
     *             if an error occurs when redirecting.
     */
    protected void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {
        ActionContext ctx = invocation.getInvocationContext();
        HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) ctx.get(ServletActionContext.HTTP_RESPONSE);

        if (isPathUrl(finalLocation)) {
            if (!finalLocation.startsWith("/")) {
                ActionMapping mapping = actionMapper.getMapping(request, Dispatcher.getInstance().getConfigurationManager());
                String namespace = null;
                if (mapping != null) {
                    namespace = mapping.getNamespace();
                }

                if ((namespace != null) && (namespace.length() > 0) && (!"/".equals(namespace))) {
                    finalLocation = namespace + "/" + finalLocation;
                } else {
                    finalLocation = "/" + finalLocation;
                }
            }

            // if the URL's are relative to the servlet context, append the
            // servlet context path
            if (prependServletContext && (request.getContextPath() != null)
                    && (request.getContextPath().length() > 0)) {
                finalLocation = request.getContextPath() + finalLocation;
            }

            finalLocation = fixSchemeIfNeeded(finalLocation, request);
        }
        ResultConfig resultConfig = invocation.getProxy().getConfig().getResults().get(invocation.getResultCode());
        if (resultConfig != null) {
            Map<String, String> resultConfigParams = resultConfig.getParams();

            List<String> prohibitedResultParams = getProhibitedResultParams();
            for (Map.Entry<String, String> e : resultConfigParams.entrySet()) {
                if (!prohibitedResultParams.contains(e.getKey())) {
                    Collection<String> values = conditionalParseCollection(e.getValue(), invocation,
                            suppressEmptyParameters);
                    if (!suppressEmptyParameters || !values.isEmpty()) {
                        requestParameters.put(e.getKey(), values);
                    }
                }
            }
        }

        StringBuilder tmpLocation = new StringBuilder(finalLocation);
        urlHelper.buildParametersString(requestParameters, tmpLocation, "&");

        // add the anchor
        if (anchor != null) {
            tmpLocation.append('#').append(anchor);
        }

        finalLocation = response.encodeRedirectURL(tmpLocation.toString());

        LOG.debug("Redirecting to finalLocation: {}", finalLocation);

        sendRedirect(response, finalLocation);
    }

    protected String fixSchemeIfNeeded(String location, HttpServletRequest request) {
        if (shouldFixScheme(request)) {
            LOG.debug("https offloading happened, fixing redirectlocation");
            StringBuilder fixedLocation = new StringBuilder();
            fixedLocation.append("https");
            fixedLocation.append("://");
            fixedLocation.append(request.getServerName());
            if (request.getServerPort() != 80) {
                fixedLocation.append(':');
                fixedLocation.append(request.getServerPort());
            }
            fixedLocation.append(location);

            return fixedLocation.toString();
        } else {
            return location;
        }
    }

    protected boolean shouldFixScheme(HttpServletRequest request) {
        return "https".equals(request.getHeader("X-Forwarded-Proto")) || hasForwardedHeaderWithProtoParamHttps(request);
    }

    protected boolean hasForwardedHeaderWithProtoParamHttps(HttpServletRequest request) {
        Enumeration<String> forwardedHeaders = request.getHeaders("Forwarded");

        if (forwardedHeaders == null) {
            return false;
        }

        while (forwardedHeaders.hasMoreElements()) {
            String forwardedHeader = forwardedHeaders.nextElement();
            String[] forwardedHeaderElements = forwardedHeader.split(",");
            
            for (String forwardedHeaderElement : forwardedHeaderElements) {
                Matcher matcher = FORWARDED_PROTO_PARAM_HTTPS.matcher(forwardedHeaderElement.trim());

                if (matcher.matches()) {
                    return true;
                }
            }
        }

        return false;
    }

}
