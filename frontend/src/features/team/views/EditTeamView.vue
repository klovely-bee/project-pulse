<script setup>
import { onMounted, reactive, ref } from 'vue'
import { RouterLink, useRoute } from 'vue-router'
import { getTeamsBySection, updateTeam } from '../services/teamApi'

const route = useRoute()
const teamId = Number(route.params.id)
const sectionId = Number(route.query.sectionId)

const form = reactive({
  id: teamId,
  name: '',
  sectionId: '',
})

const isLoading = ref(false)
const isSubmitting = ref(false)
const successMessage = ref('')
const errorMessage = ref('')

async function loadTeam() {
  isLoading.value = true
  errorMessage.value = ''

  try {
    if (!sectionId) {
      throw new Error('Missing sectionId query parameter.')
    }

    const response = await getTeamsBySection(sectionId)
    const teams = response.data.data
    const team = teams.find((item) => item.id === teamId)

    if (!team) {
      throw new Error('Team not found.')
    }

    form.id = team.id
    form.name = team.name ?? ''
    form.sectionId = team.sectionId ?? sectionId
  } catch (error) {
    errorMessage.value = error.response?.data?.message ?? error.message ?? 'Failed to load team.'
  } finally {
    isLoading.value = false
  }
}

async function handleSubmit() {
  isSubmitting.value = true
  successMessage.value = ''
  errorMessage.value = ''

  try {
    await updateTeam({
      id: form.id,
      name: form.name,
      sectionId: Number(form.sectionId),
    })

    successMessage.value = 'Team updated successfully.'
  } catch (error) {
    errorMessage.value = error.response?.data?.message ?? 'Failed to update team.'
  } finally {
    isSubmitting.value = false
  }
}

onMounted(() => {
  loadTeam()
})
</script>

<template>
  <div class="page-container">
    <div class="card">
      <h1 class="page-title">Edit Team</h1>
      <p class="page-subtitle">Update the team details.</p>

      <p v-if="isLoading">Loading team...</p>

      <form v-else class="form" @submit.prevent="handleSubmit">
        <div class="form-group">
          <label for="team-name">Name</label>
          <input id="team-name" v-model="form.name" class="input" required />
        </div>

        <div class="form-group">
          <label for="team-section-id">Section ID</label>
          <input
            id="team-section-id"
            v-model="form.sectionId"
            class="input"
            min="1"
            required
            type="number"
          />
        </div>

        <p v-if="successMessage" class="message success">{{ successMessage }}</p>
        <p v-if="errorMessage" class="message error">{{ errorMessage }}</p>

        <div v-if="successMessage && form.sectionId" class="nav-links">
          <RouterLink class="button button-secondary" :to="`/teams/section/${form.sectionId}`">
            Back to Team List
          </RouterLink>
        </div>

        <div class="form-actions">
          <button class="button" :disabled="isSubmitting" type="submit">
            {{ isSubmitting ? 'Saving...' : 'Save Changes' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
