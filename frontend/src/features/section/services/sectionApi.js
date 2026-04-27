import apiClient from '../../../shared/api/axios'

function resolveSectionId(data) {
  return data.id ?? data.sectionId
}

export function createSection(data) {
  return apiClient.post('/sections', data)
}

export function getSections() {
  return apiClient.get('/sections')
}

export function getSectionById(id) {
  return apiClient.get(`/sections/${id}`)
}

export function updateSection(data) {
  return apiClient.put('/sections', data)
}

export function setActiveWeeks(data) {
  return apiClient.post('/sections/weeks', data)
}

export function getActiveWeeks(id) {
  return apiClient.get(`/sections/${id}/weeks`)
}
