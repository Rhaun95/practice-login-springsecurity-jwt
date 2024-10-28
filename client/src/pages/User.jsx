import React, { useState, useContext, useEffect } from "react";
import Header from "../components/Header/Header";
import UserForm from "../components/User/UserForm";
import * as auth from '../apis/auth'
import { LoginContext } from "../contexts/LoginContextProvider";
import { useNavigate } from "react-router-dom";
import Cookies from 'js-cookie';
import * as Swal from '../apis/alert';


const User = () => {

    // const [userInfo, setUserInfo] = useState()
    // const { isLogin, roles, logout } = useContext(LoginContext);
    const { isLogin, roles, logout, userInfo } = useContext(LoginContext);
    const navigate = useNavigate()

    useEffect(() => {
        if (Cookies.get("accessToken") === undefined) {
            navigate("/login")
        }

        if (userInfo?.userId) {
            getUserInfo()

        }

    }, [])

    // 회원 정보 조회
    const getUserInfo = async () => {
        if (!isLogin || !roles.isUser) {
            console.log(`isUser: ${roles.isUser}`)
            console.log(`isLogin: ${isLogin}`)
            navigate("/login")
            return
        }
        // const response = await auth.info()
        // const data = response.data
        // console.log(`getUserInfo`)
        // console.log(data)
        // setUserInfo(data)

    }


    //회원 정보 수정
    const updateUser = async (form) => {
        console.log(form)

        let response
        let data

        try {
            response = await auth.update(form)

        } catch (error) {
            console.error(`${error}`)
            console.error(`회원정보 수정 중 에러가 발생하였습니다.`)
            return
        }

        data = response.data
        const status = response.status
        console.log(`data : ${data}`)
        console.log(`status : ${status}`)

        if (status === 200) {
            // alert(`회원정보 수정 성공`)
            // logout()
            Swal.alert(`회원정보 수정 성공`, `로그아웃 후, 다시 로그인해주세요`, "success", () => logout(true))



        } else {
            alert(`회원정보 수정 실패`)

        }
    }

    //회원탈퇴
    const deleteUser = async (userId) => {
        console.log(userId)

        let response
        let data

        try {
            response = await auth.remove(userId)

        } catch (error) {
            console.error(`${error}`)
            console.error(`회원탈퇴 중 에러가 발생하였습니다.`)
            return
        }

        data = response.data
        const status = response.status
        console.log(`data : ${data}`)
        console.log(`status : ${status}`)

        if (status === 200) {
            // alert(`회원정보 삭제 성공`)
            // logout()
            Swal.alert(`회원정보 탈퇴 성공`, `그동안 감사했습니다 :)`, "success", () => logout(true))


        } else {
            alert(`회원정보 탈퇴 실패`)

        }

    }



    return (
        <>
            <Header />
            <div className="container">
                <UserForm userInfo={userInfo} isLogin={isLogin} roles={roles} updateUser={updateUser} deleteUser={deleteUser} />
            </div>

        </>
    )
}

export default User;