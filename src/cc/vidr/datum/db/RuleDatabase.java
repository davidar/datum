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

import cc.vidr.datum.Clause;
import cc.vidr.datum.Literal;
import cc.vidr.datum.UnsafeException;

/**
 * Represents a database containing rules.
 * 
 * @author  David Roberts
 */
public interface RuleDatabase {
    /**
     * Return an array of rules whose head matches the goal.
     * 
     * @param goal  the goal
     * @return      the array of rules
     */
    Clause[] search(Literal goal);
    
    /**
     * Assert the given safe rule.
     * 
     * @param rule  the rule
     * @throws      UnsafeException if the rule is unsafe
     */
    void assertRule(Clause rule) throws UnsafeException;
}
