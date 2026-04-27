import apiClient from '../../../shared/api/axios'

export function getUserById(id) {
  return apiClient.get(`/users/${id}`)
}

export function updateUser(id, data) {
  return apiClient.put(`/users/${id}`, data)
}
