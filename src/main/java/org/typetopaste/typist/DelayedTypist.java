package org.typetopaste.typist;

import java.util.concurrent.Callable;

public class DelayedTypist<T> implements Typist<T> {
	private final Typist<T> workingTypist;
	private final Callable<Long> waitBeforeGetter;
	private final Callable<Long> waitAfterGetter;
	
	
	public DelayedTypist(Typist<T> workingTypist, Callable<Long> waitBeforeGetter, Callable<Long> waitAfterGetter) {
		super();
		this.workingTypist = workingTypist;
		this.waitBeforeGetter = waitBeforeGetter;
		this.waitAfterGetter = waitAfterGetter;
	}
	


	@Override
	public void type(T c) {
		delay(getWaitBefore());
		workingTypist.type(c);
		delay(getWaitAfter());
	}

	private void delay(long delay) {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			// ignore
		}
	}


	public long getWaitBefore() {
		return getValue(waitBeforeGetter);
	}


	public long getWaitAfter() {
		return getValue(waitAfterGetter);
	}

	private Long getValue(Callable<Long> fetcher) {
		try {
			return fetcher.call();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
	
}
