// Backend API Configuration
const BASE_URL = process.env.VITE_API_URL || "http://localhost:8080/api";

export async function apiFetch(path, opts = {}) {
  const res = await fetch(BASE_URL + path, {
    ...opts,
    headers: {
      "Content-Type": "application/json",
      ...opts.headers,
    },
  });

  if (!res.ok) {
    let message = res.statusText;
    const contentType = res.headers.get("content-type") || "";
    if (contentType.includes("application/json")) {
      try {
        const body = await res.json();
        message = body?.message || message;
        throw new Error(message);
      } catch (error) {
        throw new Error(message);
      }
    } else {
      const text = await res.text();
      if (text) {
        message = text;
      }
      throw new Error(message);
    }
  }

  const contentType = res.headers.get("content-type") || "";
  if (!contentType.includes("application/json")) return null;
  return res.json();
}

export default { apiFetch };
