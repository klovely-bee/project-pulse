import apiClient from '../../../shared/api/axios'

export function createWar(data) {
  return apiClient.post('/war', data)
}

export function getWarsByStudent(userId) {
  return apiClient.get(`/war/student/${userId}`)
}

export function getWarsByTeam(teamId) {
  return apiClient.get(`/war/team/${teamId}`)
}
