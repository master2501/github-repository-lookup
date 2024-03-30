package com.tui.mapper;

public interface Mapper<I, O> {
	O convertTo(I object);
}
