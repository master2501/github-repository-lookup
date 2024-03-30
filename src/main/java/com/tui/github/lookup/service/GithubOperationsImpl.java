package com.tui.github.lookup.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.tui.github.lookup.config.RepositoryConfig;
import com.tui.github.lookup.domains.github.Branch;
import com.tui.github.lookup.domains.github.Repository;
import com.tui.github.lookup.exceptions.ApiException;
import com.tui.github.lookup.exceptions.UserNotFoundException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GithubOperationsImpl implements GithubOperations {
	private static final ParameterizedTypeReference<List<Repository>> REPOSITORIES_LIST = new ParameterizedTypeReference<List<Repository>>() {
	};
	private static final ParameterizedTypeReference<List<Branch>> BRANCHES_LIST = new ParameterizedTypeReference<List<Branch>>() {
	};
	private final RepositoryConfig config;
	private final RestTemplate restTemplate;

	public GithubOperationsImpl(RepositoryConfig config, RestTemplate restTemplate) {
		this.config = config;
		this.restTemplate = restTemplate;
	}

	@Override
	public void checkUserExists(String username, String token) throws ApiException {

		final String url = String.format(config.getUsersUrl(), username);
		log.debug("Checking username using {} ", url);

		try {
			String response = restTemplate.exchange(url, HttpMethod.GET, createEntity(token), String.class).getBody();
			System.err.println(response);
		} catch (HttpClientErrorException.NotFound e) {
			log.error("User '{}' not exists: {}", username, e.getMessage());
			throw new UserNotFoundException(String.format("User '%s' not exists", username));
		}
	}

	@Override
	public List<Branch> getBranches(String username, String token, String repositoryName) {

		final String url = String.format(config.getBranchesUrl(), username, repositoryName);
		log.debug("Reading branches from {}", url);

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
	public List<Repository> getRepositories(String username, String token, boolean filterForks) {

		final String url = config.getRepositoriesUrl();
		log.debug("Reading repositories from {}", url);

		List<Repository> repositories = null;
		try {
			HttpEntity<String> entity = createEntity(token);
			repositories = restTemplate.exchange(url, HttpMethod.GET, entity, REPOSITORIES_LIST).getBody();
		} catch (RestClientException e) {
			log.error("Error reading the list of repositories due to {}", e.getMessage());
			throw e;
		}

		if (filterForks) {
			repositories.removeIf(r -> r.isFork());
		}

		return repositories;
	}

	protected static HttpEntity<String> createEntity(String token) {
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
