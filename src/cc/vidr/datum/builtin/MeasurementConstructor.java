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

import cc.vidr.datum.Literal;
import cc.vidr.datum.term.Atom;
import cc.vidr.datum.term.FloatTerm;
import cc.vidr.datum.term.Measurement;

/**
 * Allows (de-)construction of Measurements.
 * 
 * @author  David Roberts
 */
public class MeasurementConstructor extends Builtin {
    protected String predicate() {
        return "measurement/3";
    }
    
    protected Literal[] handle(Literal goal) throws Exception {
        Measurement measurement;
        double value;
        Atom unit;
        if(goal.getArgument(0).isVariable()) { // constructor
            value = ((FloatTerm) goal.getArgument(1)).getValue();
            unit = (Atom) goal.getArgument(2);
            measurement = new Measurement(value, unit);
        } else { // deconstructor
            measurement = (Measurement) goal.getArgument(0);
            value = measurement.getValue();
            unit = measurement.getUnit();
        }
        Literal literal = new Literal(predicate(),
                measurement, new FloatTerm(value), unit);
        literal.unify(goal);
        return new Literal[] {literal};
    }
}
