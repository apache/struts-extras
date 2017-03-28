/*
 * $Id$
 *
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
import java.util.List;
import java.util.Map;

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

public class SslOffloadAwareServletActionRedirectResult extends SslOffloadAwareServletRedirectResult {
    /* The default parameter */
    public static final String DEFAULT_PARAM = "actionName";

    protected String actionName;
    protected String namespace;
    protected String method;

    public SslOffloadAwareServletActionRedirectResult() {}

    public SslOffloadAwareServletActionRedirectResult(String actionName) {
        this(null, actionName, null, null);
    }

    public SslOffloadAwareServletActionRedirectResult(String actionName, String method) {
        this(null, actionName, method, null);
    }

    public SslOffloadAwareServletActionRedirectResult(String namespace, String actionName, String method) {
        this(namespace, actionName, method, null);
    }

    public SslOffloadAwareServletActionRedirectResult(String namespace, String actionName, String method, String anchor) {
        super(null, anchor);
        this.namespace = namespace;
        this.actionName = actionName;
        this.method = method;
    }

    /**
     * @see com.opensymphony.xwork2.Result#execute(com.opensymphony.xwork2.ActionInvocation)
     */
    public void execute(ActionInvocation invocation) throws Exception {
        actionName = conditionalParse(actionName, invocation);
        if (namespace == null) {
            namespace = invocation.getProxy().getNamespace();
        } else {
            namespace = conditionalParse(namespace, invocation);
        }
        if (method == null) {
            method = "";
        } else {
            method = conditionalParse(method, invocation);
        }

        String tmpLocation = actionMapper.getUriFromActionMapping(new ActionMapping(actionName, namespace, method, null));

        setLocation(tmpLocation);

        super.execute(invocation);
    }

    /**
     * Sets the action name
     *
     * @param actionName The name
     */
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    /**
     * Sets the namespace
     *
     * @param namespace The namespace
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    /**
     * Sets the method
     *
     * @param method The method
     */
    public void setMethod(String method) {
        this.method = method;
    }

}
