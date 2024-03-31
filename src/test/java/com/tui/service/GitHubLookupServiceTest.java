package com.tui.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClientException;

import com.tui.domains.github.Branch;
import com.tui.domains.github.Commit;
import com.tui.domains.github.Repository;
import com.tui.domains.github.RepositoryInfo;
import com.tui.exceptions.ApiException;
import com.tui.exceptions.UserNotFoundException;

public class GitHubLookupServiceTest {
	private GithubOperationsService githubOperationsServiceMock;
	private GithubLookupService githubLookupService;

	@BeforeEach
	public void setup() {
		this.githubOperationsServiceMock = mock(GithubOperationsService.class);
		this.githubLookupService = spy(new GithubLookupServiceImpl(githubOperationsServiceMock));
	}

	@Test
	public void getRepositoryInfo_nullUsername_shouldThrowErrorTest() throws ApiException {
		assertThrows(IllegalArgumentException.class, () -> {
			githubLookupService.getRepositoryInfo(null, null);
		});
	}

	@Test
	public void getRepositoryInfo_emptyUsername_shouldThrowErrorTest() throws ApiException {
		assertThrows(IllegalArgumentException.class, () -> {
			githubLookupService.getRepositoryInfo("", null);
		});
	}

	@Test
	public void getRepositoryInfo_nullToken_shouldThrowErrorTest() throws ApiException {
		assertThrows(IllegalArgumentException.class, () -> {
			githubLookupService.getRepositoryInfo("xpto", null);
		});
	}

	@Test
	public void getRepositoryInfo_emptyToken_shouldThrowErrorTest() throws ApiException {
		assertThrows(IllegalArgumentException.class, () -> {
			githubLookupService.getRepositoryInfo("xpto", "");
		});
	}

	@Test
	public void getRepositoryInfo_checkUserExistsThrowError_shouldThrowErrorTest() throws ApiException {
		doThrow(new UserNotFoundException("user not found")).when(githubOperationsServiceMock).checkUserExists(anyString(), anyString());
		
		assertThrows(UserNotFoundException.class, () -> {
			githubLookupService.getRepositoryInfo("xpto", "token123");
		});
	}
	
	@Test
	public void getRepositoryInfo_getRepositoriesThrowError_shouldThrowErrorTest() throws ApiException {
		doThrow(new RestClientException("error")).when(githubOperationsServiceMock).getRepositories(anyString(), anyString());
		
		assertThrows(RestClientException.class, () -> {
			githubLookupService.getRepositoryInfo("xpto", "token123");
		});
	}
	
	@Test
	public void getRepositoryInfo_nullRepositoryList_shouldThrowErrorTest() throws ApiException {
		doReturn(null).when(githubOperationsServiceMock).getRepositories(anyString(), anyString());
		
		assertThrows(IllegalStateException.class, () -> {
			githubLookupService.getRepositoryInfo("xpto", "token123");
		});
	}
	
	@Test
	public void getRepositoryInfo_getBranchesThrowError_shouldThrowErrorTest() throws ApiException {
		
		List<Repository> repositories = new ArrayList<>();
		repositories.add(new Repository("test1", true, null));
		
		doReturn(repositories).when(githubOperationsServiceMock).getRepositories(anyString(), anyString());
		
		doThrow(new RestClientException("error")).when(githubOperationsServiceMock).getBranches(anyString(), anyString(), anyString());
		
		assertThrows(RestClientException.class, () -> {
			githubLookupService.getRepositoryInfo("xpto", "token123");
		});
	}
	
	@Test
	public void getRepositoryInfo_nullBranches_shouldReturnRepositoryInfoWithoutBranchesTest() throws ApiException {
		
		List<Repository> repositories = new ArrayList<>();
		repositories.add(new Repository("test1", true, null));
		
		doReturn(repositories).when(githubOperationsServiceMock).getRepositories(anyString(), anyString());
		
		doReturn(null).when(githubOperationsServiceMock).getBranches(anyString(), anyString(), anyString());
		
		RepositoryInfo response = githubLookupService.getRepositoryInfo("xpto", "token123");
		
		assertNotNull(response);
		assertEquals("xpto", response.getOwner());
		assertNotNull(response.getRepositories());
		assertEquals(1, response.getRepositories().size());
		
		for (Repository repository : response.getRepositories()) {
			assertEquals("test1", repository.getName());
			assertEquals(null, repository.getBranches());
		}
	}
	
	@Test
	public void getRepositoryInfo_nonNullBranches_shouldReturnRepositoryInfoWithBranchesTest() throws ApiException {
		
		List<Repository> repositories = new ArrayList<>();
		repositories.add(new Repository("test1", true, null));
		
		doReturn(repositories).when(githubOperationsServiceMock).getRepositories(anyString(), anyString());
		
		List<Branch> branches = new ArrayList<>();
		branches.add(new Branch("master", new Commit("sha123")));
		
		doReturn(branches).when(githubOperationsServiceMock).getBranches(anyString(), anyString(), anyString());
		
		RepositoryInfo response = githubLookupService.getRepositoryInfo("xpto", "token123");
		
		assertNotNull(response);
		assertEquals("xpto", response.getOwner());
		assertNotNull(response.getRepositories());
		assertEquals(1, response.getRepositories().size());
		
		for (Repository repository : response.getRepositories()) {
			assertEquals("test1", repository.getName());
			assertNotNull(repository.getBranches());
			assertEquals(1, repository.getBranches().size());
			
			for (Branch branch : repository.getBranches()) {
				assertEquals("master", branch.getName());
				assertNotNull(branch.getCommit());
				assertEquals("sha123", branch.getCommit().getSha());
			}
		}
	}
}
