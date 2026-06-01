export function generateRandomString(length = 64) {
    const charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~";
    const randomValues = new Uint8Array(length);
    window.crypto.getRandomValues(randomValues);

    let result = "";
    randomValues.forEach((value) => {
        result += charset[value % charset.length];
    });

    return result;
}

export async function sha256(plain) {
    const encoder = new TextEncoder();
    const data = encoder.encode(plain);
    return await window.crypto.subtle.digest("SHA-256", data);
}

export function base64UrlEncode(buffer) {
    const bytes = new Uint8Array(buffer);
    let binary = "";

    bytes.forEach((byte) => {
        binary += String.fromCharCode(byte);
    });

    return window
        .btoa(binary)
        .replace(/\+/g, "-")
        .replace(/\//g, "_")
        .replace(/=+$/, "");
}

export async function generateCodeChallenge(verifier) {
    const hashed = await sha256(verifier);
    return base64UrlEncode(hashed);
}

const AUTH_CONFIG = {
    clientId: "frontend-client",
    redirectUri: "http://localhost:5173/auth/callback",
    authEndpoint: "http://localhost:8081/oauth2/authorize",
    tokenEndpoint: "http://localhost:8081/oauth2/token",
    scope: "openid",
};

export async function redirectToLogin() {
    const codeVerifier = generateRandomString(64);
    const codeChallenge = await generateCodeChallenge(codeVerifier);
    const state = generateRandomString(32);
    console.log("codeVerifier:" + codeVerifier)
    console.log("codeChallenge:" + codeChallenge)
    console.log("state:" + state)

    sessionStorage.setItem("pkce_code_verifier", codeVerifier);
    sessionStorage.setItem("oauth_state", state);

    const authUrl =
        `${AUTH_CONFIG.authEndpoint}?response_type=code` +
        `&client_id=${encodeURIComponent(AUTH_CONFIG.clientId)}` +
        `&redirect_uri=${encodeURIComponent(AUTH_CONFIG.redirectUri)}` +
        `&scope=${encodeURIComponent(AUTH_CONFIG.scope)}` +
        `&state=${encodeURIComponent(state)}` +
        `&code_challenge=${encodeURIComponent(codeChallenge)}` +
        `&code_challenge_method=S256`;

    console.log("Redirecting to:", authUrl);
    window.location.href = authUrl;
}

export async function exchangeCodeForToken(code, state) {
    const savedState = sessionStorage.getItem("oauth_state");
    const codeVerifier = sessionStorage.getItem("pkce_code_verifier");
    console.log("savedState: "+savedState);
    console.log("requestState: "+state)
    console.log("savedPKCECodeVerifier: "+codeVerifier);
    if (!code) {
        throw new Error("Authorization code not found");
    }

    if (!state || state !== savedState) {
        throw new Error("Invalid state");
    }

    if (!codeVerifier) {
        throw new Error("Code verifier not found");
    }

    const body = new URLSearchParams({
        grant_type: "authorization_code",
        client_id: AUTH_CONFIG.clientId,
        code,
        redirect_uri: AUTH_CONFIG.redirectUri,
        code_verifier: codeVerifier,
    });

    const response = await fetch(AUTH_CONFIG.tokenEndpoint, {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: body.toString(),
    });

    const text = await response.text();
    console.log("Raw token response:", text);

    let data;
    try {
        data = JSON.parse(text);
    } catch {
        throw new Error("Token endpoint did not return JSON. Probably redirected to login.");
    }

    if (!response.ok) {
        throw new Error(data.error_description || data.error || "Token exchange failed");
    }


    sessionStorage.removeItem("pkce_code_verifier");
    sessionStorage.removeItem("oauth_state");

    return data;
}
