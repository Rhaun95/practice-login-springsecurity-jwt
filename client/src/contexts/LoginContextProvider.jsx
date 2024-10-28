import React, { createContext, useEffect, useState } from 'react'
import api from '../apis/api';
import Cookies from 'js-cookie';
import *  as auth from '../apis/auth';
import { useNavigate } from 'react-router-dom';
import * as Swal from '../apis/alert';

export const LoginContext = createContext();
LoginContext.displayName = 'LoginContextName'


/**
 * 로그인 체크
 * 로그인 
 * 로그아웃
 * 
 */
const LoginContextProvider = ({ children }) => {

    //context value :로그인 여부, 로그아웃 함수
    const [isLogin, setLogin] = useState(false);

    // User Info
    const [userInfo, setUserInfo] = useState({})
    const [roles, setRoles] = useState({ isUser: false, isAdmin: false })

    const [remberUserId, setRemberUserId] = useState()
    const navigate = useNavigate();


    const login = async (username, password) => {
        console.log(`username: ${username}`)
        console.log(`password: ${password}`)


        try {
            const response = await auth.login(username, password)

            const data = response.data
            const status = response.status
            const headers = response.headers
            const authorization = headers.authorization
            const accessToken = authorization.replace("Bearer ", "")

            console.log(`data : ${data}`)
            console.log(`status : ${status}`)
            console.log(`headers : ${headers}`)
            console.log(`jwt : ${accessToken}`)

            // Login Successful
            if (status == 200) {

                // 쿠키에 accessToken(jwt) 저장
                Cookies.set("accessToken", accessToken)

                loginCheck()

                Swal.alert(`로그인 성공`, `메인 화면으로 갑니다`, "success", () => navigate("/"))

            }

        } catch (error) {
            Swal.alert(`로그인 실패`, `메인 화면으로 갑니다`, "error")
        }
    }

    /* Check Login
        - check the jwt in Cookie
        - request the userinfo with jwt
    */
    const loginCheck = async () => {

        const accessToken = Cookies.get("accessToken")
        console.log(`asccessToken : ${accessToken}`)

        // Put Jwt in header
        if (!accessToken) {
            console.log(`No AccessToken in Coockie`)
            logoutSetting()
            return
        }

        api.defaults.headers.common.Authorization = `Bearer ${accessToken}`

        let response
        let data

        try {
            response = await auth.info()
        } catch (error) {
            console.log(`error : ${error}`)
            console.log(`status : ${response.status}`)
            return;
        }

        data = response.data
        console.log(`data : ${data}`)


        if (data === 'UNAUTHORIZED' || response.status === 401) {
            console.log(`accessToken (jwt)이 만료되었거나 인증에 실패하셨습니다.`);
            return
        }

        //인증 성공
        console.log(`accessToken (jwt) 토큰으로 사용자 인증정보 요청 성공!`)

        //로그인 세팅
        loginSetting(data, accessToken)
    };

    //Login Setting
    const loginSetting = async (userData, accessToken) => {
        const { no, userId, userPw, name, email, authList } = userData
        const roleList = authList.map((auth) => auth.auth)

        console.log(`no: ${no}`)
        console.log(`userId: ${userId}`)
        console.log(`userPw: ${userPw}`)
        console.log(`name: ${name}`)
        console.log(`email: ${email}`)
        console.log(`authList: ${authList}`)
        console.log(`roleList: ${roleList}`)

        // axios 객체의 header(Authorization : `Bearer ${accessToken}`)
        api.defaults.headers.common.Authorization = `Bearer ${accessToken}`

        // 로그인 여부 : true
        setLogin(true)

        // 유저정보 세팅
        const updatedUserInfo = { no, userId, userPw, name, email, roleList }
        setUserInfo(updatedUserInfo)

        //권한정보 세팅
        const updatedRoles = { isUser: false, isAdmin: false }

        roleList.forEach((role) => {
            if (role === 'ROLE_USER') updatedRoles.isUser = true
            if (role === 'ROLE_ADMIN') updatedRoles.isAdmin = true
        })

        setRoles(updatedRoles)
    }

    // Logout
    const logout = (force = false) => {


        if (force) {
            logoutSetting()
            navigate("/")
            return
        }

        Swal.confirm(`로그아웃 하시겠습니까`, `로그아웃을 진행합니다`, "warning", (result) => {
            if (result.isConfirmed) {
                logoutSetting()
                navigate("/")
            }
        })

        // const check = window.confirm(`로그아웃 하시겠습니까?`)
        // if (check) {
        //     logoutSetting()
        //     navigate("/")
        // }

    }


    //로그아웃 세팅
    const logoutSetting = () => {
        // 헤더 초기화
        api.defaults.headers.common.Authorization = undefined;

        // 쿠키 초기화
        Cookies.remove("accessToken")

        //로그인 여부: false
        setLogin(false)

        // 유저정보 초기화
        setUserInfo(null)

        // 권한 정보 초기화
        setRoles(null)

    }


    useEffect(() => {
        loginCheck()

    }, [])

    return (
        <div>
            <LoginContext.Provider value={{ isLogin, userInfo, roles, login, logout }}>
                {children}
            </LoginContext.Provider>

        </div>
    )
}

export default LoginContextProvider
