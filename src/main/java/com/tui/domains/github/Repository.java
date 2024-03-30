package com.tui.domains.github;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Repository {
	private String name;
	private boolean fork;
	private List<Branch> branches;
}
