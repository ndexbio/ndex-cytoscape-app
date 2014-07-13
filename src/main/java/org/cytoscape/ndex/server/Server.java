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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David Welker
 */
public class Server
{
    public enum Type {DEFAULT, CREDENTIALS, ADDED}
    
    private String name;
    private String url;
    private String username;
    private String password;
    private Type type;
    
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
    }
    
    public boolean namesEqual(Server s)
    {
        return name.equals(s.name);
    }
    
    public void useCredentialsOf(Server s)
    {
        username = s.username;
        password = s.password;
    }
    
//    private Server(String name, String url, String username, String password, Type type )
//    {
//        this.name = name;
//        this.url = url;
//        this.username = username;
//        this.password = password;
//        this.type = type;
//    }
    
    public String show()
    {
        String result = "";
        result += "Name: " + name +"\n";
        result += "URL: " + url +"\n";
        result += "Username: " + username +"\n";
        result += "Password: " + password +"\n";
        result += "Type: " + type +"\n";
        return result;
    }
    
    public String getHeader()
    {
        String username = getUsername();
        if( username == null )
            username = "None";
        
        String header = "NDEx Server Information\n===============\n";
        header += "Name: " + getName() + "\n";
        header += "URL: " + getUrl() + "\n";
        header += "Username: " + username + "\n";
        return header;
    }
   
    public boolean isDefault()
    {
        return type == Type.DEFAULT;
    }
    
    public String toString()
    {
        return name;
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
        this.username = username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setType(Type type)
    {
        this.type = type;
    }
    // </editor-fold>
    
}
