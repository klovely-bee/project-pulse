<template>
  <section class="page-container">
    <div class="card">
      <p class="eyebrow">Peer Evaluations</p>
      <h2 class="page-title">Evaluation Workspace</h2>
      <p class="page-subtitle">Submit an evaluation and review your sent, team, and received evaluations.</p>

      <p v-if="errorMessage" class="message error">{{ errorMessage }}</p>
      <p v-if="successMessage" class="message success">{{ successMessage }}</p>

      <form class="form" @submit.prevent="handleSubmit">
        <div class="form-group">
          <label for="evaluateeId">Evaluatee ID</label>
          <input id="evaluateeId" v-model="form.evaluateeId" class="input" min="1" required type="number" />
        </div>

        <div class="form-group">
          <label for="teamId">Team ID</label>
          <input id="teamId" v-model="form.teamId" class="input" min="1" required type="number" />
        </div>

        <div class="form-group">
          <label for="weekNumber">Week Number</label>
          <input id="weekNumber" v-model="form.weekNumber" class="input" min="1" required type="number" />
        </div>

        <div class="form-group">
          <label for="score">Score</label>
          <input id="score" v-model="form.score" class="input" min="1" max="10" required type="number" />
        </div>

        <div class="form-group">
          <label for="comments">Comments</label>
          <textarea id="comments" v-model="form.comments" class="input" rows="4" />
        </div>

        <div class="form-actions">
          <button class="button" :disabled="isSubmitting" type="submit">
            {{ isSubmitting ? 'Submitting...' : 'Submit Evaluation' }}
          </button>
          <button class="button button-secondary" :disabled="isRefreshing" type="button" @click="loadReports">
            {{ isRefreshing ? 'Refreshing...' : 'Refresh Reports' }}
          </button>
        </div>
      </form>
    </div>

    <div class="workspace-grid" style="margin-top: 20px">
      <article class="card">
        <h3 class="list-item-title">My Evaluations</h3>
        <p v-if="isRefreshing && myEvaluations.length === 0" class="muted">Loading...</p>
        <p v-else-if="myEvaluations.length === 0" class="muted">No evaluations submitted yet.</p>
        <div v-else class="list">
          <article v-for="item in myEvaluations" :key="`mine-${item.id}`" class="subcard">
            <p><strong>Target:</strong> {{ item.evaluateeId }}</p>
            <p><strong>Team:</strong> {{ item.teamId }}</p>
            <p><strong>Week:</strong> {{ item.weekNumber }}</p>
            <p><strong>Score:</strong> {{ item.score }}</p>
            <p>{{ item.comments || 'No comments provided.' }}</p>
          </article>
        </div>
      </article>

      <article class="card">
        <h3 class="list-item-title">Team Evaluations</h3>
        <div class="form-group">
          <label for="reportTeamId">Report Team ID</label>
          <input
            id="reportTeamId"
            v-model="teamReportId"
            class="input"
            min="1"
            type="number"
            @change="loadTeamEvaluations"
          />
        </div>
        <p v-if="teamEvaluations.length === 0" class="muted">No team evaluations loaded.</p>
        <div v-else class="list">
          <article v-for="item in teamEvaluations" :key="`team-${item.id}`" class="subcard">
            <p><strong>Evaluator:</strong> {{ item.evaluatorId }}</p>
            <p><strong>Target:</strong> {{ item.evaluateeId }}</p>
            <p><strong>Week:</strong> {{ item.weekNumber }}</p>
            <p><strong>Score:</strong> {{ item.score }}</p>
            <p>{{ item.comments || 'No comments provided.' }}</p>
          </article>
        </div>
      </article>

      <article class="card">
        <h3 class="list-item-title">Received Evaluations</h3>
        <p v-if="isRefreshing && receivedEvaluations.length === 0" class="muted">Loading...</p>
        <p v-else-if="receivedEvaluations.length === 0" class="muted">No received evaluations yet.</p>
        <div v-else class="list">
          <article v-for="item in receivedEvaluations" :key="`received-${item.id}`" class="subcard">
            <p><strong>From:</strong> {{ item.evaluatorId }}</p>
            <p><strong>Team:</strong> {{ item.teamId }}</p>
            <p><strong>Week:</strong> {{ item.weekNumber }}</p>
            <p><strong>Score:</strong> {{ item.score }}</p>
            <p>{{ item.comments || 'No comments provided.' }}</p>
          </article>
        </div>
      </article>
    </div>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import {
  createEvaluation,
  getEvaluationsByStudent,
  getEvaluationsByTarget,
  getEvaluationsByTeam,
} from '../services/evaluationApi'

const currentUser = JSON.parse(localStorage.getItem('currentUser') || 'null')

const form = reactive({
  evaluateeId: '',
  teamId: '',
  weekNumber: '',
  score: '',
  comments: '',
})

const teamReportId = ref('')
const myEvaluations = ref([])
const teamEvaluations = ref([])
const receivedEvaluations = ref([])
const isSubmitting = ref(false)
const isRefreshing = ref(false)
const successMessage = ref('')
const errorMessage = ref('')

function resetForm() {
  form.evaluateeId = ''
  form.teamId = ''
  form.weekNumber = ''
  form.score = ''
  form.comments = ''
}

async function loadTeamEvaluations() {
  if (!teamReportId.value) {
    teamEvaluations.value = []
    return
  }

  try {
    const response = await getEvaluationsByTeam(Number(teamReportId.value))
    teamEvaluations.value = response.data.data
  } catch (error) {
    errorMessage.value = error.response?.data?.message || 'Failed to load team evaluations.'
  }
}

async function loadReports() {
  if (!currentUser?.id) {
    errorMessage.value = 'No logged-in user found.'
    return
  }

  isRefreshing.value = true
  errorMessage.value = ''

  try {
    const [mineResponse, receivedResponse] = await Promise.all([
      getEvaluationsByStudent(currentUser.id),
      getEvaluationsByTarget(currentUser.id),
    ])

    myEvaluations.value = mineResponse.data.data
    receivedEvaluations.value = receivedResponse.data.data

    if (teamReportId.value) {
      await loadTeamEvaluations()
    }
  } catch (error) {
    errorMessage.value = error.response?.data?.message || 'Failed to load evaluations.'
  } finally {
    isRefreshing.value = false
  }
}

async function handleSubmit() {
  if (!currentUser?.id) {
    errorMessage.value = 'No logged-in user found.'
    return
  }

  isSubmitting.value = true
  successMessage.value = ''
  errorMessage.value = ''

  try {
    await createEvaluation({
      evaluatorId: currentUser.id,
      evaluateeId: Number(form.evaluateeId),
      teamId: Number(form.teamId),
      weekNumber: Number(form.weekNumber),
      score: Number(form.score),
      comments: form.comments,
    })

    successMessage.value = 'Evaluation submitted successfully.'
    if (!teamReportId.value) {
      teamReportId.value = form.teamId
    }
    resetForm()
    await loadReports()
  } catch (error) {
    errorMessage.value = error.response?.data?.message || 'Failed to submit evaluation.'
  } finally {
    isSubmitting.value = false
  }
}

onMounted(() => {
  document.title = 'Reports | Project Pulse'
  loadReports()
})
</script>
