<template>
  <section>
    <p class="eyebrow">People · UC-18</p>
    <RouterLink to="/people/instructors" class="back-link">← Back to Instructors</RouterLink>
    <h2>Invite Instructor</h2>
    <p class="error" v-if="error">{{ error }}</p>
    <div class="success-banner" v-if="invited">Instructor <strong>{{ invited.name }}</strong> invited! <RouterLink :to="`/people/instructors/${invited.id}`">View profile →</RouterLink></div>
    <form class="form-card" @submit.prevent="submit">
      <div class="form-group"><label>Full Name *</label><input v-model="form.name" placeholder="e.g. Dr. Smith" required /></div>
      <div class="form-group"><label>Email *</label><input v-model="form.email" type="email" placeholder="instructor@tcu.edu" required /></div>
      <div class="form-group"><label>Temporary Password *</label><input v-model="form.password" type="password" required /></div>
      <div class="form-actions">
        <RouterLink to="/people/instructors" class="btn btn-ghost">Cancel</RouterLink>
        <button class="btn btn-primary" type="submit" :disabled="saving">{{ saving ? 'Inviting…' : 'Invite Instructor' }}</button>
      </div>
    </form>
  </section>
</template>
<script setup>
import { ref } from 'vue'
import { inviteInstructor } from '../api/instructorsApi.js'
const form = ref({ name: '', email: '', password: '' }), error = ref(''), invited = ref(null), saving = ref(false)
async function submit() {
  error.value = ''; invited.value = null; saving.value = true
  try { invited.value = await inviteInstructor(form.value); form.value = { name: '', email: '', password: '' } }
  catch (e) { error.value = e.message } finally { saving.value = false }
}
</script>
