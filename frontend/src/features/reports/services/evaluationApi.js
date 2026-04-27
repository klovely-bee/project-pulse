import apiClient from '../../../shared/api/axios'

export function createEvaluation(data) {
  return apiClient.post('/evaluations', data)
}

export function getEvaluationsByStudent(id) {
  return apiClient.get(`/evaluations/student/${id}`)
}

export function getEvaluationsByTeam(teamId) {
  return apiClient.get(`/evaluations/team/${teamId}`)
}

export function getEvaluationsByTarget(id) {
  return apiClient.get(`/evaluations/target/${id}`)
}
