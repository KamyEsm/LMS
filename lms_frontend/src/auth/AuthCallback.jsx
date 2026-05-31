import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { exchangeCodeForToken } from "./auth";

function AuthCallback() {
    const navigate = useNavigate();
    const [error, setError] = useState("");

    useEffect(() => {
        async function handleCallback() {
            try {
                const params = new URLSearchParams(window.location.search);
                const code = params.get("code");
                const state = params.get("state");

                const tokenData = await exchangeCodeForToken(code, state);

                localStorage.setItem("access_token", tokenData.access_token);

                if (tokenData.refresh_token) {
                    localStorage.setItem("refresh_token", tokenData.refresh_token);
                }

                navigate("/dashboard", { replace: true });
            } catch (err) {
                console.error(err);
                setError(err.message || "خطا در ورود");
            }
        }

        handleCallback();
    }, [navigate]);

    return (
        <div className="container">
            <div className="login">
                {error ? (
                    <p className="login-error">{error}</p>
                ) : (
                    <p className="login-subtitle">در حال تکمیل ورود...</p>
                )}
            </div>
        </div>
    );
}

export default AuthCallback;
