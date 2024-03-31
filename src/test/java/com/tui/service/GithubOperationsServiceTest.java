package com.tui.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.nio.charset.Charset;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.tui.config.RepositoryConfig;
import com.tui.domains.github.Branch;
import com.tui.domains.github.Repository;
import com.tui.exceptions.ApiException;
import com.tui.exceptions.UserNotFoundException;

public class GithubOperationsServiceTest {
	private RepositoryConfig configMock;
	private RestTemplate restTemplateMock;
	private GithubOperationsService service;

	@BeforeEach
	public void setup() {
		this.configMock = mock(RepositoryConfig.class);
		this.restTemplateMock = mock(RestTemplate.class);
		this.service = spy(new GithubOperationsServiceImpl(configMock, restTemplateMock));
	}

	// checkUserExists

	@Test
	public void checkUserExists_nullUserName_ShouldThrowErrorTest() {
		assertThrows(IllegalArgumentException.class, () -> {
			service.checkUserExists(null, null);
		});
	}

	@Test
	public void checkUserExists_emptyUserName_ShouldThrowErrorTest() {
		assertThrows(IllegalArgumentException.class, () -> {
			service.checkUserExists("", null);
		});
	}

	@Test
	public void checkUserExists_nullToken_ShouldThrowErrorTest() {
		assertThrows(IllegalArgumentException.class, () -> {
			service.checkUserExists("xpto", null);
		});
	}

	@Test
	public void checkUserExists_emptyToken_ShouldThrowErrorTest() {
		assertThrows(IllegalArgumentException.class, () -> {
			service.checkUserExists("xpto", "");
		});
	}

	@Test
	public void checkUserExists_throwUserNotFound_ShouldThrowErrorTest() {
		doReturn("http://localhost/users/%s").when(configMock).getUsersUrl();

		when(restTemplateMock.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET), Mockito.any(HttpEntity.class),
				Mockito.<Class<String>>any()))
						.thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "not found", new HttpHeaders(),
								null, Charset.defaultCharset()));

		assertThrows(UserNotFoundException.class, () -> {
			service.checkUserExists("xpto", "fgd");
		});
	}

	@Test
	public void checkUserExists_throwRestClientException_ShouldThrowErrorTest() {
		doReturn("http://localhost/users/%s").when(configMock).getUsersUrl();

		when(restTemplateMock.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET), Mockito.any(HttpEntity.class),
				Mockito.<Class<String>>any())).thenThrow(new RestClientException("error making the request"));

		assertThrows(RestClientException.class, () -> {
			service.checkUserExists("xpto", "fgd");
		});
	}

	// getBranches

	@Test
	public void getBranches_nullUserName_ShouldThrowErrorTest() {
		assertThrows(IllegalArgumentException.class, () -> {
			service.getBranches(null, null, null);
		});
	}

	@Test
	public void getBranches_emptyUserName_ShouldThrowErrorTest() {
		assertThrows(IllegalArgumentException.class, () -> {
			service.getBranches("", null, null);
		});
	}

	@Test
	public void getBranches_nullToken_ShouldThrowErrorTest() {
		assertThrows(IllegalArgumentException.class, () -> {
			service.getBranches("xpto", null, null);
		});
	}

	@Test
	public void getBranches_emptyToken_ShouldThrowErrorTest() {
		assertThrows(IllegalArgumentException.class, () -> {
			service.getBranches("xpto", "", "abc");
		});
	}

	@Test
	public void getBranches_exchangeMethodThrowException_ShouldThrowErrorTest() {
		doReturn("http://localhost/branches/%s/%s").when(configMock).getBranchesUrl();

		when(restTemplateMock.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET), Mockito.any(HttpEntity.class),
				Mockito.eq(new ParameterizedTypeReference<List<Branch>>() {
				}))).thenThrow(new RestClientException("error"));

		assertThrows(RestClientException.class, () -> {
			service.getBranches("xpto", "fgd", "abc");
		});
	}

	@Test
	public void getBranches_ShouldReturnBranchesTest() throws ApiException {
		doReturn("http://localhost/branches/%s/%s").when(configMock).getBranchesUrl();
		
		@SuppressWarnings("unchecked")
		List<Branch> branchListMock = mock(List.class);
		ResponseEntity<List<Branch>> responseEntity = new ResponseEntity<List<Branch>>(branchListMock, HttpStatus.OK);

		when(restTemplateMock.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET), Mockito.any(HttpEntity.class),
				Mockito.eq(new ParameterizedTypeReference<List<Branch>>() {
				}))).thenReturn(responseEntity);

		List<Branch> response = service.getBranches("xpto", "fgd", "abc");
		assertNotNull(response);
		assertEquals(branchListMock, response);
	}

	// getRepositories

	@Test
	public void getRepositories_nullUserName_ShouldThrowErrorTest() {
		assertThrows(IllegalArgumentException.class, () -> {
			service.getRepositories(null, null);
		});
	}

	@Test
	public void getRepositories_emptyUserName_ShouldThrowErrorTest() {
		assertThrows(IllegalArgumentException.class, () -> {
			service.getRepositories("", null);
		});
	}

	@Test
	public void getRepositories_nullToken_ShouldThrowErrorTest() {
		assertThrows(IllegalArgumentException.class, () -> {
			service.getRepositories("xpto", null);
		});
	}

	@Test
	public void getRepositories_emptyToken_ShouldThrowErrorTest() {
		assertThrows(IllegalArgumentException.class, () -> {
			service.getRepositories("xpto", "");
		});
	}
	
	@Test
	public void getRepositories_exchangeMethodThrowException_ShouldThrowErrorTest() {
		doReturn("http://localhost/repositories").when(configMock).getRepositoriesUrl();

		when(restTemplateMock.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET), Mockito.any(HttpEntity.class),
				Mockito.eq(new ParameterizedTypeReference<List<Repository>>() {
				}))).thenThrow(new RestClientException("error"));

		assertThrows(RestClientException.class, () -> {
			service.getRepositories("xpto", "fgd");
		});
	}
	
	@Test
	public void getRepositories_ShouldReturnRepositoriesTest() throws ApiException {
		doReturn("http://localhost/repositories").when(configMock).getRepositoriesUrl();
		
		@SuppressWarnings("unchecked")
		List<Repository> repositoryListMock = mock(List.class);
		ResponseEntity<List<Repository>> responseEntity = new ResponseEntity<List<Repository>>(repositoryListMock, HttpStatus.OK);

		when(restTemplateMock.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET), Mockito.any(HttpEntity.class),
				Mockito.eq(new ParameterizedTypeReference<List<Repository>>() {
				}))).thenReturn(responseEntity);

		List<Repository> response = service.getRepositories("xpto", "fgd");
		assertNotNull(response);
		assertEquals(repositoryListMock, response);
	}
}
