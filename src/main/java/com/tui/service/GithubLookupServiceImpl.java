package com.tui.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.common.base.Preconditions;
import com.tui.domains.github.Branch;
import com.tui.domains.github.Repository;
import com.tui.domains.github.RepositoryInfo;
import com.tui.exceptions.ApiException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GithubLookupServiceImpl implements GithubLookupService {
	private final GithubOperationsService githubOperationsService;

	public GithubLookupServiceImpl(GithubOperationsService githubOperationsService) {
		this.githubOperationsService = githubOperationsService;
	}

	@Override
	public RepositoryInfo getRepositoryInfo(String username, String token) throws ApiException {
		Preconditions.checkArgument(StringUtils.hasText(username), "Invalid 'username' value parameter");
		Preconditions.checkArgument(StringUtils.hasText(token), "Invalid 'token' value parameter");

		// check if user exists
		log.debug("Checking if user '{}' exists", username);
		githubOperationsService.checkUserExists(username, token);

		// get all repositories
		// forked repositories are filtered
		log.debug("Checking if user '{}' exists", username);
		List<Repository> repositories = githubOperationsService.getRepositories(username, token);
		Preconditions.checkState(repositories != null, "Repositories cannot be null");
		log.debug("User '{}' has '{}' repositories", username, repositories.size());

		// get branches for each repository
		log.trace("Processing branches for each repository...");
		for (Repository repository : repositories) {
			// get all the branches for a given repository
			log.debug("Retrieving branches for repository '{}'", repository.getName());
			List<Branch> branches = githubOperationsService.getBranches(username, token, repository.getName());
			log.trace("Number of branches found '{}', for repository '{}'", branches == null ? 0 : branches.size(),
					repository.getName());

			// add branches to repository
			if (branches != null) {
				repository.setBranches(branches);
			}
		}

		// returns all info related with the user
		return new RepositoryInfo(username, repositories);
	}

}
