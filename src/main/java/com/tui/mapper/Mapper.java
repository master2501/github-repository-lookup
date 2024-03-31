package com.tui.mapper;

/**
 * Mapper interface <br>
 * <p>
 * Maps one object into another
 *
 * @param <I> inbound object type
 * @param <O> outbound object type
 */
public interface Mapper<I, O> {
	/**
	 * Converts an object into another
	 * 
	 * @param object the inbound object
	 * @return the target object
	 */
	O convertTo(I object);
}
