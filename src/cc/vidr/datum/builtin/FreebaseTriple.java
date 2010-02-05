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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONObject;

import cc.vidr.datum.Literal;
import cc.vidr.datum.UnificationException;
import cc.vidr.datum.term.Atom;
import cc.vidr.datum.term.StringTerm;
import cc.vidr.datum.term.Term;
import cc.vidr.datum.util.FreebaseUtils;

/**
 * Allows retrieval of triples from Freebase.
 * 
 * @author  David Roberts
 */
public class FreebaseTriple extends Builtin {
    private static final String path = "service/mqlread?query=";
    
    protected String predicate() {
        return "freebase_triple/4";
    }
    
    protected Literal[] handle(Literal goal) throws Exception {
        Term subject = goal.getArgument(0);
        String property = ((StringTerm) goal.getArgument(1)).getValue();
        String type = goal.getArgument(2).toString();
        Term object = goal.getArgument(3);
        List<Literal> literals = query(
                "{\"query\":[{" +
                "\"id\":" + FreebaseUtils.termToString(subject) + "," +
                "\"" + property + "\":[" +
                FreebaseUtils.termToJSON(object, type) + "]" +
                "}]}", property, type);
        for(Iterator<Literal> iter = literals.iterator(); iter.hasNext();)
            try {
                iter.next().unify(goal);
            } catch(UnificationException e) {
                iter.remove();
            }
        return literals.toArray(new Literal[0]);
    }
    
    /**
     * Return the list of facts returned by the Freebase API for the given
     * query.
     * 
     * @param query     the MQL query
     * @param property  the property being queried
     * @param type      the type of the property
     * @return          the list of facts
     */
    @SuppressWarnings("unchecked")
    private List<Literal> query(String query, String property, String type)
    throws Exception {
        List<Literal> list = new LinkedList<Literal>();
        List<JSONObject> result = FreebaseUtils.getResultList(
                path + URLEncoder.encode(query, "UTF-8"));
        for(JSONObject subject : result)
            for(JSONObject object : (List<JSONObject>) subject.get(property))
                try {
                    list.add(new Literal(predicate(),
                        FreebaseUtils.atomFromID((String) subject.get("id")),
                        new StringTerm(property), new Atom(type),
                        FreebaseUtils.termFromJSON(object, type)));
                } catch(Exception e) {}
        return list;
    }
}
