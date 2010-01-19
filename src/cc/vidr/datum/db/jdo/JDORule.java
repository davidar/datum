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

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import cc.vidr.datum.Clause;

/**
 * A JDO model wrapping a rule.
 * 
 * @author  David Roberts
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class JDORule {
    /** The unique ID of the rule */
    @SuppressWarnings("unused")
    @PrimaryKey
    private String id;
    
    /** The rule */
    @Persistent(serialized = "true")
    private Clause rule;
    
    /** The predicate of the head of the rule */
    @SuppressWarnings("unused")
    @Persistent
    private String predicate;
    
    /**
     * Wrap the given rule.
     * 
     * @param rule  the rule to be wrapped
     */
    public JDORule(Clause rule) {
        this.id = rule.getVariantTag();
        this.rule = rule;
        this.predicate = rule.getHead().getPredicate();
    }
    
    /**
     * Return a renamed copy of the wrapped rule.
     * 
     * @return  the renamed rule
     */
    public Clause rule() {
        return rule.rename();
    }
}
