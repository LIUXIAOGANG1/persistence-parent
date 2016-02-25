package com.github.lxgang.persistence.oracle.pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public interface PaginationHelper<T> {
	public Page<T> fetchPage(final JdbcTemplate jdbcTemplate, final String sqlCountRows, final String sqlPrefix, final String sqlInner, final String sqlSuffix, final Object args[], final Pageable pageable, final RowMapper<T> rowMapper);
}
