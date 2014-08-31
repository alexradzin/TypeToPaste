package org.typetopaste.typist;

/**
 * Types sequence of given type using {@code singleTypist} parametrized to the same type. 
 * @author alex
 *
 * @param <T>
 */
public class SequenceTypist<T> implements Typist<Iterable<T>> {
	private final Typist<T> singleTypist;
	
	public SequenceTypist(Typist<T> singleTypist) {
		super();
		this.singleTypist = singleTypist;
	}


	@Override
	public void type(Iterable<T> sequence) {
		for (T t : sequence) {
			singleTypist.type(t);
		}
	}

}
