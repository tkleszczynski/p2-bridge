/*
 * Copyright (c) 2007-2010 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.plugins.p2.bridge;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sonatype.eclipse.bridge.EclipseInstance;
import org.sonatype.p2.bridge.IUIdentity;
import org.sonatype.p2.bridge.P2Director;
import org.sonatype.p2.bridge.P2ProfileRegistry;
import org.sonatype.p2.bridge.client.P2DirectorFactory;
import org.sonatype.p2.bridge.client.P2ProfileRegistryFactory;

/**
 * @goal list-available
 * @phase process-resources
 * @requiresDependencyResolution runtime
 */
public class ListAvailableMojo
    extends WithinEclipseRunningProjectBasedMojo
{

    /**
     * @parameter expression="${location}" default-value="${project.build.directory}/tycho-p2/p2-install-folder/p2"
     * @required
     */
    protected File location;

    /**
     * @parameter default-value="tycho-p2"
     * @required
     */
    private String profile;

    /**
     * @parameter
     * @required
     */
    private List<String> repositories;

    /**
     * @component
     */
    private P2DirectorFactory p2DirectorFactory;

    /**
     * @component
     */
    private P2ProfileRegistryFactory p2ProfileRegistryFactory;

    /**
     * @component
     */
    private Console console;

    @Override
    protected void doWithEclipse( final EclipseInstance eclipse )
    {
        console.printHeader( String.format( "Available versions for %s :", location.getAbsolutePath() ) );

        final P2Director p2Director = p2DirectorFactory.create( eclipse );
        final P2ProfileRegistry p2ProfileRegistry = p2ProfileRegistryFactory.create( eclipse );

        final PluginLogProxy logProxy = new PluginLogProxy( getLog() );

        final IUIdentity[] installedRoots = p2ProfileRegistry.getInstalledRoots( location, profile );

        final IUIdentity[] availableRoots =
            p2Director.getAvailableIUs( logProxy, toIdSet( installedRoots ), repositories );

        if ( toIdSet( availableRoots ).size() == 1 )
        {
            for ( final IUIdentity iu : availableRoots )
            {
                console.print( iu.getVersion() );
            }
        }
        else
        {
            for ( final IUIdentity iu : availableRoots )
            {
                console.print( iu.toString() );
            }
        }

        console.print();
    }

    private Set<String> toIdSet( final IUIdentity[] installedRoots )
    {
        final Set<String> roots = new HashSet<String>();
        for ( final IUIdentity iu : installedRoots )
        {
            roots.add( iu.getId() );
        }
        return roots;
    }
}
