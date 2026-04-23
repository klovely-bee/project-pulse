const BASE = '/api/students'

async function handle(res) {
  if (!res.ok) { const e = await res.json().catch(() => ({})); throw new Error(e.message || `HTTP ${res.status}`) }
  if (res.status === 204) return null
  return res.json()
}

export const findStudents = (name = '') =>
  fetch(`${BASE}${name ? `?name=${encodeURIComponent(name)}` : ''}`).then(handle)
export const getStudent = (id) => fetch(`${BASE}/${id}`).then(handle)
export const inviteStudent = (data) =>
  fetch(BASE, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(data) }).then(handle)
export const deleteStudent = (id) => fetch(`${BASE}/${id}`, { method: 'DELETE' }).then(handle)
