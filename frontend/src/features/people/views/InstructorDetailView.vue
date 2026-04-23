<template>
  <section>
    <p class="eyebrow">People · UC-22</p>
    <RouterLink to="/people/instructors" class="back-link">← Back to Instructors</RouterLink>
    <p class="muted" v-if="loading">Loading…</p>
    <p class="error" v-if="error">{{ error }}</p>
    <template v-if="instructor">
      <h2>{{ instructor.name }}</h2>
      <div class="detail-grid">
        <div class="card"><p class="label">Email</p><p>{{ instructor.email }}</p></div>
        <div class="card"><p class="label">Status</p><span class="badge" :class="instructor.active ? 'badge-green' : 'badge-gray'">{{ instructor.active ? 'Active' : 'Inactive' }}</span></div>
      </div>
      <div class="actions">
        <button class="btn" :class="instructor.active ? 'btn-outline' : 'btn-primary'" @click="toggleStatus" :disabled="toggling">
          {{ instructor.active ? 'Deactivate (UC-23)' : 'Reactivate (UC-24)' }}
        </button>
      </div>
    </template>
  </section>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getInstructor, deactivateInstructor, reactivateInstructor } from '../api/instructorsApi.js'
const route = useRoute()
const instructor = ref(null), loading = ref(false), error = ref(''), toggling = ref(false)
onMounted(async () => { loading.value = true; try { instructor.value = await getInstructor(route.params.id) } catch (e) { error.value = e.message } finally { loading.value = false } })
async function toggleStatus() {
  toggling.value = true
  try { instructor.value = instructor.value.active ? await deactivateInstructor(route.params.id) : await reactivateInstructor(route.params.id) }
  catch (e) { error.value = e.message } finally { toggling.value = false }
}
</script>
