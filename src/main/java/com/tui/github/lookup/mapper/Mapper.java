package com.tui.github.lookup.mapper;

public interface Mapper<I, O> {
	O convertTo(I object);
}
