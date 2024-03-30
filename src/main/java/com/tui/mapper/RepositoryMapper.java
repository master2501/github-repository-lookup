package com.tui.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.tui.domains.github.Repository;
import com.tui.dto.RepositoryDto;

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
