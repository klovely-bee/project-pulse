<script setup>
import { onMounted, ref } from 'vue'
import { RouterLink, useRoute } from 'vue-router'
import { getUserById } from '../services/userApi'

const route = useRoute()
const user = ref(null)
const successMessage = ref('')
const errorMessage = ref('')
const isLoading = ref(false)

async function loadUser() {
  successMessage.value = ''
  errorMessage.value = ''
  isLoading.value = true

  try {
    const response = await getUserById(route.params.id)
    console.log(response.data.data)
    user.value = response.data.data
    successMessage.value = response.data.message
  } catch (error) {
    errorMessage.value = error.response?.data?.message || 'Failed to load user profile'
  } finally {
    isLoading.value = false
  }
}

onMounted(loadUser)
</script>

<template>
  <div class="page-container">
    <div class="card">
      <h1 class="page-title">User Profile</h1>
      <p class="page-subtitle">View the current user information.</p>

      <p v-if="successMessage" class="message success">{{ successMessage }}</p>
      <p v-if="errorMessage" class="message error">{{ errorMessage }}</p>
      <p v-if="isLoading">Loading profile...</p>

      <div v-if="user" class="details">
        <p><strong>ID:</strong> {{ user.id }}</p>
        <p><strong>Email:</strong> {{ user.email }}</p>
        <p><strong>First Name:</strong> {{ user.firstName }}</p>
        <p><strong>Last Name:</strong> {{ user.lastName }}</p>
        <p><strong>Role:</strong> {{ user.role }}</p>
        <p><strong>Active:</strong> {{ user.active }}</p>
      </div>

      <div class="nav-links">
        <RouterLink :to="`/users/${route.params.id}/edit`">Edit Profile</RouterLink>
        <RouterLink to="/login">Login</RouterLink>
      </div>
    </div>
  </div>
</template>
