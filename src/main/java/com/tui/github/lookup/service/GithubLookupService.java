package com.tui.github.lookup.service;

import com.tui.github.lookup.domains.github.RepositoryInfo;
import com.tui.github.lookup.exceptions.ApiException;

public interface GithubLookupService {
	RepositoryInfo getRepositoryInfo(String username, String token) throws ApiException;
}
