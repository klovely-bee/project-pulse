<template>
  <section>
    <p class="eyebrow">People · UC-21</p>
    <h2>Find Instructors</h2>
    <div class="toolbar">
      <input class="search-input" v-model="query" type="text" placeholder="Search by name…" @keyup.enter="search" />
      <button class="btn" @click="search">Search</button>
      <button class="btn btn-ghost" @click="reset">Reset</button>
      <RouterLink to="/people/instructors/invite" class="btn btn-primary">+ Invite Instructor</RouterLink>
    </div>
    <p class="error" v-if="error">{{ error }}</p>
    <p class="muted" v-if="loading">Loading…</p>
    <table v-if="!loading && instructors.length" class="data-table">
      <thead><tr><th>Name</th><th>Email</th><th>Status</th><th>Actions</th></tr></thead>
      <tbody>
        <tr v-for="i in instructors" :key="i.id">
          <td><RouterLink :to="`/people/instructors/${i.id}`">{{ i.name }}</RouterLink></td>
          <td>{{ i.email }}</td>
          <td><span class="badge" :class="i.active ? 'badge-green' : 'badge-gray'">{{ i.active ? 'Active' : 'Inactive' }}</span></td>
          <td>
            <RouterLink :to="`/people/instructors/${i.id}`" class="btn btn-sm">View</RouterLink>
            <button class="btn btn-sm" :class="i.active ? 'btn-outline' : 'btn-primary'" style="margin-left:6px" @click="toggleStatus(i)">{{ i.active ? 'Deactivate' : 'Reactivate' }}</button>
          </td>
        </tr>
      </tbody>
    </table>
    <p class="muted" v-if="!loading && !instructors.length">No instructors found.</p>
  </section>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { findInstructors, deactivateInstructor, reactivateInstructor } from '../api/instructorsApi.js'
const instructors = ref([]), query = ref(''), loading = ref(false), error = ref('')
async function search() {
  loading.value = true; error.value = ''
  try { instructors.value = await findInstructors(query.value) } catch (e) { error.value = e.response?.data?.message || '' } finally { loading.value = false }
}
function reset() { query.value = ''; search() }
async function toggleStatus(i) {
  try {
    const updated = i.active ? await deactivateInstructor(i.id) : await reactivateInstructor(i.id)
    const idx = instructors.value.findIndex(x => x.id === i.id)
    if (idx !== -1) instructors.value[idx] = updated
  } catch (e) { error.value = e.response?.data?.message || '' }
}
onMounted(search)
</script>
