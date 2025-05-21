import { useState } from "react";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import Dashboard from "./pages/Dashboard";

function App() {
  const [token, setToken] = useState(localStorage.getItem("token"));
  const [showSignup, setShowSignup] = useState(false);

  if (!token) {
    return (
      <div className="p-4 max-w-2xl mx-auto">
        {showSignup ? (
          <>
            <Signup onSignup={(newToken) => setToken(newToken)} />
            <p className="text-sm mt-2">
              มีบัญชีแล้ว?{" "}
              <button className="text-blue-600" onClick={() => setShowSignup(false)}>
                กลับไป Login
              </button>
            </p>
          </>
        ) : (
          <>
            <Login onLogin={(newToken) => setToken(newToken)} />
            <p className="text-sm mt-2">
              ยังไม่มีบัญชี?{" "}
              <button className="text-green-600" onClick={() => setShowSignup(true)}>
                สมัครสมาชิก
              </button>
            </p>
          </>
        )}
      </div>
    );
  }

  return <Dashboard />;
}

export default App;



