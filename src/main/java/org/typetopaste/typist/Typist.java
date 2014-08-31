package org.typetopaste.typist;

/**
 * {@code Typist} is a interface that defines contract for all typists - classes that "paste" content by typing instead of using clip-board. 
 * @author alex
 *
 * @param <T> generic type of content. In fact String or Character are used. 
 */
public interface Typist<T> {
	public void type(T c);
}
