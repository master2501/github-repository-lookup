package com.tui.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Preconditions;
import com.tui.config.RepositoryConfig;
import com.tui.domains.github.Branch;
import com.tui.domains.github.Repository;
import com.tui.exceptions.ApiException;
import com.tui.exceptions.UserNotFoundException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GithubOperationsServiceImpl implements GithubOperationsService {
	private static final ParameterizedTypeReference<List<Repository>> REPOSITORIES_LIST = new ParameterizedTypeReference<List<Repository>>() {
	};
	private static final ParameterizedTypeReference<List<Branch>> BRANCHES_LIST = new ParameterizedTypeReference<List<Branch>>() {
	};
	private final RepositoryConfig config;
	private final RestTemplate restTemplate;

	public GithubOperationsServiceImpl(RepositoryConfig config, RestTemplate restTemplate) {
		this.config = config;
		this.restTemplate = restTemplate;
	}

	@Override
	public void checkUserExists(String username, String token) throws ApiException {
		Preconditions.checkArgument(StringUtils.hasText(username), "Invalid 'username' value parameter");
		Preconditions.checkArgument(StringUtils.hasText(token), "Invalid 'token' value parameter");

		// prepare url
		final String url = String.format(config.getUsersUrl(), username);
		log.trace("Checking username using url {} ", url);

		try {
			restTemplate.exchange(url, HttpMethod.GET, createEntity(token), String.class).getBody();
		} catch (HttpClientErrorException.NotFound e) {
			log.error("User '{}' not exists: {}", username, e.getMessage());
			throw new UserNotFoundException(String.format("User '%s' not exists", username));
		} catch (RestClientException e) {
			log.error("Error checking if the user '{}' exists due to {}", username, e.getMessage());
			throw e;
		}
	}

	@Override
	public List<Branch> getBranches(String username, String token, String repositoryName) throws ApiException {
		Preconditions.checkArgument(StringUtils.hasText(username), "Invalid 'username' value parameter");
		Preconditions.checkArgument(StringUtils.hasText(token), "Invalid 'token' value parameter");

		// prepare url
		final String url = String.format(config.getBranchesUrl(), username, repositoryName);
		log.trace("Reading branches using url {}", url);

		List<Branch> branches = null;
		try {
			HttpEntity<String> entity = createEntity(token);
			branches = restTemplate.exchange(url, HttpMethod.GET, entity, BRANCHES_LIST).getBody();
		} catch (RestClientException e) {
			log.error("Error reading the list of branches for repository '{}' due to {}", repositoryName,
					e.getMessage());
			throw e;
		}

		return branches;
	}

	@Override
	public List<Repository> getRepositories(String username, String token, boolean filterForks) throws ApiException {
		Preconditions.checkArgument(StringUtils.hasText(username), "Invalid 'username' value parameter");
		Preconditions.checkArgument(StringUtils.hasText(token), "Invalid 'token' value parameter");

		// prepare url
		final String url = config.getRepositoriesUrl();
		log.trace("Reading repositories using url {}", url);

		List<Repository> repositories = null;
		try {
			HttpEntity<String> entity = createEntity(token);
			repositories = restTemplate.exchange(url, HttpMethod.GET, entity, REPOSITORIES_LIST).getBody();
		} catch (RestClientException e) {
			log.error("Error reading the list of repositories due to {}", e.getMessage());
			throw e;
		}

		// filter forked repositories
		if (filterForks) {
			repositories.removeIf(r -> r.isFork());
		}

		return repositories;
	}

	/**
	 * Create the http entity including the authorization header
	 * 
	 * @param token the token used for authorization
	 * @return the http entity object
	 */
	private static HttpEntity<String> createEntity(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(HeaderConstants.AUTHORIZATION, HeaderConstants.BEARER + token);
		return new HttpEntity<>(headers);
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	private static class HeaderConstants {
		public static final String AUTHORIZATION = "Authorization";
		public static final String BEARER = "Bearer ";
	}
}
