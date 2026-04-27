<script setup>
import { onMounted, ref } from 'vue'
import { RouterLink } from 'vue-router'
import { getSections } from '../services/sectionApi'

const sections = ref([])
const isLoading = ref(false)
const errorMessage = ref('')

async function loadSections() {
  isLoading.value = true
  errorMessage.value = ''

  try {
    const response = await getSections()
    sections.value = response.data.data
  } catch (error) {
    errorMessage.value = error.response?.data?.message ?? 'Failed to load sections.'
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  loadSections()
})
</script>

<template>
  <div class="page-container">
    <div class="card">
      <div class="section-header">
        <div>
          <h1 class="page-title">Sections</h1>
          <p class="page-subtitle">View available sections.</p>
        </div>
      </div>

      <div class="nav-links">
        <RouterLink class="button" to="/sections/create">Create Section</RouterLink>
        <RouterLink class="button button-secondary" to="/rubrics/create">Create Rubric</RouterLink>
        <RouterLink class="button button-secondary" to="/teams/create">Create Team</RouterLink>
      </div>

      <p v-if="isLoading">Loading sections...</p>
      <p v-else-if="errorMessage" class="message error">{{ errorMessage }}</p>
      <p v-else-if="sections.length === 0">No sections found.</p>

      <div v-else class="list">
        <RouterLink
          v-for="section in sections"
          :key="section.id"
          class="list-item"
          :to="`/sections/${section.id}`"
        >
          <h2 class="list-item-title">{{ section.name }}</h2>
          <p>{{ section.semester }} {{ section.year }}</p>
        </RouterLink>
      </div>
    </div>
  </div>
</template>
