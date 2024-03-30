package com.tui.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryInfoDto {
	private String owner;
	private List<RepositoryDto> repositories;
}
