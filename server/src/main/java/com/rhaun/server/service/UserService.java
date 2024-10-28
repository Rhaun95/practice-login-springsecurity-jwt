package com.rhaun.server.service;

import com.rhaun.server.dto.Users;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    public int insert(Users user) throws Exception;

    public Users select(int userNo) throws Exception;

    public void login(Users user, HttpServletRequest request) throws Exception;

    public int update(Users user) throws Exception;

    public int delete(String userId) throws Exception;

}
