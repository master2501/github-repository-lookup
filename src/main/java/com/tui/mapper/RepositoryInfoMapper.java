package com.tui.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.tui.domains.github.RepositoryInfo;
import com.tui.dto.RepositoryInfoDto;

@Component
public class RepositoryInfoMapper extends AbstractMapper<RepositoryInfo, RepositoryInfoDto> {

	public RepositoryInfoMapper(ModelMapper modelMapper) {
		super(modelMapper);
	}

	@Override
	public RepositoryInfoDto convertTo(RepositoryInfo repositoryInfo) {
		return modelMapper.map(repositoryInfo, RepositoryInfoDto.class);
	}
}
