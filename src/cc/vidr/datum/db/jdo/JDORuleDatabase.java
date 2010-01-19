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

package cc.vidr.datum.db.jdo;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import cc.vidr.datum.Clause;
import cc.vidr.datum.Literal;
import cc.vidr.datum.UnsafeException;
import cc.vidr.datum.db.AbstractRuleDatabase;
import cc.vidr.datum.db.RuleDatabase;

/**
 * An implementation of the RuleDatabase interface backed by a JDO datastore.
 * 
 * @author  David Roberts
 */
public class JDORuleDatabase
extends AbstractRuleDatabase implements RuleDatabase {
    @SuppressWarnings("unchecked")
    protected Iterable<Clause> searchRaw(Literal goal) {
        List<Clause> clauses = new ArrayList<Clause>();
        PersistenceManager pm = PMF.instance().getPersistenceManager();
        
        Query query = pm.newQuery(JDORule.class);
        query.setFilter("predicate == predicateParam");
        query.declareParameters("java.lang.String predicateParam");
        try {
            List<JDORule> results =
                (List<JDORule>) query.execute(goal.getPredicate());
            for(JDORule result : results)
                clauses.add(result.rule());
        } finally {
            query.closeAll();
            pm.close();
        }
        return clauses;
    }
    
    public void assertRule(Clause rule) throws UnsafeException {
        if(!rule.isSafe())
            throw new UnsafeException();
        PersistenceManager pm = PMF.instance().getPersistenceManager();
        try {
            pm.makePersistent(new JDORule(rule));
        } finally {
            pm.close();
        }
    }
}
