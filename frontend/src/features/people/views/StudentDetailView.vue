<template>
  <section>
    <p class="eyebrow">People · UC-16</p>
    <RouterLink to="/people/students" class="back-link">← Back to Students</RouterLink>
    <p class="muted" v-if="loading">Loading…</p>
    <p class="error" v-if="error">{{ error }}</p>
    <template v-if="student">
      <h2>{{ student.name }}</h2>
      <div class="detail-grid">
        <div class="card"><p class="label">Email</p><p>{{ student.email }}</p></div>
        <div class="card"><p class="label">Status</p><span class="badge" :class="student.active ? 'badge-green' : 'badge-gray'">{{ student.active ? 'Active' : 'Inactive' }}</span></div>
      </div>
      <div class="actions">
        <button class="btn btn-danger" @click="showConfirm = true">Delete Student (UC-17)</button>
      </div>
    </template>
    <div class="confirm-overlay" v-if="showConfirm">
      <div class="confirm-box">
        <p>Delete <strong>{{ student?.name }}</strong>? This cannot be undone.</p>
        <div class="confirm-actions">
          <button class="btn btn-ghost" @click="showConfirm = false">Cancel</button>
          <button class="btn btn-danger" @click="confirmDelete">Delete</button>
        </div>
      </div>
    </div>
  </section>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getStudent, deleteStudent } from '../api/studentsApi.js'
const route = useRoute(), router = useRouter()
const student = ref(null), loading = ref(false), error = ref(''), showConfirm = ref(false)
onMounted(async () => { loading.value = true; try { student.value = await getStudent(route.params.id) } catch (e) { error.value = e.message } finally { loading.value = false } })
async function confirmDelete() { try { await deleteStudent(route.params.id); router.push('/people/students') } catch (e) { error.value = e.message; showConfirm.value = false } }
</script>
