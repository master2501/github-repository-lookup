package com.tui.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tui.domains.github.RepositoryInfo;
import com.tui.dto.RepositoryInfoDto;
import com.tui.exceptions.ApiException;
import com.tui.mapper.RepositoryInfoMapper;
import com.tui.service.GithubLookupService;

@RestController
@RequestMapping("/api/v1")
public class LookupRestController {
	private final GithubLookupService githubLookupService;
	private final RepositoryInfoMapper mapper;

	public LookupRestController(GithubLookupService githubLookupService, RepositoryInfoMapper mapper) {
		this.githubLookupService = githubLookupService;
		this.mapper = mapper;
	}

	@GetMapping(value = "repositories", produces = MediaType.APPLICATION_JSON_VALUE)
	public RepositoryInfoDto getRepositoryInfo(@RequestHeader("username") String username,
			@RequestHeader("token") String token) throws ApiException {

		RepositoryInfo repositoryInfo = githubLookupService.getRepositoryInfo(username, token);

		return mapper.convertTo(repositoryInfo);
	}
}
