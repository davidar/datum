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

package cc.vidr.datum.util;

/**
 * Utility class for random operations.
 * 
 * @author  David Roberts
 */
public final class Random {
    /** Random Number Generator */
    private static java.util.Random random = new java.util.Random();
    
    /**
     * Prevent instantiation.
     */
    private Random() {}
    
    /**
     * Return a random element from the given array.
     * 
     * @param <T>  the type of the array
     * @param a    the array
     * @return     a random element in the array, or null if the array is empty
     */
    public static <T> T element(T[] a) {
        return a.length > 0 ? a[random.nextInt(a.length)] : null;
    }
}
