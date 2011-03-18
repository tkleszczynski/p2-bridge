/*
 * Copyright (c) 2007-2011 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.eclipse.bridge.internal;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;

import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.eclipse.bridge.EclipseBridge;
import org.sonatype.eclipse.bridge.EclipseInstance;
import org.sonatype.eclipse.bridge.EclipseLocation;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;

@Component( role = EclipseBridge.class )
public class DefaultEclipseBridge
    implements EclipseBridge
{

    public EclipseInstance createInstance( final EclipseLocation location )
    {
        final File eclipseDir = location.get();
        final File pluginsDir = new File( eclipseDir, "plugins" );
        if ( !pluginsDir.exists() )
        {
            throw new RuntimeException( String.format( "Cannot find org.eclipse.osgi as [%s] does not exist",
                pluginsDir.getAbsolutePath() ) );
        }
        final File[] plugins = pluginsDir.listFiles( new FilenameFilter()
        {

            public boolean accept( final File dir, final String name )
            {
                return name.startsWith( "org.eclipse.osgi_" ) && name.endsWith( ".jar" );
            }

        } );
        if ( plugins == null || plugins.length == 0 )
        {
            throw new RuntimeException( String.format( "Cannot find org.eclipse.osgi in [%s]",
                pluginsDir.getAbsolutePath() ) );
        }
        if ( plugins.length > 1 )
        {
            throw new RuntimeException( String.format( "Found more then one org.eclipse.osgi in [%s]",
                pluginsDir.getAbsolutePath() ) );
        }
        final ClassLoader thisClassLoader = this.getClass().getClassLoader();
        final URL instanceJar = thisClassLoader.getResource( "META-INF/libs/eclipse-bridge-instance.jar" );
        if ( instanceJar == null )
        {
            throw new RuntimeException( "Could not find META-INF/libs/eclipse-bridge-instance.jar in plugin classpath" );
        }
        try
        {
            final URL equinoxURL = plugins[0].toURI().toURL();

            final JarClassLoader jcl = new JarClassLoader();
            jcl.add( instanceJar.openStream() );
            jcl.add( equinoxURL );

            final JclObjectFactory factory = JclObjectFactory.getInstance();
            final Object instance =
                factory.create( jcl, "org.sonatype.eclipse.bridge.internal.instance.DefaultEclipseInstance",
                    new Object[] { location }, new Class[] { EclipseLocation.class } );

            return EclipseInstance.class.cast( instance );
        }
        catch ( final Exception e )
        {
            throw new RuntimeException( e );
        }
    }

}
