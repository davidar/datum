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

package cc.vidr.datum.builtin;

import java.net.URLEncoder;
import java.util.List;

import org.json.simple.JSONObject;

import cc.vidr.datum.Literal;
import cc.vidr.datum.term.Atom;
import cc.vidr.datum.term.StringTerm;
import cc.vidr.datum.util.FreebaseUtils;

/**
 * Provides access to the Freebase Search API.
 * 
 * @author  David Roberts
 */
public class FreebaseSearch extends Builtin {
    private static final String path = "service/search?query=";
    
    protected String predicate() {
        return "freebase_search/2";
    }
    
    protected Literal[] handle(Literal goal) throws Exception {
        if(!goal.getArgument(1).isVariable())
            return new Literal[0];
        String query = ((StringTerm) goal.getArgument(0)).getValue();
        Atom atom = search(query);
        return new Literal[] {
                new Literal(predicate(), new StringTerm(query), atom) };
    }
    
    /**
     * Return the atom that is the best match for the given query.
     * 
     * @param query  the query
     * @return       the atom
     */
    private static Atom search(String query) throws Exception {
        List<JSONObject> result = FreebaseUtils.getResultList(
                path + URLEncoder.encode(query, "UTF-8"));
        JSONObject result0 = result.get(0);
        String id = (String) result0.get("id");
        return FreebaseUtils.atomFromID(id);
    }
}
