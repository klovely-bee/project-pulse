<script setup>
import { RouterLink, useRoute } from 'vue-router'
import { reactive, ref } from 'vue'
import { createTeam } from '../services/teamApi'

const route = useRoute()
const initialSectionId = route.query.sectionId ?? ''

const form = reactive({
  name: '',
  sectionId: initialSectionId,
})

const isSubmitting = ref(false)
const successMessage = ref('')
const errorMessage = ref('')
const createdSectionId = ref(initialSectionId)

function resetForm() {
  form.name = ''
}

async function handleSubmit() {
  isSubmitting.value = true
  successMessage.value = ''
  errorMessage.value = ''

  try {
    const sectionId = Number(form.sectionId)

    await createTeam({
      name: form.name,
      sectionId,
    })

    successMessage.value = 'Team created successfully.'
    createdSectionId.value = String(sectionId)
    resetForm()
  } catch (error) {
    errorMessage.value = error.response?.data?.message ?? 'Failed to create team.'
  } finally {
    isSubmitting.value = false
  }
}
</script>

<template>
  <div class="page-container">
    <div class="card">
      <h1 class="page-title">Create Team</h1>
      <p class="page-subtitle">Create a team and attach it to a section.</p>

      <div class="nav-links">
        <RouterLink class="button button-secondary" to="/sections">Back to Sections</RouterLink>
        <RouterLink
          v-if="form.sectionId || createdSectionId"
          class="button button-secondary"
          :to="`/teams/section/${form.sectionId || createdSectionId}`"
        >
          Back to Teams
        </RouterLink>
      </div>

      <form class="form" @submit.prevent="handleSubmit">
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

        <div v-if="successMessage && createdSectionId" class="nav-links">
          <RouterLink class="button button-secondary" :to="`/teams/section/${createdSectionId}`">
            View Teams for This Section
          </RouterLink>
        </div>

        <div class="form-actions">
          <button class="button" :disabled="isSubmitting" type="submit">
            {{ isSubmitting ? 'Creating...' : 'Create Team' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
