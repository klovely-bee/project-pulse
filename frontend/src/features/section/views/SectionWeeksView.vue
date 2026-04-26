<script setup>
import { onMounted, reactive, ref } from 'vue'
import { RouterLink, useRoute } from 'vue-router'
import { getActiveWeeks, setActiveWeeks } from '../services/sectionApi'

const route = useRoute()
const sectionId = route.params.id

const weeks = reactive([])
const isLoading = ref(false)
const isSubmitting = ref(false)
const successMessage = ref('')
const errorMessage = ref('')

function createEmptyWeek() {
  return {
    weekNumber: '',
    startDate: '',
    endDate: '',
  }
}

function replaceWeeks(items) {
  weeks.splice(0, weeks.length, ...items)
}

function normalizeWeeks(items) {
  return (items ?? []).map((week) => ({
    weekNumber: week.weekNumber ?? '',
    startDate: week.startDate ?? '',
    endDate: week.endDate ?? '',
  }))
}

function addWeek() {
  weeks.push(createEmptyWeek())
}

function removeWeek(index) {
  weeks.splice(index, 1)
}

async function loadWeeks() {
  isLoading.value = true
  errorMessage.value = ''

  try {
    const response = await getActiveWeeks(sectionId)
    replaceWeeks(normalizeWeeks(response.data.data))
  } catch (error) {
    errorMessage.value = error.response?.data?.message ?? 'Failed to load active weeks.'
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
      sectionId,
      weeks: weeks.map((week) => ({
        weekNumber: Number(week.weekNumber),
        startDate: week.startDate,
        endDate: week.endDate,
      })),
    }

    await setActiveWeeks(payload)
    successMessage.value = 'Active weeks saved successfully.'
    await loadWeeks()
  } catch (error) {
    errorMessage.value = error.response?.data?.message ?? 'Failed to save active weeks.'
  } finally {
    isSubmitting.value = false
  }
}

onMounted(() => {
  loadWeeks()
})
</script>

<template>
  <div class="page-container">
    <div class="card">
      <div class="section-header">
        <div>
          <h1 class="page-title">Active Weeks</h1>
          <p class="page-subtitle">Manage active weeks for this section.</p>
        </div>
      </div>

      <div class="nav-links">
        <button class="button button-secondary" type="button" @click="addWeek">Add Week</button>
        <RouterLink class="button button-secondary" :to="`/sections/${sectionId}`">
          Back to Section Details
        </RouterLink>
      </div>

      <p v-if="isLoading">Loading active weeks...</p>

      <form v-else class="form" @submit.prevent="handleSubmit">
        <p v-if="weeks.length === 0">No active weeks configured yet.</p>

        <div v-for="(week, index) in weeks" :key="index" class="subcard">
          <div class="form-group">
            <label :for="`week-number-${index}`">Week Number</label>
            <input
              :id="`week-number-${index}`"
              v-model="week.weekNumber"
              class="input"
              min="1"
              required
              type="number"
            />
          </div>

          <div class="form-group">
            <label :for="`week-start-${index}`">Start Date</label>
            <input
              :id="`week-start-${index}`"
              v-model="week.startDate"
              class="input"
              required
              type="date"
            />
          </div>

          <div class="form-group">
            <label :for="`week-end-${index}`">End Date</label>
            <input
              :id="`week-end-${index}`"
              v-model="week.endDate"
              class="input"
              required
              type="date"
            />
          </div>

          <button class="button button-secondary" type="button" @click="removeWeek(index)">
            Remove Week
          </button>
        </div>

        <p v-if="successMessage" class="message success">{{ successMessage }}</p>
        <p v-if="errorMessage" class="message error">{{ errorMessage }}</p>

        <div class="form-actions">
          <button class="button" :disabled="isSubmitting" type="submit">
            {{ isSubmitting ? 'Saving...' : 'Save Active Weeks' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
