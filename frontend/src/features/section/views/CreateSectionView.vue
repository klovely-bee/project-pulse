<script setup>
import { RouterLink } from 'vue-router'
import { reactive, ref } from 'vue'
import { createSection } from '../services/sectionApi'

const form = reactive({
  name: '',
  semester: '',
  year: '',
  rubricId: '',
})

const isSubmitting = ref(false)
const successMessage = ref('')
const errorMessage = ref('')
const createdSectionId = ref('')

function resetForm() {
  form.name = ''
  form.semester = ''
  form.year = ''
  form.rubricId = ''
}

async function handleSubmit() {
  isSubmitting.value = true
  successMessage.value = ''
  errorMessage.value = ''

  try {
    const payload = {
      name: form.name,
      semester: form.semester,
      year: Number(form.year),
      rubricId: Number(form.rubricId),
    }

    const response = await createSection(payload)
    successMessage.value = 'Section created successfully.'
    createdSectionId.value = response.data?.data?.id ?? ''
    resetForm()
  } catch (error) {
    errorMessage.value = error.response?.data?.message ?? 'Failed to create section.'
  } finally {
    isSubmitting.value = false
  }
}
</script>

<template>
  <div class="page-container">
    <div class="card">
      <h1 class="page-title">Create Section</h1>
      <p class="page-subtitle">Create a section and connect it to a rubric.</p>

      <div class="nav-links">
        <RouterLink class="button button-secondary" to="/sections">Back to Sections</RouterLink>
      </div>

      <form class="form" @submit.prevent="handleSubmit">
        <div class="form-group">
          <label for="section-name">Name</label>
          <input id="section-name" v-model="form.name" class="input" required />
        </div>

        <div class="form-group">
          <label for="section-semester">Semester</label>
          <input id="section-semester" v-model="form.semester" class="input" required />
        </div>

        <div class="form-group">
          <label for="section-year">Year</label>
          <input
            id="section-year"
            v-model="form.year"
            class="input"
            min="2000"
            required
            type="number"
          />
        </div>

        <div class="form-group">
          <label for="section-rubric-id">Rubric ID</label>
          <input
            id="section-rubric-id"
            v-model="form.rubricId"
            class="input"
            min="1"
            required
            type="number"
          />
        </div>

        <p v-if="successMessage" class="message success">{{ successMessage }}</p>
        <p v-if="errorMessage" class="message error">{{ errorMessage }}</p>

        <div v-if="successMessage" class="nav-links">
          <RouterLink class="button button-secondary" to="/sections">View Sections</RouterLink>
          <RouterLink
            v-if="createdSectionId"
            class="button button-secondary"
            :to="`/sections/${createdSectionId}`"
          >
            View Section Details
          </RouterLink>
        </div>

        <div class="form-actions">
          <button class="button" :disabled="isSubmitting" type="submit">
            {{ isSubmitting ? 'Creating...' : 'Create Section' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
