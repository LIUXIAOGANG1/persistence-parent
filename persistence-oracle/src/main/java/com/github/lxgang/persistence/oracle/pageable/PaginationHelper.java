package com.github.lxgang.persistence.oracle.pageable;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public interface PaginationHelper<T> {
	public Page<T> fetchPage(final JdbcTemplate jdbcTemplate, final String sqlCountRows, final String sqlFetchRows, final Object args[], final Pageable pageable, final RowMapper<T> rowMapper);
	
	public Page<T> fetchPage(final NamedParameterJdbcTemplate jdbcTemplate, String sqlCountRows, String sqlFetchRows, Map<String, Object> params, Pageable pageable, RowMapper<T> rowMapper);
	
	public String withPagination(String sql, Pageable pageable);
}
