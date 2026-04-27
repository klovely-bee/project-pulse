<script setup>
import { onMounted, reactive, ref } from 'vue'
import { RouterLink, useRoute } from 'vue-router'
import { getSectionById, updateSection } from '../services/sectionApi'

const route = useRoute()
const sectionId = route.params.id

const form = reactive({
  id: sectionId,
  name: '',
  semester: '',
  year: '',
  rubricId: '',
})

const isLoading = ref(false)
const isSubmitting = ref(false)
const successMessage = ref('')
const errorMessage = ref('')

async function loadSection() {
  isLoading.value = true
  errorMessage.value = ''

  try {
    const response = await getSectionById(sectionId)
    const section = response.data.data

    form.id = section.id
    form.name = section.name ?? ''
    form.semester = section.semester ?? ''
    form.year = section.year ?? ''
    form.rubricId = section.rubricId ?? ''
  } catch (error) {
    errorMessage.value = error.response?.data?.message ?? 'Failed to load section.'
  } finally {
    isLoading.value = false
  }
}

async function handleSubmit() {
  isSubmitting.value = true
  successMessage.value = ''
  errorMessage.value = ''

  try {
    const payload = {
      id: form.id,
      name: form.name,
      semester: form.semester,
      year: Number(form.year),
      rubricId: Number(form.rubricId),
    }

    await updateSection(payload)
    successMessage.value = 'Section updated successfully.'
  } catch (error) {
    errorMessage.value = error.response?.data?.message ?? 'Failed to update section.'
  } finally {
    isSubmitting.value = false
  }
}

onMounted(() => {
  loadSection()
})
</script>

<template>
  <div class="page-container">
    <div class="card">
      <h1 class="page-title">Edit Section</h1>
      <p class="page-subtitle">Update the section details.</p>

      <p v-if="isLoading">Loading section...</p>

      <form v-else class="form" @submit.prevent="handleSubmit">
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
          <RouterLink class="button button-secondary" :to="`/sections/${sectionId}`">
            Back to Section Details
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
