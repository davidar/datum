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

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * A constant uniquely identified by a name.
 * 
 * @author  David Roberts
 */
public final class Atom extends AbstractConstant<String> implements Constant {
    private static final long serialVersionUID = -2144459114646717101L;
    
    /**
     * Create a new atom with the given name.
     * 
     * @param name  the name of the atom
     */
    public Atom(String name) {
        super(name.intern());
    }
    
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o instanceof Atom) {
            Atom a = (Atom) o;
            return data == a.data;
        }
        return false;
    }
    
    public int hashCode() {
        return data.hashCode();
    }
    
    public String toString() {
        return data;
    }
    
    private void readObject(ObjectInputStream in)
    throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        data = data.intern();
    }
}
