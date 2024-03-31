package com.tui.controller;

import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tui.domains.github.RepositoryInfo;
import com.tui.dto.RepositoryInfoDto;
import com.tui.exceptions.ApiException;
import com.tui.exceptions.ValidationApiException;
import com.tui.mapper.RepositoryInfoMapper;
import com.tui.service.GithubLookupService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class ApiRestController {
	private final GithubLookupService githubLookupService;
	private final RepositoryInfoMapper mapper;

	public ApiRestController(GithubLookupService githubLookupService, RepositoryInfoMapper mapper) {
		this.githubLookupService = githubLookupService;
		this.mapper = mapper;
	}

	@GetMapping(value = "repository", produces = MediaType.APPLICATION_JSON_VALUE)
	public RepositoryInfoDto getRepositoryInfo(@RequestHeader("username") String username,
			@RequestHeader("token") String token) throws ApiException {

		if (!StringUtils.hasText(username)) {
			throw new ValidationApiException("Invalid 'username' value");
		} else if (!StringUtils.hasText(token)) {
			throw new ValidationApiException("Invalid 'token' value");
		}

		log.debug("Request received to retrieve repository info for user {}", username);
		RepositoryInfo repositoryInfo = githubLookupService.getRepositoryInfo(username, token);

		return mapper.convertTo(repositoryInfo);
	}
}
