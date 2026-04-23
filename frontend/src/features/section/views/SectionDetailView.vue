<script setup>
import { onMounted, ref } from 'vue'
import { RouterLink, useRoute } from 'vue-router'
import { getSectionById } from '../services/sectionApi'

const route = useRoute()
const sectionId = route.params.id

const section = ref(null)
const isLoading = ref(false)
const errorMessage = ref('')

async function loadSection() {
  isLoading.value = true
  errorMessage.value = ''

  try {
    const response = await getSectionById(sectionId)
    section.value = response.data.data
  } catch (error) {
    errorMessage.value = error.response?.data?.message ?? 'Failed to load section.'
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  loadSection()
})
</script>

<template>
  <div class="page-container">
    <div class="card">
      <h1 class="page-title">Section Details</h1>
      <p class="page-subtitle">View section information and section actions.</p>

      <p v-if="isLoading">Loading section...</p>
      <p v-else-if="errorMessage" class="message error">{{ errorMessage }}</p>

      <div v-else-if="section" class="details">
        <p><strong>Name:</strong> {{ section.name }}</p>
        <p><strong>Semester:</strong> {{ section.semester }}</p>
        <p><strong>Year:</strong> {{ section.year }}</p>

        <div class="nav-links">
          <RouterLink class="button" :to="`/sections/${sectionId}/edit`">Edit Section</RouterLink>
          <RouterLink class="button" :to="`/sections/${sectionId}/weeks`">
            Manage Active Weeks
          </RouterLink>
          <RouterLink class="button" :to="`/teams/section/${sectionId}`">
            View Teams for This Section
          </RouterLink>
          <RouterLink class="button button-secondary" to="/sections">Back to Sections</RouterLink>
        </div>
      </div>
    </div>
  </div>
</template>
