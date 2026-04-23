const BASE = '/api/instructors'

async function handle(res) {
  if (!res.ok) { const e = await res.json().catch(() => ({})); throw new Error(e.message || `HTTP ${res.status}`) }
  if (res.status === 204) return null
  return res.json()
}

export const findInstructors = (name = '') =>
  fetch(`${BASE}${name ? `?name=${encodeURIComponent(name)}` : ''}`).then(handle)
export const getInstructor = (id) => fetch(`${BASE}/${id}`).then(handle)
export const inviteInstructor = (data) =>
  fetch(BASE, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(data) }).then(handle)
export const deactivateInstructor = (id) => fetch(`${BASE}/${id}/deactivate`, { method: 'PUT' }).then(handle)
export const reactivateInstructor = (id) => fetch(`${BASE}/${id}/reactivate`, { method: 'PUT' }).then(handle)
