/*
 * Copyright (c) 2014, the Cytoscape Consortium and the Regents of the University of California
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.cytoscape.ndex.server;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractListModel;
import org.cytoscape.ndex.strings.ErrorMessage;
import org.cytoscape.ndex.strings.FilePath;
import org.cytoscape.ndex.strings.ResourcePath;

/**
 *
 * @author David Welker
 */
public class ServerList extends AbstractListModel
{
    //A list of servers, both DEFAULT and ADDED, displayed to the user.
    private List<Server> serverList = new ArrayList<Server>();
    //A list of default servers with CREDENTIALS.
    private List<Server> defaultServerCredentials = new ArrayList<Server>();
    //Efficiently tracks server names already used to prevent duplicates.
    private HashSet<String> namesUsed = new HashSet<String>();
    
    public ServerList()
    {
        super();
        readServers();
    }
    
    private void readServers()
    {
        readDefaultServers();
        readAddedServers();
        readDefaultServerCredentials();
        mergeDefaultServerCredentials();
    }
   
    private void readDefaultServers()
    {
        Collection<Server> defaultServers = readServerCollection(ResourcePath.DEFAULT_NDEX_SERVERS); 
        serverList.addAll(defaultServers);
        for( Server server : defaultServers )
            namesUsed.add( server.getName() );
    }
    
    private void readDefaultServerCredentials()
    {
        Collection<Server> credentials = readServerCollection(ResourcePath.DEFAULT_SERVER_CREDENTIALS);
        defaultServerCredentials.addAll(credentials);
        for( Server server : credentials )
            namesUsed.add( server.getName() );
    }
    
    private void mergeDefaultServerCredentials()
    {
        for( Server credentials : defaultServerCredentials )
            mergeDefaultServerCredentials(credentials);
    }
    
    private void mergeDefaultServerCredentials(Server credentials )
    {   
        for( Server server : getDefaultServers() )
        {
            if( server.namesEqual( credentials ) )
            {
                server.useCredentialsOf( credentials );
            }
        }      
    }
       
    private void readAddedServers()
    {
        Collection<Server> addedServers = readServerCollection(ResourcePath.ADDED_SERVERS);     
        serverList.addAll(addedServers);
        for( Server server : addedServers )
            namesUsed.add( server.getName() );
    }
    
    private Collection<Server> readServerCollection(String resourcePath)
    {
        InputStream is = ClassLoader.getSystemResourceAsStream(resourcePath);
        BufferedReader br = new BufferedReader( new InputStreamReader (is) );
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<Server>>(){}.getType();
        Collection<Server> result =  gson.fromJson(br, collectionType);
        try
        {
            br.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(ServerList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    private List<Server> getAddedServers()
    {
        List<Server> result = new ArrayList<Server>();
        for( Server s : serverList )
        {
            if( s.getType() == Server.Type.ADDED )
                result.add(s);
        }
        return result;
    }
    
    private List<Server> getDefaultServers()
    {
        List<Server> result = new ArrayList<Server>();
        for( Server s : serverList )
        {
            if( s.getType() == Server.Type.DEFAULT )
                result.add(s);
        }
        return result;
    }
    
    public void save()
    {
        saveAddedServers();
        saveDefaultServerCredentials();
    }
    
    private void saveAddedServers()
    {
        saveServerList( getAddedServers(), FilePath.ADDED_SERVERS );
    }
    
    private void saveDefaultServerCredentials()
    {
        saveServerList( defaultServerCredentials, FilePath.DEFAULT_SERVER_CREDENTIALS );
    }
    
    private void saveServerList( List<Server> serverList, String filePath )
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson( serverList );
        File serverFile = new File( filePath );
        try
        {
            Files.write(json, serverFile, Charsets.UTF_8);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }    
    }
    
    /**
     * Checks whether a proposed name for a server has already been used.
     * This is currently used when editing an existing server.
     * @param name The name to be checked.
     * @return Whether the name is used or not.
     */
    public boolean isNameUsed(String name)
    {
        return namesUsed.contains(name);
    }
    
    /**
     * This method is used to inform the server list that one of the servers
     * already in the list has been edited so that it can fire the appropriate
     * events to notify and update the GUI.
     * @param editedServer The server that was just edited.
     */
    public void edited(Server editedServer)
    {
        int indexOfEditedServer = serverList.indexOf(editedServer);
        fireContentsChanged(this, indexOfEditedServer, indexOfEditedServer);
    }
    
    public void rename(Server server)
    {
        //TODO Implement this
    }
    
    public void delete(Server server)
    {
        if( !serverList.contains(server) )
            throw new IllegalArgumentException("The server to be deleted must exist in the list.");
        int indexOfDeletedServer = serverList.indexOf(server);
        serverList.remove(server);
        this.fireIntervalRemoved(server, indexOfDeletedServer, indexOfDeletedServer);
    }
    
    public void add(Server server) throws Exception
    {
        if( namesUsed.contains(server.getName()) )
            throw new Exception( ErrorMessage.serverNameAlreadyUsed );
        namesUsed.add(server.getName());
        serverList.add(server);
        int indexOfAddedServer = serverList.indexOf(server);
        fireContentsChanged(this, indexOfAddedServer, indexOfAddedServer);
    }
    
    /**
     * This method registers the credentials for a default server so they may be saved and retrieved later.
     * @param defaultServer The default server for which credentials are being saved.
     */
    public void registerDefaultServerCredentials(Server defaultServer)
    {
        if( defaultServer.getType() != Server.Type.DEFAULT )
            throw new IllegalArgumentException("Only DEFAULT server credentials may be be seperately registered.");
        Server credentials = new Server(defaultServer);
        //Change the type to CREDENTIALS.
        credentials.setType(Server.Type.CREDENTIALS);
        //Search for an existing credentials to replace, if any.
        Server existingCredentialsToReplace = null;
        for( Server s : defaultServerCredentials )
        {
            if( credentials.getName().equals(s.getName()) )
                existingCredentialsToReplace = s;
        }
        //If credentials for the server do not already exist, add them. Otherwise, replace them.
        if( existingCredentialsToReplace == null )
            defaultServerCredentials.add(credentials);
        else
        {
            int index = defaultServerCredentials.indexOf(existingCredentialsToReplace);
            defaultServerCredentials.set( index, credentials);
        }
    }
    
    public String findNextAvailableName(String startName)
    {
        int count = 0;
        while( true )
        {
            String candidate = startName + "-" + ++count;
            if( !namesUsed.contains( candidate ) )
                return candidate;           
        }
    }
    
    public Server get(int index)
    {
        return serverList.get(index);
    }
    
    @Override
    public int getSize()
    {
        return serverList.size();
    }

    @Override
    public Object getElementAt(int index)
    {
        return serverList.get(index);
    }
    
}
