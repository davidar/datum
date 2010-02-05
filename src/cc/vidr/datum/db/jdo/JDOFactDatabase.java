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

import cc.vidr.datum.Literal;
import cc.vidr.datum.UnsafeException;
import cc.vidr.datum.db.AbstractFactDatabase;
import cc.vidr.datum.db.FactDatabase;
import cc.vidr.datum.term.Term;

/**
 * An implementation of the FactDatabase interface backed by a JDO datastore.
 * 
 * @author  David Roberts
 */
public class JDOFactDatabase
extends AbstractFactDatabase implements FactDatabase {
    @SuppressWarnings("unchecked")
    protected Iterable<Literal> searchRaw(Literal goal) {
        List<Literal> literals = new ArrayList<Literal>();
        PersistenceManager pm = PMF.instance().getPersistenceManager();
        StringBuilder filterBuilder = new StringBuilder();
        StringBuilder paramBuilder = new StringBuilder();
        List<Object> queryArgs = new ArrayList<Object>();
        int arity = Math.min(goal.getArity(), JDOFact.NUM_INDEXED_ARGUMENTS);
        
        // build query
        filterBuilder.append("predicate == predicateParam");
        paramBuilder.append("java.lang.String predicateParam");
        queryArgs.add(goal.getPredicate());
        for(int i = 0; i < arity; i++) {
            Term argument = goal.getArgument(i);
            if(argument.isVariable())
                continue;
            String fieldName = "argument" + i;
            filterBuilder.append(" && ").append(fieldName).append(" == ")
                .append(fieldName).append("Param");
            paramBuilder.append(", java.lang.String ")
                .append(fieldName).append("Param");
            queryArgs.add(argument.toString());
        }
        
        Query query = pm.newQuery(JDOFact.class);
        query.setFilter(filterBuilder.toString());
        query.declareParameters(paramBuilder.toString());
        try {
            List<JDOFact> results =
                (List<JDOFact>) query.executeWithArray(queryArgs.toArray());
            for(JDOFact result : results)
                literals.add(result.fact());
        } finally {
            query.closeAll();
            pm.close();
        }
        return literals;
    }
    
    public void assertFact(Literal fact) throws UnsafeException {
        if(!fact.isGround())
            throw new UnsafeException();
        PersistenceManager pm = PMF.instance().getPersistenceManager();
        try {
            pm.makePersistent(new JDOFact(fact));
        } finally {
            pm.close();
        }
    }
}
