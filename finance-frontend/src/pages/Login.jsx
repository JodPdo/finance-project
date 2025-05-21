import { useState } from "react";

function Login({ onLogin }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const handleLogin = async () => {
  setMessage("");
  try {
    const response = await fetch("http://localhost:8080/api/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ email, password }),
    });

    if (!response.ok) {
      throw new Error("Login failed");
    }

    const data = await response.json();
    const token = data.token;

    localStorage.setItem("token", token);
    onLogin(token);

    setMessage("✅ Login สำเร็จ!");

    // ✅ redirect ไปหน้า dashboard
    window.location.href = "/dashboard";
  } catch (err) {
    setMessage("❌ Login ผิดพลาด: " + err.message);
  }
};


  return (
    <div className="p-4 max-w-sm mx-auto">
      <h2 className="text-2xl font-bold mb-4">เข้าสู่ระบบ</h2>

      <input
        className="border px-2 py-1 w-full mb-2"
        type="email"
        placeholder="Email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />

      <input
        className="border px-2 py-1 w-full mb-2"
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />

      <button
        className="bg-blue-500 text-white px-4 py-2 rounded w-full"
        onClick={handleLogin}
      >
        Login
      </button>

      {message && <p className="mt-2">{message}</p>}
    </div>
  );
}

export default Login;
