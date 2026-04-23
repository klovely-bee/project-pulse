import apiClient from '../../../shared/api/axios'

export function createRubric(data) {
  return apiClient.post('/rubrics', data)
}
