package com.tui.service;

import com.tui.domains.github.RepositoryInfo;
import com.tui.exceptions.ApiException;

/**
 * Github lookup service interface<br>
 * Contain all the operations needed to retrieve all the github information
 * related with a given user
 */
public interface GithubLookupService {
	/**
	 * Gets all github information for the specified username
	 * 
	 * @param username the username
	 * @param token    the token
	 * @return the repository info with all the data found
	 * @throws IllegalArgumentException if any of the arguments are null or empty
	 * @throws ApiException             if an error occurs
	 */
	RepositoryInfo getRepositoryInfo(String username, String token) throws ApiException;
}
