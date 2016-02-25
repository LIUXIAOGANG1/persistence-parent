package com.github.lxgang.persistence.oracle.pageable;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class OraclePaginationHelper<T> implements PaginationHelper<T> {
	private Logger logger = LoggerFactory.getLogger(OraclePaginationHelper.class); 
	
	@Override
	public Page<T> fetchPage(JdbcTemplate jdbcTemplate, String sqlCountRows, String sqlPrefix, String sqlInner,
			String sqlSuffix, Object[] args, Pageable pageable, RowMapper<T> rowMapper) {
		
		PageImpl<T> page = null;
		String fetchSQL = withPagination(sqlPrefix, sqlInner, sqlSuffix, pageable);
		
		logger.debug("Count SQL: {}", sqlCountRows);
		logger.debug("Fetch SQL: {}", fetchSQL);
		
        final long rowCount = jdbcTemplate.queryForObject(sqlCountRows, args, Long.class);
        List<T> content = jdbcTemplate.query(fetchSQL, args, rowMapper);
        
        page = new PageImpl<T>(content, pageable, rowCount);
		
		return page;
	}
	
	public String withPagination(String sqlPrefix, String sqlInner,
			String sqlSuffix, Pageable pageable){
		if(pageable == null) return sqlPrefix.concat(sqlInner).concat(sqlSuffix);
		
		int offset = pageable.getOffset();
		int pageSize = pageable.getPageSize();
		sqlSuffix += String.format(" AND RN>= %d AND RN<= %d", offset, pageSize);
		
		Sort sort = pageable.getSort();
		// 使用排序
		if(sort != null){
			Iterator<Order> it = sort.iterator();
			
			boolean hasKeyword = false;
			while(it.hasNext()){
				Order order = it.next();
				if(!hasKeyword){
					sqlInner += String.format(" ORDER BY %s %s", order.getProperty(), order.getDirection().toString());
					hasKeyword = true;
				}else{
					sqlInner += String.format(", %s %s", order.getProperty(), order.getDirection().toString());
				}
			}
		}
		
		return sqlPrefix.concat(sqlInner).concat(sqlSuffix);
	}
}
