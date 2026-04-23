<script setup>
import { onMounted, ref } from 'vue'
import { RouterLink, useRoute } from 'vue-router'
import { deleteTeam, getTeamsBySection } from '../services/teamApi'

const route = useRoute()
const sectionId = route.params.sectionId

const teams = ref([])
const isLoading = ref(false)
const successMessage = ref('')
const errorMessage = ref('')

async function loadTeams() {
  isLoading.value = true
  errorMessage.value = ''

  try {
    const response = await getTeamsBySection(sectionId)
    teams.value = response.data.data
  } catch (error) {
    errorMessage.value = error.response?.data?.message ?? 'Failed to load teams.'
  } finally {
    isLoading.value = false
  }
}

async function handleDelete(teamId) {
  successMessage.value = ''
  errorMessage.value = ''

  try {
    await deleteTeam(teamId)
    successMessage.value = 'Team deleted successfully.'
    await loadTeams()
  } catch (error) {
    errorMessage.value = error.response?.data?.message ?? 'Failed to delete team.'
  }
}

onMounted(() => {
  loadTeams()
})
</script>

<template>
  <div class="page-container">
    <div class="card">
      <div class="section-header">
        <div>
          <h1 class="page-title">Teams</h1>
          <p class="page-subtitle">Teams for section {{ sectionId }}.</p>
        </div>
      </div>

      <div class="nav-links">
        <RouterLink class="button button-secondary" :to="`/sections/${sectionId}`">
          Back to Section Details
        </RouterLink>
        <RouterLink class="button" :to="`/teams/create?sectionId=${sectionId}`">Create Team</RouterLink>
      </div>

      <p v-if="successMessage" class="message success">{{ successMessage }}</p>
      <p v-if="errorMessage" class="message error">{{ errorMessage }}</p>

      <p v-if="isLoading">Loading teams...</p>
      <p v-else-if="teams.length === 0">No teams found for this section.</p>

      <div v-else class="list">
        <div v-for="team in teams" :key="team.id" class="list-item">
          <h2 class="list-item-title">{{ team.name }}</h2>

          <div class="form-actions">
            <RouterLink class="button" :to="`/teams/${team.id}/edit?sectionId=${sectionId}`">
              Edit
            </RouterLink>
            <button class="button button-secondary" type="button" @click="handleDelete(team.id)">
              Delete
            </button>
            <RouterLink class="button" :to="`/teams/${team.id}/assignments?sectionId=${sectionId}`">
              Assignments
            </RouterLink>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
