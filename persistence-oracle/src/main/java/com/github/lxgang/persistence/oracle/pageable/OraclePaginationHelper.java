package com.github.lxgang.persistence.oracle.pageable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class OraclePaginationHelper<T> implements PaginationHelper<T> {
	private Logger logger = LoggerFactory.getLogger(OraclePaginationHelper.class); 
	
	@Override
	public Page<T> fetchPage(
			final JdbcTemplate jdbcTemplate, 
			final String sqlCountRows, 
			final String sqlFetchRows, 
			final Object args[], 
			final Pageable pageable,
			final RowMapper<T> rowMapper) {
		
		PageImpl<T> page = null;
		String fetchSQL = withPagination(sqlFetchRows, pageable);
		
		logger.debug("Count SQL: {}", sqlCountRows);
		logger.debug("Fetch SQL: {}", fetchSQL);
		
        final long rowCount = jdbcTemplate.queryForObject(sqlCountRows, args, Long.class);
        List<T> content = jdbcTemplate.query(fetchSQL, args, rowMapper);
        
        page = new PageImpl<T>(content, pageable, rowCount);
		
		
		return page;
	}
	
	@Override
	public Page<T> fetchPage(
			final NamedParameterJdbcTemplate jdbcTemplate, 
			final String sqlCountRows, 
			final String sqlFetchRows, 
			final Map<String, Object> params, 
			final Pageable pageable,
			final RowMapper<T> rowMapper) {
		
		PageImpl<T> page = null;
		String fetchSQL = withPagination(sqlFetchRows, pageable);
		
		logger.debug("Count SQL: {}", sqlCountRows);
		logger.debug("Fetch SQL: {}", fetchSQL);
		
        final long rowCount = jdbcTemplate.queryForObject(sqlCountRows, params, Long.class);
        
        List<T> content = jdbcTemplate.query(fetchSQL, params, rowMapper);
        
        page = new PageImpl<T>(content, pageable, rowCount);
		
		
		return page;
	}
	
	public String withPagination(String sql, Pageable pageable){
		if(pageable == null) return sql;
		
		int offset = pageable.getOffset();
		int pageSize = pageable.getPageSize();
		sql += String.format("AND RN> %d AND RN<= %d", offset, pageSize);
		
		Sort sort = pageable.getSort();
		
		// 使用排序
		if(sort != null){
			Iterator<Order> it = sort.iterator();
			
			boolean hasKeyword = false;
			while(it.hasNext()){
				Order order = it.next();
				if(!hasKeyword){
					sql += String.format(" ORDER BY %s %s", order.getProperty(), order.getDirection().toString());
					hasKeyword = true;
				}else{
					sql += String.format(", %s %s", order.getProperty(), order.getDirection().toString());
				}
			}
		}
		
		return sql;
	}
}
