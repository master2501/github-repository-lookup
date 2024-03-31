package com.tui.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import com.tui.GithubApplication;
import com.tui.domains.github.Branch;
import com.tui.domains.github.Commit;
import com.tui.domains.github.Repository;
import com.tui.domains.github.RepositoryInfo;
import com.tui.exceptions.ApiException;
import com.tui.exceptions.UserNotFoundException;
import com.tui.service.GithubLookupService;

@AutoConfigureMockMvc
@SpringBootTest(classes = GithubApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiRestControllerIT {
	private static final String API_URL = "/api/v1/repository";

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private GithubLookupService githubLookupServiceMock;
	
	@Test
	public void getRepositoryInfo_acceptHeaderXml_ShouldThrowErrorTest() throws JSONException {
		String expectedJson = "{\"status\":406,\"message\":\"Could not find acceptable representation\"}";

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
		
		HttpEntity<String> entity = new HttpEntity<>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.GET, entity, String.class);

		assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
		JSONAssert.assertEquals(expectedJson, response.getBody(), false);
	}

	@Test
	public void getRepositoryInfo_requiredUsernameHeader_ShouldThrowErrorTest() throws JSONException {
		String expectedJson = "{\"status\":400,\"message\":\"Required request header 'username' for method parameter type String is not present\"}";

		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.GET, entity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		JSONAssert.assertEquals(expectedJson, response.getBody(), false);
	}

	@Test
	public void getRepositoryInfo_emptyUsernameHeader_ShouldThrowErrorTest() throws JSONException {
		String expectedJson = "{\"status\":400,\"message\":\"Invalid 'username' value\"}";

		HttpHeaders headers = new HttpHeaders();
		headers.add("username", "");
		headers.add("token", "");
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.GET, entity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		JSONAssert.assertEquals(expectedJson, response.getBody(), false);
	}

	@Test
	public void getRepositoryInfo_emptyTokenHeader_ShouldThrowErrorTest() throws JSONException {
		String expectedJson = "{\"status\":400,\"message\":\"Invalid 'token' value\"}";

		HttpHeaders headers = new HttpHeaders();
		headers.add("username", "asdasd");
		headers.add("token", "");
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.GET, entity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		JSONAssert.assertEquals(expectedJson, response.getBody(), false);
	}

	@Test
	public void getRepositoryInfo_userNotFound_ShouldThrowErrorTest() throws JSONException, ApiException {
		String expectedJson = "{\"status\":404,\"message\":\"user xpto not found\"}";

		when(githubLookupServiceMock.getRepositoryInfo(anyString(), anyString()))
				.thenThrow(new UserNotFoundException("user xpto not found"));

		HttpHeaders headers = new HttpHeaders();
		headers.add("username", "user123");
		headers.add("token", "token123");
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.GET, entity, String.class);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		JSONAssert.assertEquals(expectedJson, response.getBody(), false);
	}
	
	@Test
	public void getRepositoryInfo_serviceCallThrowError_ShouldThrowErrorTest() throws JSONException, ApiException {
		String expectedJson = "{\"status\":500,\"message\":\"error requesting data to github\"}";

		when(githubLookupServiceMock.getRepositoryInfo(anyString(), anyString()))
				.thenThrow(new RestClientException("error requesting data to github"));

		HttpHeaders headers = new HttpHeaders();
		headers.add("username", "user123");
		headers.add("token", "token123");
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.GET, entity, String.class);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		JSONAssert.assertEquals(expectedJson, response.getBody(), false);
	}

	@Test
	public void getRepositoryInfo_serviceCallReturnObject_ShouldReturnResponseTest() throws JSONException, ApiException {
		String expectedJson = "{\"owner\":\"user123\",\"repositories\":[{\"name\":\"repo1\",\"branches\":[{\"name\":\"master\",\"lastCommit\":\"sha123\"}]}]}";

		List<Branch> branchs = new ArrayList<>();
		branchs.add(new Branch("master", new Commit("sha123")));

		List<Repository> repositories = new ArrayList<>();
		repositories.add(new Repository("repo1", false, branchs));

		RepositoryInfo payload = RepositoryInfo.builder() //
				.owner("user123") //
				.repositories(repositories) //
				.build();

		when(githubLookupServiceMock.getRepositoryInfo(anyString(), anyString())) //
				.thenReturn(payload);

		HttpHeaders headers = new HttpHeaders();
		headers.add("username", "user123");
		headers.add("token", "token123");
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.GET, entity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expectedJson, response.getBody(), false);
	}
}