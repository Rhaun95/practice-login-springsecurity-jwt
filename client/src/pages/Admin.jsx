import React, { useContext, useEffect } from "react";
import Header from "../components/Header/Header";
import LoginContextConsumer from "../contexts/LoginContextConsumer";
import { LoginContext } from "../contexts/LoginContextProvider";
import { useNavigate } from "react-router-dom";

const Admin = () => {

    const { isLogin, roles, userInfo } = useContext(LoginContext)
    const navigate = useNavigate()

    useEffect(() => {

        if (!userInfo || !isLogin) {
            alert(`로그인이 필요합니다`)
            navigate("/login")
            return

        }
        if (!roles.isAdmin) {
            alert(`권환이 필요합니다`)
            navigate(-1)
            return
        }
    }, [])


    return (
        <>
            <Header />

            {
                isLogin && roles.isAdmin &&
                <>
                    <div className="container">
                        <h1>Admin</h1>
                        <hr />
                        <h2>관리자 페이지</h2>
                        <center>
                            <img src={process.env.PUBLIC_URL + '/img/loading.gif'} alt="admin" className="admin-logo" />
                        </center>
                    </div>


                </>

            }

        </>
    )
}

export default Admin;