<template>
  <section>
    <p class="eyebrow">Student · UC-27</p>
    <h2>Weekly Activity Report</h2>
    <p style="color:#64748b;font-size:13px;margin-bottom:20px">Log what you worked on this week. Be specific — include outcomes, hours, and blockers.</p>

    <p class="error" v-if="error">{{ error }}</p>
    <div class="success-banner" v-if="submitted">WAR submitted for week {{ lastWeek }}!</div>

    <form class="form-card" @submit.prevent="submit">
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
  </section>
</template>
<script setup>
import { ref, computed } from 'vue'

const form = ref({ teamId: '', weekNumber: '', activities: [{ description: '', category: '', hours: 0 }] })
const error = ref(''), submitted = ref(false), saving = ref(false), lastWeek = ref('')

const totalHours = computed(() => form.value.activities.reduce((s, a) => s + (Number(a.hours) || 0), 0).toFixed(1))

function add() { form.value.activities.push({ description: '', category: '', hours: 0 }) }
function remove(i) { form.value.activities.splice(i, 1) }
function reset() { form.value.activities = [{ description: '', category: '', hours: 0 }]; form.value.weekNumber = ''; submitted.value = false; error.value = '' }

async function submit() {
  error.value = ''; submitted.value = false; saving.value = true
  try {
    const payload = {
      teamId: Number(form.value.teamId),
      weekNumber: Number(form.value.weekNumber),
      activities: form.value.activities,
    }
    const res = await fetch('/api/war', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
    if (!res.ok) { const e = await res.json().catch(() => ({})); throw new Error(e.message || `HTTP ${res.status}`) }
    lastWeek.value = form.value.weekNumber
    submitted.value = true
    reset()
  } catch (e) { error.value = e.message }
  finally { saving.value = false }
}
</script>
