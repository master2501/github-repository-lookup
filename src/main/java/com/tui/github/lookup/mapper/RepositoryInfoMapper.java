package com.tui.github.lookup.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.tui.github.lookup.domains.github.RepositoryInfo;
import com.tui.github.lookup.dto.RepositoryInfoDto;

@Component
public class RepositoryInfoMapper extends AbstractMapper<RepositoryInfo, RepositoryInfoDto> {

	protected RepositoryInfoMapper(ModelMapper modelMapper) {
		super(modelMapper);
	}

	@Override
	public RepositoryInfoDto convertTo(RepositoryInfo repositoryInfo) {
		return modelMapper.map(repositoryInfo, RepositoryInfoDto.class);
	}

}
