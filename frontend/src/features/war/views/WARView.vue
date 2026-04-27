<template>
  <section class="page-container">
    <div class="card">
    <p class="eyebrow">Student · UC-27</p>
    <h2>Weekly Activity Report</h2>
    <p class="page-subtitle">Log what you worked on this week. Include outcomes, hours, and blockers.</p>

    <p class="error" v-if="error">{{ error }}</p>
    <div class="success-banner" v-if="submitted">WAR submitted for week {{ lastWeek }}.</div>
    <p class="muted" v-if="historyLoading">Loading your WAR history...</p>

    <form class="form" @submit.prevent="submit">
      <div class="form-group">
        <label>Team ID</label>
        <input v-model="form.teamId" type="number" placeholder="Enter your team ID" required />
      </div>
      <div class="form-group">
        <label>Week Number</label>
        <input v-model="form.weekNumber" type="number" min="1" placeholder="e.g. 3" required />
      </div>

      <div style="margin:18px 0 10px;font-weight:600;font-size:14px">Activities</div>

      <div v-for="(a, i) in form.activities" :key="i" class="activity-row">
        <input v-model="a.description" :placeholder="`Activity ${i+1}`" required />
        <input v-model="a.category" placeholder="Category" />
        <input v-model.number="a.hours" type="number" min="0" step="0.5" placeholder="Hours" />
        <button type="button" class="btn btn-ghost btn-sm" @click="remove(i)" :disabled="form.activities.length === 1">✕</button>
      </div>

      <div style="display:flex;align-items:center;gap:12px;margin-top:8px">
        <button type="button" class="btn btn-outline btn-sm" @click="add">+ Add activity</button>
        <span style="font-size:12px;color:#64748b">Total: {{ totalHours }}h</span>
      </div>

      <div class="form-actions">
        <button type="button" class="btn btn-ghost" @click="reset">Reset</button>
        <button class="btn btn-primary" type="submit" :disabled="saving">{{ saving ? 'Submitting…' : 'Submit WAR' }}</button>
      </div>
    </form>

    <div class="card" style="margin-top: 20px">
      <h3 class="list-item-title">My WAR History</h3>
      <p v-if="!historyLoading && warHistory.length === 0" class="muted">No WARs submitted yet.</p>

      <div v-else class="list">
        <article v-for="war in warHistory" :key="war.id" class="subcard">
          <p><strong>Week:</strong> {{ war.weekNumber }}</p>
          <p><strong>Team ID:</strong> {{ war.teamId }}</p>
          <p><strong>Submitted:</strong> {{ formatDate(war.createdAt) }}</p>
          <pre class="war-content">{{ war.content }}</pre>
        </article>
      </div>
    </div>
    </div>
  </section>
</template>
<script setup>
import { computed, onMounted, ref } from 'vue'
import { createWar, getWarsByStudent } from '../services/warApi'

const currentUser = JSON.parse(localStorage.getItem('currentUser') || 'null')

const form = ref({ teamId: '', weekNumber: '', activities: [{ description: '', category: '', hours: 0 }] })
const warHistory = ref([])
const error = ref('')
const submitted = ref(false)
const saving = ref(false)
const historyLoading = ref(false)
const lastWeek = ref('')

const totalHours = computed(() => form.value.activities.reduce((s, a) => s + (Number(a.hours) || 0), 0).toFixed(1))

function add() { form.value.activities.push({ description: '', category: '', hours: 0 }) }
function remove(i) { form.value.activities.splice(i, 1) }
function reset() {
  form.value.teamId = ''
  form.value.weekNumber = ''
  form.value.activities = [{ description: '', category: '', hours: 0 }]
  submitted.value = false
  error.value = ''
}

function buildContent() {
  return form.value.activities
    .map((activity, index) => {
      const hours = Number(activity.hours) || 0
      const category = activity.category ? `Category: ${activity.category}` : 'Category: Unspecified'
      return `Activity ${index + 1}: ${activity.description}\n${category}\nHours: ${hours}`
    })
    .join('\n\n')
}

function formatDate(value) {
  if (!value) {
    return 'Unknown'
  }

  return new Date(value).toLocaleString()
}

async function loadWarHistory() {
  if (!currentUser?.id) {
    warHistory.value = []
    return
  }

  historyLoading.value = true

  try {
    const response = await getWarsByStudent(currentUser.id)
    warHistory.value = response.data.data
  } catch (e) {
    error.value = e.response?.data?.message || e.message || 'Failed to load WAR history.'
  } finally {
    historyLoading.value = false
  }
}

async function submit() {
  error.value = ''; submitted.value = false; saving.value = true
  try {
    const payload = {
      userId: currentUser?.id,
      teamId: Number(form.value.teamId),
      weekNumber: Number(form.value.weekNumber),
      content: buildContent(),
    }

    if (!payload.userId) {
      throw new Error('No logged-in user found.')
    }

    await createWar(payload)
    lastWeek.value = form.value.weekNumber
    submitted.value = true
    reset()
    await loadWarHistory()
  } catch (e) { error.value = e.response?.data?.message || e.message || 'Failed to submit WAR.' }
  finally { saving.value = false }
}

onMounted(() => {
  loadWarHistory()
})
</script>

<style scoped>
.war-content {
  margin: 12px 0 0;
  white-space: pre-wrap;
  font-family: inherit;
  background: #f8fafc;
  padding: 12px;
  border-radius: 10px;
  border: 1px solid #e5e7eb;
}
</style>
