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

package org.cytoscape.ndex.internal.server;

import java.io.IOException;
import java.util.UUID;

import org.ndexbio.model.exceptions.NdexException;
import org.ndexbio.model.object.NdexStatus;
import org.ndexbio.model.object.User;
import org.ndexbio.rest.client.NdexRestClient;
import org.ndexbio.rest.client.NdexRestClientModelAccessLayer;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 *
 * @author David Welker
 */
public class Server
{
    public enum Type {DEFAULT, CREDENTIALS, ADDED}
    
    private String name;  // unique in the ServerList
    private String url;
    private String username;
    private String password;
    private String description;
    private Type type;
    private UUID userId;
    
    private boolean authenticated;
    
    /**
     * Default constructor,
     */
    public Server()
    {
    }
    
    /**
     * Copy constructor/
     * @param s The server to be copied.
     */
    public Server(Server s)
    {
        name = s.name;
        url = s.url;
        username = s.username;
        password = s.password;
        type = s.type;
        description = s.description;
        authenticated = s.authenticated;
        userId = s.getUserId();
    }

    public boolean isRunningNdexServer(NdexRestClientModelAccessLayer mal) throws IOException, NdexException
    {
    	    NdexStatus s = mal.getServerStatus();
    	    return s.getMessage().equals("Online");
    }
    
    public boolean check(NdexRestClientModelAccessLayer mal) throws IOException
    {
        boolean usernamePresent = username != null && !username.isEmpty();
        boolean passwordPresent = password != null && !username.isEmpty();
        if( !usernamePresent && !passwordPresent )
        {
            authenticated = false;
            return true;
        }
        else
        {
          //  try
          //  {
            	userId = mal.getNdexRestClient().getUserUid();
            	authenticated = userId != null;
          /* Old implementation. commenting out for now. 	
           * User user = mal.authenticateUser(mal.getUserName(),mal.getPassword());
                authenticated = user !=null;
                if (user!=null) 
                	userId = user.getExternalId(); */
          /*  }
            catch (NdexException e)
            {
                authenticated = false;
                e.printStackTrace();
            } */
            return authenticated;
        }
    }
    
    public boolean hasSameName(Server s)
    {
        return name.equals(s.name);
    }
    
    public void useCredentialsOf(Server s)
    {
        username = s.username;
        password = s.password;
        description = s.description;
    }
    
 /*   public String show()
    {
        String result = "";
        result += "Name: " + name +"\n";
        result += "URL: " + url +"\n";
        result += "Username: " + username +"\n";
        result += "Password: " + password +"\n";
        result += "Type: " + type +"\n";
        result += "Authenticated: " + authenticated +"\n";
        result += "UUID: " + userId +"\n";
        return result;
    } */
    
    public String getHeader()
    {
    	String userNameStr = 
         username == null ? "None" : username;
        
        String header = "NDEx Source Information\n===============\n";
        header += "Name: " + name + "\n";
        header += "URL: " + url + "\n";
        header += "Username: " + userNameStr + "\n";
        if( description != null )
        {
            header += "\n";
            header += "Description\n=======\n";
            header += description + "\n";
        }
        return header;
    }
    
    public NdexRestClientModelAccessLayer getModelAccessLayer() throws JsonProcessingException, IOException, NdexException
    {
        NdexRestClient client = new NdexRestClient(username,password,url);
        return new NdexRestClientModelAccessLayer(client);
    }
   
 /*   public NdexRestClientModelAccessLayer getModelAccessLayer(String userName, String password)
    {
        NdexRestClient client = new NdexRestClient(username,password,url);
        return new NdexRestClientModelAccessLayer(client);
    } */

    public boolean isDefault()
    {
        return type == Type.DEFAULT;
    }
    
    @Override
    public String toString()
    {
        return name;
    }
    
    public String display()
    {
        return name + " ("+url+")";
    }
       
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    //Getters
    public String getName()
    {
        return name;
    }

    public String getUrl()
    {
        return url;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public Type getType()
    {
        return type;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    //Setters
    public void setName(String name)
    {
        this.name = name;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public void setUsername(String username)
    {
        if( username != null && username.trim().equals("") )
            this.username = null;
        else
            this.username = username;
    }

    public void setPassword(String password)
    {
        if( password != null && password.trim().equals("") )
            this.password = null;
        else
            this.password = password;
    }

    public void setType(Type type)
    {
        this.type = type;
    }
    
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    public boolean isAuthenticated() 
    {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) 
    {
        this.authenticated = authenticated;
    }
    // </editor-fold>

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}
	
	
	// give each server an unique ID 
	/* static final AtomicLong NEXT_ID = new AtomicLong(0);
	 final long id = NEXT_ID.getAndIncrement();

	 public long getId() {
	         return id;
	 } */
    
}
