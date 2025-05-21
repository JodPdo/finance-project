import { useState } from "react";

function Signup({ onSignup }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [username, setUsername] = useState("");
  const [message, setMessage] = useState("");

  const handleSignup = async () => {
    setMessage("");
    try {
      const response = await fetch("http://localhost:8080/api/auth/signup", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password, username }),
      });

      if (!response.ok) throw new Error("Signup failed");

      const data = await response.json();
      const token = data.token;

      localStorage.setItem("token", token);
      onSignup(token); // ส่ง token ไป App

      setMessage("✅ สมัครสมาชิกสำเร็จ!");
    } catch (err) {
      setMessage("❌ Signup ผิดพลาด: " + err.message);
    }
  };

  return (
    <div className="p-4 max-w-sm mx-auto">
      <h2 className="text-2xl font-bold mb-4">สมัครสมาชิก</h2>

      <input
        className="border px-2 py-1 w-full mb-2"
        type="text"
        placeholder="Username"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      />

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
        className="bg-green-600 text-white px-4 py-2 rounded w-full"
        onClick={handleSignup}
      >
        Sign Up
      </button>

      {message && <p className="mt-2">{message}</p>}
    </div>
  );
}

export default Signup;
