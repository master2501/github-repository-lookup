package com.tui.service;

import java.util.List;

import com.tui.domains.github.Branch;
import com.tui.domains.github.Repository;
import com.tui.exceptions.ApiException;

/**
 * Github Operations service interface
 * <p>
 * Contains all the github operations
 */
public interface GithubOperationsService {
	/**
	 * Checks if a given user exists
	 * 
	 * @param username the username
	 * @param token    the token for authentication
	 * @throws IllegalArgumentException if any of the arguments are null or empty
	 * @throws UserNotFoundException    if user not exists
	 * @throws RestClientException if an error occurs during the operation execution
	 */
	void checkUserExists(String username, String token) throws ApiException;

	/**
	 * Gets all the repositories for a given user <br>
	 * Filters the repositories, by removing the repositories that are forked, by
	 * default.
	 * 
	 * @param username the username to get the repositories
	 * @param token    the token for authentication
	 * @return the list of repositories for the specified username
	 * @throws IllegalArgumentException if any of the arguments are null or empty
	 * @throws RestClientException if an error occurs during the operation execution
	 */
	default List<Repository> getRepositories(String username, String token) throws ApiException {
		return getRepositories(username, token, true);
	}

	/**
	 * Gets all the repositories for a given user <br>
	 * Filters the repositories, by removing the repositories that are forked, by
	 * default.
	 * 
	 * @param username   the username to get the repositories
	 * @param token      the token used for authentication
	 * @param filterFork if true filters the repositories that are forked, otherwise
	 *                   returns all
	 * @return the list of repositories for the specified username
	 * @throws IllegalArgumentException if any of the arguments are null or empty
	 * @throws RestClientException if an error occurs during the operation execution
	 */
	List<Repository> getRepositories(String username, String token, boolean filterFork) throws ApiException;

	/**
	 * Gets all the branches related with a given repository
	 * 
	 * @param username       the username
	 * @param token          used for authentication
	 * @param repositoryName the repository name
	 * @return the list of branches found for the given repository name
	 * @throws IllegalArgumentException if any of the arguments are null or empty
	 * @throws RestClientException if an error occurs during the operation execution
	 */
	List<Branch> getBranches(String username, String token, String repositoryName) throws ApiException;
}
