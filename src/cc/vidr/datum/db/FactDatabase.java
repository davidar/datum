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

import cc.vidr.datum.Literal;
import cc.vidr.datum.UnsafeException;

/**
 * Represents a database containing facts.
 * 
 * @author  David Roberts
 */
public interface FactDatabase {
    /**
     * Return an array of facts matching the given goal.
     * 
     * @param goal  the goal
     * @return      the array of facts
     */
    Literal[] search(Literal goal);
    
    /**
     * Assert the given ground fact.
     * 
     * @param fact  the fact
     * @throws      UnsafeException if the fact is not ground
     */
    void assertFact(Literal fact) throws UnsafeException;
}
