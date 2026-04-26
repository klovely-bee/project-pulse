import apiClient from '../../../shared/api/axios'

function resolveTeamId(data) {
  return data.id ?? data.teamId
}

export function createTeam(data) {
  return apiClient.post('/teams', data)
}

export function getTeamsBySection(sectionId) {
  return apiClient.get(`/teams/section/${sectionId}`)
}

export function updateTeam(data) {
  return apiClient.put(`/teams/${resolveTeamId(data)}`, data)
}

export function deleteTeam(id) {
  return apiClient.delete(`/teams/${id}`)
}

export function assignStudents(data) {
  return apiClient.post('/teams/students/assign', data)
}

export function removeStudents(data) {
  return apiClient.post('/teams/students/remove', data)
}

export function assignInstructors(data) {
  return apiClient.post('/teams/instructors/assign', data)
}

export function removeInstructors(data) {
  return apiClient.post('/teams/instructors/remove', data)
}
