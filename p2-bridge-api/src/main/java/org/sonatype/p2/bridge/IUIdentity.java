/*
 * Copyright (c) 2007-2010 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.p2.bridge;

public class IUIdentity
{

    private final String id;

    private final String version;

    public IUIdentity( final String id, final String version )
    {
        this.id = id;
        this.version = version;
    }

    public String getId()
    {
        return id;
    }

    public String getVersion()
    {
        return version;
    }

    public String identity()
    {
        return String.format( "%s/%s", id, version );
    }

    @Override
    public String toString()
    {
        return identity();
    }

}
