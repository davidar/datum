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

package cc.vidr.datum;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows incremental construction of Clauses.
 * 
 * @author  David Roberts
 */
public class ClauseBuilder {
    /** The head literal */
    private Literal head;
    /** The current body of conditions */
    private List<Literal> body = new ArrayList<Literal>();
    
    /**
     * Set the head literal.
     * 
     * @param head  the head literal
     */
    public void setHead(Literal head) {
        this.head = head;
    }
    
    /**
     * Append the given condition to the body.
     * 
     * @param condition  the condition
     */
    public void addCondition(Literal condition) {
        body.add(condition);
    }
    
    /**
     * Build the Clause.
     * 
     * @return  the clause
     */
    public Clause toClause() {
        return new Clause(head, body.toArray(new Literal[0]));
    }
}
