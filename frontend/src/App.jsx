import { useState } from "react";

const API_BASE = "http://localhost:3000";

export default function App() {
  const [userId, setUserId] = useState("21");
  const [token, setToken] = useState("");
  const [tweets, setTweets] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [deletingId, setDeletingId] = useState(null);

  const fetchTweets = async () => {
    setError("");
    setLoading(true);
    setTweets([]);

    try {
      const res = await fetch(`${API_BASE}/tweet/findByUserId?userId=${userId}`, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!res.ok) {
        throw new Error(`HTTP ${res.status} - ${res.statusText}`);
      }

      const data = await res.json();
      // Backend bazen direkt array döner, bazen { ... } olabilir.
      // Array değilse, bir field içinden çekmeyi dene:
      const list = Array.isArray(data) ? data : (data.tweets ?? []);
      setTweets(list);
    } catch (e) {
      setError(e.message || "Bir hata oluştu");
    } finally {
      setLoading(false);
    }
  };

  const deleteTweet = async (tweetId) => {
    setError("");
    setDeletingId(tweetId);

    try {
      const res = await fetch(`${API_BASE}/tweet/${tweetId}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      // 204 No Content beklenebilir (sende Postman'de 204 gördük)
      if (!(res.status === 204 || res.ok)) {
        // bazı backend'ler hata body döner
        const maybeText = await res.text().catch(() => "");
        throw new Error(`Silme hatası: HTTP ${res.status} ${maybeText ? "- " + maybeText : ""}`);
      }

      // UI'dan kaldır
      setTweets((prev) => prev.filter((t) => t.id !== tweetId));
    } catch (e) {
      setError(e.message || "Silme sırasında hata oluştu");
    } finally {
      setDeletingId(null);
    }
  };

  return (
    <div style={styles.page}>
      <h1 style={styles.title}>Tweet List</h1>

      <div style={styles.form}>
        <label style={styles.label}>UserId:</label>
        <input
          style={styles.input}
          value={userId}
          onChange={(e) => setUserId(e.target.value)}
          placeholder="örn: 21"
        />

        <label style={styles.label}>JWT Token:</label>
        <input
          style={styles.input}
          value={token}
          onChange={(e) => setToken(e.target.value)}
          placeholder="Bearer'siz sadece token yapıştır"
        />

        <button style={styles.button} onClick={fetchTweets} disabled={loading}>
          {loading ? "Yükleniyor..." : "Tweetleri Getir"}
        </button>

        {error ? (
          <p style={styles.error}>Hata: {error}</p>
        ) : (
          <p style={styles.hint}>(Bu kısım CORS veya 401/403 göstermek için güzel 👀)</p>
        )}
      </div>

      <div style={styles.listWrap}>
        {tweets.length === 0 && !loading && !error ? (
          <p style={styles.empty}>Henüz listelenecek tweet yok.</p>
        ) : null}

        {tweets.map((t) => (
          <div key={t.id} style={styles.row}>
            <div style={styles.rowText}>
              <span style={styles.bullet}>•</span>
              <span style={styles.tweetText}>
                <strong>#{t.id}</strong> — {t.content}{" "}
                {t.user?.username ? <span style={styles.user}>(@{t.user.username})</span> : null}
              </span>
            </div>

            <button
              style={{
                ...styles.deleteBtn,
                opacity: deletingId === t.id ? 0.6 : 1,
                cursor: deletingId === t.id ? "not-allowed" : "pointer",
              }}
              onClick={() => deleteTweet(t.id)}
              disabled={deletingId === t.id}
              title="Tweeti sil"
            >
              {deletingId === t.id ? "Siliniyor..." : "Sil"}
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}

const styles = {
  page: {
    minHeight: "100vh",
    background: "#2a2a2a",
    color: "#f3f3f3",
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    padding: "64px 16px",
    gap: 24,
  },
  title: { fontSize: 56, margin: 0, fontWeight: 700 },
  form: {
    width: "min(640px, 95vw)",
    display: "flex",
    flexDirection: "column",
    gap: 10,
    alignItems: "center",
  },
  label: { alignSelf: "center", opacity: 0.9 },
  input: {
    width: "100%",
    padding: "12px 14px",
    borderRadius: 6,
    border: "1px solid #555",
    background: "#3a3a3a",
    color: "#f3f3f3",
    outline: "none",
  },
  button: {
    marginTop: 8,
    width: "100%",
    padding: "12px 14px",
    borderRadius: 8,
    border: "1px solid #666",
    background: "#111",
    color: "#fff",
    fontWeight: 700,
  },
  error: { color: "#ff4d4d", marginTop: 8 },
  hint: { color: "#ff4d4d", marginTop: 8, opacity: 0.9 },
  listWrap: {
    width: "min(760px, 95vw)",
    marginTop: 6,
    display: "flex",
    flexDirection: "column",
    gap: 10,
  },
  empty: { opacity: 0.8, textAlign: "center" },
  row: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    gap: 12,
    padding: "10px 12px",
    borderRadius: 10,
    border: "1px solid #444",
    background: "#2f2f2f",
  },
  rowText: { display: "flex", alignItems: "center", gap: 8, minWidth: 0 },
  bullet: { opacity: 0.9 },
  tweetText: { overflow: "hidden", textOverflow: "ellipsis", whiteSpace: "nowrap" },
  user: { opacity: 0.85 },
  deleteBtn: {
    padding: "8px 12px",
    borderRadius: 8,
    border: "1px solid #883a3a",
    background: "#3b1a1a",
    color: "#fff",
    fontWeight: 700,
    flexShrink: 0,
  },
};
