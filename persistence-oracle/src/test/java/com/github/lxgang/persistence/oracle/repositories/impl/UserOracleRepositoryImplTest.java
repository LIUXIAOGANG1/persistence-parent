package com.github.lxgang.persistence.oracle.repositories.impl;

import javax.annotation.Resource;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.lxgang.persistence.oracle.repositories.UserOracleRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
public class UserOracleRepositoryImplTest {

	@Resource
	private UserOracleRepository userOracleRepository;
	
}
