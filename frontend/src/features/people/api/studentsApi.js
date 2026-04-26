import apiClient from '../../../shared/api/axios'

export async function findStudents(name = '') {
  const response = await apiClient.get('/students', {
    params: name ? { name } : {},
  })

  return response.data.data
}

export async function getStudent(id) {
  const response = await apiClient.get(`/students/${id}`)
  return response.data.data
}

export async function inviteStudent(data) {
  const response = await apiClient.post('/students', data)
  return response.data.data
}

export async function deleteStudent(id) {
  const response = await apiClient.delete(`/students/${id}`)
  return response.data.data
}
