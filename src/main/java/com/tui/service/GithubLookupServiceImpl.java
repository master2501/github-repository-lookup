package com.tui.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tui.domains.github.Branch;
import com.tui.domains.github.Repository;
import com.tui.domains.github.RepositoryInfo;
import com.tui.exceptions.ApiException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GithubLookupServiceImpl implements GithubLookupService {

	@Autowired
	private GithubOperations githubOperations;

	@Override
	public RepositoryInfo getRepositoryInfo(String username, String token) throws ApiException {
		
		// check if user exists
		githubOperations.checkUserExists(username, token);
		
		// get all repositories 
		// forked repositories are filtered
		List<Repository> repositories = githubOperations.getRepositories(username, token);
		
		// get branches for each repository
		for (Repository repository : repositories) {
			// get all the branches for a given repository
			List<Branch> branches = githubOperations.getBranches(username, token, repository.getName());
			repository.setBranches(branches);
		}
		
		return new RepositoryInfo(username, repositories);
	}

}
