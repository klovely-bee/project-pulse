import apiClient from '../../../shared/api/axios'

export function registerStudent(data) {
  return apiClient.post('/auth/register/student', data)
}

export function registerInstructor(data) {
  return apiClient.post('/auth/register/instructor', data)
}

export function login(data) {
  return apiClient.post('/auth/login', data)
}

export function logout() {
  return apiClient.post('/auth/logout')
}
