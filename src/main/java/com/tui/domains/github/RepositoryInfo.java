package com.tui.domains.github;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryInfo {
	private String owner;
	private List<Repository> repositories;
}
