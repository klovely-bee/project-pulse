<template>
  <section class="page-container">
    <div v-if="isStudentUser" class="card">
      <p class="eyebrow">Peer Evaluations</p>
      <h2 class="page-title">Evaluation Workspace</h2>
      <p class="page-subtitle">Submit an evaluation and review your sent and received reports.</p>

      <p v-if="errorMessage" class="message error">{{ errorMessage }}</p>
      <p v-if="successMessage" class="message success">{{ successMessage }}</p>

      <form class="form" @submit.prevent="handleSubmit">
        <div class="form-group">
          <label for="evaluateeId">Evaluatee ID</label>
          <input id="evaluateeId" v-model="studentForm.evaluateeId" class="input" min="1" required type="number" />
        </div>

        <div class="form-group">
          <label for="teamId">Team ID</label>
          <input id="teamId" v-model="studentForm.teamId" class="input" min="1" required type="number" />
        </div>

        <div class="form-group">
          <label for="weekNumber">Week Number</label>
          <input id="weekNumber" v-model="studentForm.weekNumber" class="input" min="1" readonly required type="number" />
          <p class="page-subtitle">Peer evaluations are locked to the previous calendar week: {{ previousWeekNumber }}.</p>
        </div>

        <div class="form-group">
          <label for="score">Score</label>
          <input id="score" v-model="studentForm.score" class="input" min="1" max="10" required type="number" />
        </div>

        <div class="form-group">
          <label for="comments">Comments</label>
          <textarea id="comments" v-model="studentForm.comments" class="input" rows="4" />
        </div>

        <div class="form-actions">
          <button class="button" :disabled="isSubmitting" type="submit">
            {{ isSubmitting ? 'Submitting...' : 'Submit Evaluation' }}
          </button>
          <button class="button button-secondary" :disabled="isRefreshing" type="button" @click="loadStudentReports">
            {{ isRefreshing ? 'Refreshing...' : 'Refresh Reports' }}
          </button>
        </div>
      </form>
    </div>

    <div v-if="isStudentUser" class="workspace-grid" style="margin-top: 20px">
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

    <div v-if="canViewInstructorReports" class="workspace-grid">
      <article class="card">
        <p class="eyebrow">UC-31</p>
        <h3 class="list-item-title">Section Peer Evaluation Report</h3>
        <p class="page-subtitle">Load all teams in a section, then aggregate peer evaluations across those teams.</p>

        <div class="form-group">
          <label for="sectionReportId">Section ID</label>
          <input id="sectionReportId" v-model="sectionReportId" class="input" min="1" type="number" />
        </div>

        <div class="form-actions">
          <button class="button" type="button" @click="loadSectionEvaluationReport">Load Section Report</button>
        </div>

        <p v-if="sectionEvaluationReport.length === 0" class="muted">No section evaluation report loaded.</p>
        <div v-else class="list">
          <article v-for="item in sectionEvaluationReport" :key="`section-${item.id}`" class="subcard">
            <p><strong>Team:</strong> {{ item.teamId }}</p>
            <p><strong>Evaluator:</strong> {{ item.evaluatorId }}</p>
            <p><strong>Target:</strong> {{ item.evaluateeId }}</p>
            <p><strong>Week:</strong> {{ item.weekNumber }}</p>
            <p><strong>Score:</strong> {{ item.score }}</p>
            <p>{{ item.comments || 'No comments provided.' }}</p>
          </article>
        </div>
      </article>

      <article class="card">
        <p class="eyebrow">UC-32</p>
        <h3 class="list-item-title">Team WAR Report</h3>

        <div class="form-group">
          <label for="teamWarReportId">Team ID</label>
          <input id="teamWarReportId" v-model="teamWarReportId" class="input" min="1" type="number" />
        </div>

        <div class="form-actions">
          <button class="button" type="button" @click="loadTeamWarReport">Load Team WAR Report</button>
        </div>

        <p v-if="teamWarReports.length === 0" class="muted">No team WAR report loaded.</p>
        <div v-else class="list">
          <article v-for="war in teamWarReports" :key="`team-war-${war.id}`" class="subcard">
            <p><strong>User:</strong> {{ war.userId }}</p>
            <p><strong>Team:</strong> {{ war.teamId }}</p>
            <p><strong>Week:</strong> {{ war.weekNumber }}</p>
            <p><strong>Submitted:</strong> {{ formatDate(war.createdAt) }}</p>
            <pre class="report-content">{{ war.content }}</pre>
          </article>
        </div>
      </article>

      <article class="card">
        <p class="eyebrow">UC-33</p>
        <h3 class="list-item-title">Student Peer Evaluation Report</h3>

        <div class="form-group">
          <label for="studentEvaluationReportId">Student ID</label>
          <input id="studentEvaluationReportId" v-model="studentEvaluationReportId" class="input" min="1" type="number" />
        </div>

        <div class="form-actions">
          <button class="button" type="button" @click="loadStudentEvaluationReport">Load Student Evaluation Report</button>
        </div>

        <p v-if="studentEvaluationReports.length === 0" class="muted">No student evaluation report loaded.</p>
        <div v-else class="list">
          <article v-for="item in studentEvaluationReports" :key="`student-eval-${item.id}`" class="subcard">
            <p><strong>From:</strong> {{ item.evaluatorId }}</p>
            <p><strong>Team:</strong> {{ item.teamId }}</p>
            <p><strong>Week:</strong> {{ item.weekNumber }}</p>
            <p><strong>Score:</strong> {{ item.score }}</p>
            <p>{{ item.comments || 'No comments provided.' }}</p>
          </article>
        </div>
      </article>

      <article class="card">
        <p class="eyebrow">UC-34</p>
        <h3 class="list-item-title">Student WAR Report</h3>

        <div class="form-group">
          <label for="studentWarReportId">Student ID</label>
          <input id="studentWarReportId" v-model="studentWarReportId" class="input" min="1" type="number" />
        </div>

        <div class="form-actions">
          <button class="button" type="button" @click="loadStudentWarReport">Load Student WAR Report</button>
        </div>

        <p v-if="studentWarReports.length === 0" class="muted">No student WAR report loaded.</p>
        <div v-else class="list">
          <article v-for="war in studentWarReports" :key="`student-war-${war.id}`" class="subcard">
            <p><strong>Team:</strong> {{ war.teamId }}</p>
            <p><strong>Week:</strong> {{ war.weekNumber }}</p>
            <p><strong>Submitted:</strong> {{ formatDate(war.createdAt) }}</p>
            <pre class="report-content">{{ war.content }}</pre>
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
import { getWarsByStudent, getWarsByTeam } from '../../war/services/warApi'
import { getTeamsBySection } from '../../team/services/teamApi'

const currentUser = JSON.parse(localStorage.getItem('currentUser') || 'null')
const isStudentUser = currentUser?.role === 'STUDENT'
const canViewInstructorReports = currentUser?.role === 'ADMIN' || currentUser?.role === 'INSTRUCTOR'

const studentForm = reactive({
  evaluateeId: '',
  teamId: '',
  weekNumber: String(getPreviousCalendarWeekNumber()),
  score: '',
  comments: '',
})

const myEvaluations = ref([])
const receivedEvaluations = ref([])
const sectionReportId = ref('')
const teamWarReportId = ref('')
const studentEvaluationReportId = ref('')
const studentWarReportId = ref('')
const sectionEvaluationReport = ref([])
const teamWarReports = ref([])
const studentEvaluationReports = ref([])
const studentWarReports = ref([])
const isSubmitting = ref(false)
const isRefreshing = ref(false)
const successMessage = ref('')
const errorMessage = ref('')

const previousWeekNumber = getPreviousCalendarWeekNumber()

function getPreviousCalendarWeekNumber() {
  const now = new Date()
  const startOfYear = new Date(now.getFullYear(), 0, 1)
  const dayOfYear = Math.floor((now - startOfYear) / 86400000) + 1
  const currentWeek = Math.ceil(dayOfYear / 7)
  return Math.max(1, currentWeek - 1)
}

function resetStudentForm() {
  studentForm.evaluateeId = ''
  studentForm.teamId = ''
  studentForm.weekNumber = String(previousWeekNumber)
  studentForm.score = ''
  studentForm.comments = ''
}

function formatDate(value) {
  if (!value) {
    return 'Unknown'
  }

  return new Date(value).toLocaleString()
}

async function loadStudentReports() {
  if (!isStudentUser || !currentUser?.id) {
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
  } catch (error) {
    errorMessage.value = error.response?.data?.message || 'Failed to load evaluations.'
  } finally {
    isRefreshing.value = false
  }
}

async function handleSubmit() {
  if (!isStudentUser || !currentUser?.id) {
    return
  }

  isSubmitting.value = true
  successMessage.value = ''
  errorMessage.value = ''

  try {
    if (Number(studentForm.weekNumber) !== previousWeekNumber) {
      throw new Error(`Peer evaluations must be submitted for the previous week (${previousWeekNumber}).`)
    }

    await createEvaluation({
      evaluatorId: currentUser.id,
      evaluateeId: Number(studentForm.evaluateeId),
      teamId: Number(studentForm.teamId),
      weekNumber: Number(studentForm.weekNumber),
      score: Number(studentForm.score),
      comments: studentForm.comments,
    })

    successMessage.value = 'Evaluation submitted successfully.'
    resetStudentForm()
    await loadStudentReports()
  } catch (error) {
    errorMessage.value = error.response?.data?.message || 'Failed to submit evaluation.'
  } finally {
    isSubmitting.value = false
  }
}

async function loadSectionEvaluationReport() {
  errorMessage.value = ''

  if (!sectionReportId.value) {
    sectionEvaluationReport.value = []
    return
  }

  try {
    const teamsResponse = await getTeamsBySection(Number(sectionReportId.value))
    const teams = teamsResponse.data.data ?? []

    if (teams.length === 0) {
      sectionEvaluationReport.value = []
      return
    }

    const evaluationResponses = await Promise.all(
      teams.map((team) => getEvaluationsByTeam(team.id)),
    )

    sectionEvaluationReport.value = evaluationResponses
      .flatMap((response) => response.data.data ?? [])
      .sort((left, right) => new Date(right.createdAt ?? 0) - new Date(left.createdAt ?? 0))
  } catch (error) {
    errorMessage.value = error.response?.data?.message || 'Failed to load section evaluation report.'
  }
}

async function loadTeamWarReport() {
  errorMessage.value = ''

  if (!teamWarReportId.value) {
    teamWarReports.value = []
    return
  }

  try {
    const response = await getWarsByTeam(Number(teamWarReportId.value))
    teamWarReports.value = response.data.data
  } catch (error) {
    errorMessage.value = error.response?.data?.message || 'Failed to load team WAR report.'
  }
}

async function loadStudentEvaluationReport() {
  errorMessage.value = ''

  if (!studentEvaluationReportId.value) {
    studentEvaluationReports.value = []
    return
  }

  try {
    const response = await getEvaluationsByTarget(Number(studentEvaluationReportId.value))
    studentEvaluationReports.value = response.data.data
  } catch (error) {
    errorMessage.value = error.response?.data?.message || 'Failed to load student evaluation report.'
  }
}

async function loadStudentWarReport() {
  errorMessage.value = ''

  if (!studentWarReportId.value) {
    studentWarReports.value = []
    return
  }

  try {
    const response = await getWarsByStudent(Number(studentWarReportId.value))
    studentWarReports.value = response.data.data
  } catch (error) {
    errorMessage.value = error.response?.data?.message || 'Failed to load student WAR report.'
  }
}

onMounted(() => {
  if (isStudentUser) {
    loadStudentReports()
  }
})
</script>

<style scoped>
.report-content {
  margin: 12px 0 0;
  white-space: pre-wrap;
  font-family: inherit;
  background: #f8fafc;
  padding: 12px;
  border-radius: 10px;
  border: 1px solid #e5e7eb;
}
</style>
