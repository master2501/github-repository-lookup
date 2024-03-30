package com.tui.service;

import java.util.List;

import com.tui.domains.github.Branch;
import com.tui.domains.github.Repository;
import com.tui.exceptions.ApiException;

public interface GithubOperations {
	
	void checkUserExists(String username, String token) throws ApiException;

	default List<Repository> getRepositories(String username, String token) {
		return getRepositories(username, token, true);
	}
	
	List<Repository> getRepositories(String username, String token, boolean filterFork);

	List<Branch> getBranches(String username, String tokeny, String repositoryName);
}
