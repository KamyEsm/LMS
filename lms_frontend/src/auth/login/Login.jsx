// import { useState } from 'react'
import './login.css'
import {redirectToLogin} from "../auth.js";

export default function Login() {

    const handleLogin = async (e) => {
        try {
            e.preventDefault();
            await redirectToLogin();
        } catch (error) {
            console.error("Login redirect error:", error);
        }
    };

    return (
        <>
            <div className="container">
                <form className="login" onSubmit={handleLogin}>
                    <h1 className="login-title">login</h1>
                    <p className="login-subtitle">Log in to your account to continue.</p>
                    <button className="login-button" type="submit">login</button>
                </form>
            </div>

        </>
    )
}

