/*
 * Copyright (c) 2007-2010 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.plugins.p2.bridge;

import org.apache.maven.plugin.logging.Log;
import org.sonatype.p2.bridge.LogProxy;

public class PluginLogProxy
    implements LogProxy
{

    private final Log log;

    public PluginLogProxy( final Log log )
    {
        this.log = log;
    }

    public void info( final CharSequence content )
    {
        log.info( content );
    }

}
