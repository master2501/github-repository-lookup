package com.tui.github.lookup.mapper;

import org.modelmapper.ModelMapper;

public abstract class AbstractMapper<I,O> implements Mapper<I,O>{
	protected final ModelMapper modelMapper;

	protected AbstractMapper(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}
}
