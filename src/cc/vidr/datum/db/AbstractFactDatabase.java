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

package cc.vidr.datum.db;

import java.util.ArrayList;
import java.util.List;

import cc.vidr.datum.Literal;
import cc.vidr.datum.UnificationException;

/**
 * A basic implementation of a FactDatabase which filters out matching facts
 * from a collection of roughly matching facts.
 * 
 * @author  David Roberts
 */
public abstract class AbstractFactDatabase implements FactDatabase {
    public Literal[] search(Literal goal) {
        List<Literal> facts = new ArrayList<Literal>();
        for(Literal literal : searchRaw(goal))
            try {
                literal.unify(goal);
                facts.add(literal);
            } catch(UnificationException e) {
                // literal doesn't match goal
            }
        return facts.toArray(new Literal[0]);
    }
    
    /**
     * Return a collection of facts roughly matching the given goal.
     * 
     * @param goal  the goal
     * @return      the collection of facts
     */
    protected abstract Iterable<Literal> searchRaw(Literal goal);
}
