package com.rhaun.server.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.rhaun.server.dto.UserAuth;
import com.rhaun.server.dto.Users;


@Mapper
public interface UserMapper {
    
    public int insert(Users user) throws Exception;

    public Users select(int userNo) throws Exception;

    public Users login(String username);
   
    public int insertAuth(UserAuth userAuth) throws Exception;
   
    public int update(Users user) throws Exception;
   
    public int delete(String userId) throws Exception;

}

