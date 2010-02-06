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

package cc.vidr.datum.term;

/**
 * A physical quantity.
 * 
 * @author  David Roberts
 */
public class Measurement extends AbstractConstant<Double> implements Constant {
    private static final long serialVersionUID = 5968789525209015272L;
    
    /** The unit of measurement */
    private final Atom unit;
    
    /**
     * Create a new Measurement with the given magnitude relative to the given
     * unit.
     * 
     * @param value  the magnitude
     * @param unit   the unit of measurement
     */
    public Measurement(double value, Atom unit) {
        super(value);
        this.unit = unit;
    }
    
    /**
     * Returns the unit of this measurement.
     * 
     * @return  the unit
     */
    public Atom getUnit() {
        return unit;
    }
    
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o instanceof Measurement) {
            Measurement m = (Measurement) o;
            return data.equals(m.data) && unit.equals(m.unit);
        }
        return false;
    }
    
    public String toString() {
        return data + " " + unit;
    }
}
