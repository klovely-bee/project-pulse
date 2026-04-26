import apiClient from '../../../shared/api/axios'

export async function findInstructors(name = '') {
  const response = await apiClient.get('/instructors', {
    params: name ? { name } : {},
  })

  return response.data.data
}

export async function getInstructor(id) {
  const response = await apiClient.get(`/instructors/${id}`)
  return response.data.data
}

export async function inviteInstructor(data) {
  const response = await apiClient.post('/instructors', data)
  return response.data.data
}

export async function deactivateInstructor(id) {
  const response = await apiClient.put(`/instructors/${id}/deactivate`)
  return response.data.data
}

export async function reactivateInstructor(id) {
  const response = await apiClient.put(`/instructors/${id}/reactivate`)
  return response.data.data
}
