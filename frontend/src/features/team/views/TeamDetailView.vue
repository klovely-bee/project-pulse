<script setup>
import { onMounted, ref } from 'vue'
import { RouterLink, useRoute } from 'vue-router'
import { getTeamById } from '../services/teamApi'

const route = useRoute()
const currentUser = JSON.parse(localStorage.getItem('currentUser') || 'null')
const isAdminUser = currentUser?.role === 'ADMIN'

const team = ref(null)
const isLoading = ref(false)
const errorMessage = ref('')

function normalizeMembers(teamData, keyOptions) {
  const key = keyOptions.find((option) => Array.isArray(teamData?.[option]))
  return key ? teamData[key] : []
}

async function loadTeam() {
  isLoading.value = true
  errorMessage.value = ''

  try {
    const response = await getTeamById(route.params.id)
    const teamData = response.data.data

    team.value = {
      ...teamData,
      students: normalizeMembers(teamData, ['students', 'studentMembers', 'teamStudents']),
      instructors: normalizeMembers(teamData, ['instructors', 'instructorMembers', 'teamInstructors']),
    }
  } catch (error) {
    errorMessage.value = error.response?.data?.message ?? 'Failed to load team.'
  } finally {
    isLoading.value = false
  }
}

function memberLabel(member) {
  return member.name || [member.firstName, member.lastName].filter(Boolean).join(' ') || member.email || `User ${member.id}`
}

onMounted(() => {
  loadTeam()
})
</script>

<template>
  <div class="page-container">
    <div class="card">
      <h1 class="page-title">Team Details</h1>
      <p class="page-subtitle">View team membership and section ownership.</p>

      <p v-if="isLoading">Loading team...</p>
      <p v-else-if="errorMessage" class="message error">{{ errorMessage }}</p>

      <template v-else-if="team">
        <div class="details">
          <p><strong>Team ID:</strong> {{ team.id }}</p>
          <p><strong>Name:</strong> {{ team.name }}</p>
          <p><strong>Section ID:</strong> {{ team.sectionId }}</p>
        </div>

        <div class="workspace-grid" style="margin-top: 20px">
          <article class="card">
            <h2 class="list-item-title">Students</h2>
            <p v-if="team.students.length === 0">No students assigned.</p>
            <ul v-else class="list">
              <li v-for="student in team.students" :key="`student-${student.id}`" class="list-item">
                <strong>{{ memberLabel(student) }}</strong>
                <p>ID: {{ student.id }}</p>
              </li>
            </ul>
          </article>

          <article class="card">
            <h2 class="list-item-title">Instructors</h2>
            <p v-if="team.instructors.length === 0">No instructors assigned.</p>
            <ul v-else class="list">
              <li v-for="instructor in team.instructors" :key="`instructor-${instructor.id}`" class="list-item">
                <strong>{{ memberLabel(instructor) }}</strong>
                <p>ID: {{ instructor.id }}</p>
              </li>
            </ul>
          </article>
        </div>

        <div class="nav-links" style="margin-top: 20px">
          <RouterLink v-if="team.sectionId" class="button button-secondary" :to="`/teams/section/${team.sectionId}`">
            Back to Team List
          </RouterLink>
          <RouterLink
            v-if="isAdminUser"
            class="button"
            :to="`/teams/${team.id}/edit?sectionId=${team.sectionId}`"
          >
            Edit Team
          </RouterLink>
          <RouterLink
            v-if="isAdminUser"
            class="button"
            :to="`/teams/${team.id}/assignments?sectionId=${team.sectionId}`"
          >
            Manage Assignments
          </RouterLink>
        </div>
      </template>
    </div>
  </div>
</template>
