/*
 * Copyright (C) 2010  David Roberts <d@vidr.cc>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.vidr.datum.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import cc.vidr.datum.Atom;
import cc.vidr.datum.StringTerm;
import cc.vidr.datum.Term;

/**
 * Methods for interacting with Freebase.com
 * 
 * @author  David Roberts
 */
public final class FreebaseUtils {
    private static final String endpoint = "http://api.freebase.com/api/";
    
    /**
     * Prevent instantiation.
     */
    private FreebaseUtils() {}
    
    /**
     * Return the list of JSON objects returned by the API for the given query.
     * 
     * @param path  the API service path
     * @return      the list of JSON objects
     * @throws      IOException if there was an error communicating with the
     *              server
     * @throws      ParseException if the server returns malformed JSON
     */
    @SuppressWarnings("unchecked")
    public static List<JSONObject> getResultList(String path)
    throws IOException, ParseException {
        URLConnection connection = new URL(endpoint + path).openConnection();
        connection.connect();
        InputStream stream = connection.getInputStream();
        Reader reader = new InputStreamReader(stream);
        JSONObject o = (JSONObject) JSONValue.parse(reader);
        return (List<JSONObject>) o.get("result");
    }
    
    /**
     * Return the MQL/JSON string representation of the given term.
     * 
     * @param term  the term
     * @return      the string representation
     */
    public static String termToString(Term term) {
        if(term.isVariable())
            return "null";
        if(term instanceof Atom)
            return "\"/en/" + term + "\"";
        if(term instanceof StringTerm)
            return term.toString();
        throw new IllegalArgumentException("Unsupported type: " + term);
    }
    
    /**
     * Return the MQL/JSON object representation for the given term.
     * 
     * @param term  the term
     * @param type  the type of the MQL object
     * @return      the JSON representation
     */
    public static String termToJSON(Term term, String type) {
        if(type.equals("object"))
            return "{\"id\":" + termToString(term) + "}";
        if(type.equals("text"))
            return "{\"lang\":\"/lang/en\"," +
                   "\"value\":" + termToString(term) + "}";
        throw new IllegalArgumentException("Invalid type: " + type);
    }
    
    /**
     * Return the term represented by the given MQL/JSON object.
     * 
     * @param o     the JSON object
     * @param type  the type of the MQL object
     * @return      the term
     */
    public static Term termFromJSON(JSONObject o, String type) {
        if(type.equals("object"))
            return FreebaseUtils.atomFromID((String) o.get("id"));
        if(type.equals("text"))
            return new StringTerm((String) o.get("value"));
        throw new IllegalArgumentException("Invalid type: " + type);
    }
    
    /**
     * Return the atom corresponding to the given Freebase ID.
     * 
     * @param id  the Freebase ID
     * @return    the atom
     */
    public static Atom atomFromID(String id) {
        if(id.startsWith("/en/"))
            return new Atom(id.substring("/en/".length()));
        throw new IllegalArgumentException("Unsupported domain: " + id);
    }
}
