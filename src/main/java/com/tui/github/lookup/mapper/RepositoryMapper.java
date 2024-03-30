package com.tui.github.lookup.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.tui.github.lookup.domains.github.Repository;
import com.tui.github.lookup.dto.RepositoryDto;

@Component
public class RepositoryMapper extends AbstractMapper<Repository, RepositoryDto> {

	public RepositoryMapper(ModelMapper mapper) {
		super(mapper);
	}

	@Override
	public RepositoryDto convertTo(Repository repository) {
		return modelMapper.map(repository, RepositoryDto.class);
	}
}
