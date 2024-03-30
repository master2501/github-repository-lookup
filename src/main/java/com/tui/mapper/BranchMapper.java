package com.tui.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.tui.domains.github.Branch;
import com.tui.dto.BranchDto;

@Component
public class BranchMapper extends AbstractMapper<Branch, BranchDto> {

	public BranchMapper(ModelMapper modelMapper) {
		super(modelMapper);
		modelMapper.typeMap(Branch.class, BranchDto.class).addMappings(mapper -> {
			mapper.map(src -> src.getCommit().getSha(), BranchDto::setLastCommit);
		});
	}

	@Override
	public BranchDto convertTo(Branch branch) {
		return modelMapper.map(branch, BranchDto.class);
	}
}
