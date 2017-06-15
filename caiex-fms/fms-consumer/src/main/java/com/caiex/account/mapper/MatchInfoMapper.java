package com.caiex.account.mapper;

import org.mybatis.spring.annotation.MapperScan;

import com.caiex.account.entity.MatchInfo;
import com.caiex.account.model.MatchInfoModel;

@MapperScan
public interface MatchInfoMapper {
	//用户管理
	public MatchInfo queryByCodeWeek(MatchInfo info);
}
