<template>
  <section>
    <p class="eyebrow">People · UC-15</p>
    <h2>Find Students</h2>
    <div class="toolbar">
      <input class="search-input" v-model="query" placeholder="Search by name…" @keyup.enter="search" />
      <button class="btn" @click="search">Search</button>
      <button class="btn btn-ghost" @click="reset">Reset</button>
      <RouterLink to="/people/students/invite" class="btn btn-primary">+ Invite Student</RouterLink>
    </div>
    <p class="error" v-if="error">{{ error }}</p>
    <p class="muted" v-if="loading">Loading…</p>
    <table v-if="!loading && students.length" class="data-table">
      <thead><tr><th>Name</th><th>Email</th><th>Status</th><th>Actions</th></tr></thead>
      <tbody>
        <tr v-for="s in students" :key="s.id">
          <td><RouterLink :to="`/people/students/${s.id}`">{{ s.name }}</RouterLink></td>
          <td>{{ s.email }}</td>
          <td><span class="badge" :class="s.active ? 'badge-green' : 'badge-gray'">{{ s.active ? 'Active' : 'Inactive' }}</span></td>
          <td>
            <RouterLink :to="`/people/students/${s.id}`" class="btn btn-sm">View</RouterLink>
            <button class="btn btn-sm btn-danger" style="margin-left:6px" @click="deleting = s">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>
    <p class="muted" v-if="!loading && !students.length">No students found.</p>
    <div class="confirm-overlay" v-if="deleting">
      <div class="confirm-box">
        <p>Delete <strong>{{ deleting.name }}</strong>? This cannot be undone.</p>
        <div class="confirm-actions">
          <button class="btn btn-ghost" @click="deleting = null">Cancel</button>
          <button class="btn btn-danger" @click="confirmDelete">Delete</button>
        </div>
      </div>
    </div>
  </section>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { findStudents, deleteStudent } from '../api/studentsApi.js'
const students = ref([]), query = ref(''), loading = ref(false), error = ref(''), deleting = ref(null)
async function search() {
  loading.value = true; error.value = ''
  try { students.value = await findStudents(query.value) } catch (e) { error.value = e.message } finally { loading.value = false }
}
function reset() { query.value = ''; search() }
async function confirmDelete() {
  try { await deleteStudent(deleting.value.id); students.value = students.value.filter(s => s.id !== deleting.value.id); deleting.value = null }
  catch (e) { error.value = e.message; deleting.value = null }
}
onMounted(search)
</script>
