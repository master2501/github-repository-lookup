package com.tui.service;

import com.tui.domains.github.RepositoryInfo;
import com.tui.exceptions.ApiException;

public interface GithubLookupService {
	RepositoryInfo getRepositoryInfo(String username, String token) throws ApiException;
}
