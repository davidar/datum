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

import cc.vidr.datum.Clause;
import cc.vidr.datum.Literal;
import cc.vidr.datum.UnificationException;

/**
 * A basic implementation of a RuleDatabase which filters out matching rules
 * from a collection of roughly matching rules.
 * 
 * @author  David Roberts
 */
public abstract class AbstractRuleDatabase implements RuleDatabase {
    public Clause[] search(Literal goal) {
        List<Clause> rules = new ArrayList<Clause>();
        for(Clause clause : searchRaw(goal))
            try {
                rules.add(clause.subst(clause.getHead().unify(goal)));
            } catch(UnificationException e) {
                // clause doesn't match goal
            }
        return rules.toArray(new Clause[0]);
    }
    
    /**
     * Return a collection of rules roughly matching the given goal.
     * 
     * @param goal  the goal
     * @return      the collection of rules
     */
    protected abstract Iterable<Clause> searchRaw(Literal goal);
}
