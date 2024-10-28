import React from "react";
import Header from "../components/Header/Header";
import JoinForm from "../components/Join/JoinForm";
import * as auth from '../apis/auth'
import { useNavigate } from "react-router-dom";
import * as Swal from '../apis/alert';



const Join = () => {
    const navigate = useNavigate()

    const join = async (form) => {
        console.log(form)

        let response
        let data

        try {
            response = await auth.join(form)
        } catch (error) {
            console.error(`${error}`)
            console.error(`회원가입 요청 중 에러가 발생했습니다.`)
            return
        }

        data = response.data
        const status = response.status
        console.log(`data : ${data}`)
        console.log(`status : ${status}`)

        if (status == 200) {
            console.log(`회원가입 성공!`)
            Swal.alert(`회원가입 성공`, `로그인 화면으로 갑니다`, "success", () => navigate("/login"))
            navigate("/login")
        } else {
            Swal.alert(`회원가입 실패`, `회원가입에 실패하셨습니다`, "error")

            console.log(`회원가입 실패!`)
            alert(`Join failed!`)
        }
    }

    return (
        <>
            <Header />
            <div className="container">
                <JoinForm join={join} />

            </div>

        </>
    )
}

export default Join;