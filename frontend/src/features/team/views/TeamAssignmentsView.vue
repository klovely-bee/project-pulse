<script setup>
import { RouterLink, useRoute } from 'vue-router'
import { reactive, ref } from 'vue'
import {
  assignInstructors,
  assignStudents,
  removeInstructors,
  removeStudents,
} from '../services/teamApi'

const route = useRoute()
const teamId = route.params.id
const sectionId = route.query.sectionId ?? ''

const form = reactive({
  userId: '',
})

const isSubmitting = ref(false)
const successMessage = ref('')
const errorMessage = ref('')

function resetMessages() {
  successMessage.value = ''
  errorMessage.value = ''
}

async function runAssignment(action, successText) {
  isSubmitting.value = true
  resetMessages()

  try {
    await action({
      teamId: Number(teamId),
      userIds: [Number(form.userId)],
    })

    successMessage.value = successText
    form.userId = ''
  } catch (error) {
    errorMessage.value = error.response?.data?.message ?? 'Assignment action failed.'
  } finally {
    isSubmitting.value = false
  }
}
</script>

<template>
  <div class="page-container">
    <div class="card">
      <h1 class="page-title">Team Assignments</h1>
      <p class="page-subtitle">Manage team assignments for team {{ teamId }}.</p>

      <div class="details">
        <p><strong>Team ID:</strong> {{ teamId }}</p>
      </div>

      <div v-if="sectionId" class="nav-links">
        <RouterLink class="button button-secondary" :to="`/teams/section/${sectionId}`">
          Back to Teams
        </RouterLink>
      </div>

      <div class="form">
        <div class="form-group">
          <label for="assignment-user-id">User ID</label>
          <input
            id="assignment-user-id"
            v-model="form.userId"
            class="input"
            min="1"
            required
            type="number"
          />
        </div>

        <p v-if="successMessage" class="message success">{{ successMessage }}</p>
        <p v-if="errorMessage" class="message error">{{ errorMessage }}</p>

        <div class="form-actions">
          <button
            class="button"
            :disabled="isSubmitting"
            type="button"
            @click="runAssignment(assignStudents, 'Student assigned successfully.')"
          >
            Assign Student
          </button>

          <button
            class="button button-secondary"
            :disabled="isSubmitting"
            type="button"
            @click="runAssignment(removeStudents, 'Student removed successfully.')"
          >
            Remove Student
          </button>

          <button
            class="button"
            :disabled="isSubmitting"
            type="button"
            @click="runAssignment(assignInstructors, 'Instructor assigned successfully.')"
          >
            Assign Instructor
          </button>

          <button
            class="button button-secondary"
            :disabled="isSubmitting"
            type="button"
            @click="runAssignment(removeInstructors, 'Instructor removed successfully.')"
          >
            Remove Instructor
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
