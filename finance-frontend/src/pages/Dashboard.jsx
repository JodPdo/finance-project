import { useState, useEffect } from "react";

function Dashboard({ onLogout }) {
  const [transactions, setTransactions] = useState([]);
  const [summary, setSummary] = useState(null);
  const [categories, setCategories] = useState([]);
  const [error, setError] = useState("");

  const [newTransaction, setNewTransaction] = useState({
    amount: "",
    description: "",
    date: "",
    type: "income",
    categoryId: 1,
  });

  const [editTransactionId, setEditTransactionId] = useState(null);

  const getToken = () => localStorage.getItem("token");

  const checkAuth = (response) => {
    if (response.status === 403 || !getToken()) {
      localStorage.removeItem("token");
      onLogout();
      throw new Error("Session expired");
    }
  };

  useEffect(() => {
    const fetchTransactions = async () => {
      try {
        const token = getToken();
        const response = await fetch("http://localhost:8080/api/transactions", {
          headers: { Authorization: `Bearer ${token}` },
        });
        checkAuth(response);
        const data = await response.json();
        setTransactions(data);
      } catch (err) {
        setError(err.message);
      }
    };
    fetchTransactions();
  }, []);

  useEffect(() => {
    const fetchSummary = async () => {
      try {
        const token = getToken();
        const response = await fetch("http://localhost:8080/api/transactions/summary/total", {
          headers: { Authorization: `Bearer ${token}` },
        });
        checkAuth(response);
        const data = await response.json();
        setSummary(data);
      } catch (err) {
        setError("❌ Summary error: " + err.message);
      }
    };
    fetchSummary();
  }, []);

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const token = getToken();
        const response = await fetch("http://localhost:8080/api/categories", {
          headers: { Authorization: `Bearer ${token}` },
        });
        checkAuth(response);
        const data = await response.json();
        setCategories(data);
      } catch (err) {
        setError("❌ Category error: " + err.message);
      }
    };
    fetchCategories();
  }, []);

  const handleDelete = async (id) => {
    const token = getToken();
    const confirmDelete = window.confirm("คุณแน่ใจหรือไม่ว่าจะลบรายการนี้?");
    if (!confirmDelete) return;

    try {
      const response = await fetch(`http://localhost:8080/api/transactions/${id}`, {
        method: "DELETE",
        headers: { Authorization: `Bearer ${token}` },
      });
      checkAuth(response);
      if (!response.ok) throw new Error("ลบไม่สำเร็จ");
      setTransactions((prev) => prev.filter((t) => t.id !== id));
    } catch (err) {
      setError("❌ Error ลบข้อมูล: " + err.message);
    }
  };

  const handleEdit = (t) => {
    setEditTransactionId(t.id);
    setNewTransaction({
      amount: t.amount,
      description: t.description,
      date: t.date,
      type: t.type,
      categoryId: t.category.id,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const token = getToken();

    const payload = {
      amount: parseFloat(newTransaction.amount),
      description: newTransaction.description,
      date: newTransaction.date,
      type: newTransaction.type,
      category: { id: parseInt(newTransaction.categoryId) },
    };

    try {
      let response;
      if (editTransactionId) {
        response = await fetch(`http://localhost:8080/api/transactions/${editTransactionId}`, {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify(payload),
        });
      } else {
        response = await fetch("http://localhost:8080/api/transactions", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify(payload),
        });
      }

      checkAuth(response);
      const saved = await response.json();

      if (editTransactionId) {
        setTransactions((prev) => prev.map((t) => (t.id === editTransactionId ? saved : t)));
      } else {
        setTransactions((prev) => [saved, ...prev]);
      }

      setNewTransaction({ amount: "", description: "", date: "", type: "income", categoryId: 1 });
      setEditTransactionId(null);
    } catch (err) {
      setError("❌ Submit Error: " + err.message);
    }
  };

  return (
    <div className="max-w-2xl mx-auto p-6">
      <div className="flex justify-between items-center mb-4">
        <h2 className="text-2xl font-bold">📋 รายการธุรกรรม</h2>
        <button
          className="bg-red-500 text-white px-4 py-1 rounded"
          onClick={() => {
            localStorage.removeItem("token");
            onLogout();
          }}
        >
          Logout
        </button>
      </div>

      {error && <p className="text-red-500">{error}</p>}

      {summary && (
        <div className="grid grid-cols-3 gap-4 text-center my-4 font-semibold">
          <div className="bg-green-100 text-green-800 p-4 rounded-xl shadow">
            <p>✅ รายรับ</p>
            <p className="text-xl font-bold">{summary.totalIncome ?? 0} บาท</p>
          </div>
          <div className="bg-red-100 text-red-800 p-4 rounded-xl shadow">
            <p>❌ รายจ่าย</p>
            <p className="text-xl font-bold">{summary.totalExpense ?? 0} บาท</p>
          </div>
          <div className="bg-yellow-100 text-yellow-800 p-4 rounded-xl shadow">
            <p>💰 คงเหลือ</p>
            <p className="text-xl font-bold">
              {(summary.totalIncome ?? 0) - (summary.totalExpense ?? 0)} บาท
            </p>
          </div>
        </div>
      )}

      <form onSubmit={handleSubmit} className="bg-gray-100 p-4 rounded mb-6">
        <div className="grid grid-cols-2 gap-4">
          <input type="number" placeholder="จำนวนเงิน" required className="p-2 rounded border"
            value={newTransaction.amount} onChange={(e) => setNewTransaction({ ...newTransaction, amount: e.target.value })} />

          <input type="text" placeholder="คำอธิบาย" required className="p-2 rounded border"
            value={newTransaction.description} onChange={(e) => setNewTransaction({ ...newTransaction, description: e.target.value })} />

          <input type="date" required className="p-2 rounded border"
            value={newTransaction.date} onChange={(e) => setNewTransaction({ ...newTransaction, date: e.target.value })} />

          <select className="p-2 rounded border"
            value={newTransaction.type} onChange={(e) => setNewTransaction({ ...newTransaction, type: e.target.value })}>
            <option value="income">รายรับ</option>
            <option value="expense">รายจ่าย</option>
          </select>

          <select className="p-2 rounded border col-span-2"
            value={newTransaction.categoryId} onChange={(e) => setNewTransaction({ ...newTransaction, categoryId: e.target.value })}>
            {categories.map((cat) => (
              <option key={cat.id} value={cat.id}>{cat.name}</option>
            ))}
          </select>
        </div>

        <div className="mt-4">
          <button type="submit" className="bg-blue-600 text-white px-4 py-1 rounded">
            {editTransactionId ? "อัปเดตรายการ" : "เพิ่มรายการ"}
          </button>

          {editTransactionId && (
            <button
              type="button"
              className="ml-4 text-gray-600 underline"
              onClick={() => {
                setEditTransactionId(null);
                setNewTransaction({ amount: "", description: "", date: "", type: "income", categoryId: 1 });
              }}
            >
              ❌ ยกเลิก
            </button>
          )}
        </div>
      </form>

      <ul className="space-y-2">
        {transactions.map((t) => (
          <li key={t.id} className="p-3 bg-white shadow rounded">
            <div className="flex justify-between items-center">
              <div>
                <p><strong>{t.description}</strong> - {t.amount} บาท</p>
                <p className="text-sm text-gray-500">ประเภท: {t.type}</p>
              </div>
              <div className="flex gap-2">
                <button
                  className="text-blue-600 hover:underline"
                  onClick={() => handleEdit(t)}
                >
                  🗒️ แก้ไข
                </button>
                <button
                  className="text-red-600 hover:underline"
                  onClick={() => handleDelete(t.id)}
                >
                  🗑️ ลบ
                </button>
              </div>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default Dashboard;



