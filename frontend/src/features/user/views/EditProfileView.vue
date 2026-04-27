<script setup>
import { onMounted, reactive, ref } from 'vue'
import { RouterLink, useRoute } from 'vue-router'
import { getUserById, updateUser } from '../services/userApi'

const route = useRoute()

const form = reactive({
  id: Number(route.params.id),
  email: '',
  firstName: '',
  lastName: '',
  role: '',
  active: true,
})

const successMessage = ref('')
const errorMessage = ref('')
const isSubmitting = ref(false)
const isLoading = ref(false)

async function loadUser() {
  errorMessage.value = ''
  isLoading.value = true

  try {
    const response = await getUserById(route.params.id)
    console.log(response.data.data)
    const user = response.data.data
    form.id = user.id
    form.email = user.email
    form.firstName = user.firstName
    form.lastName = user.lastName
    form.role = user.role
    form.active = user.active
  } catch (error) {
    errorMessage.value = error.response?.data?.message || 'Failed to load user profile'
  } finally {
    isLoading.value = false
  }
}

async function handleSubmit() {
  successMessage.value = ''
  errorMessage.value = ''
  isSubmitting.value = true

  try {
    const payload = {
      ...form,
      id: Number(route.params.id),
    }
    const response = await updateUser(route.params.id, payload)
    console.log(response.data.data)
    successMessage.value = response.data.message
  } catch (error) {
    errorMessage.value = error.response?.data?.message || 'Failed to update user profile'
  } finally {
    isSubmitting.value = false
  }
}

onMounted(loadUser)
</script>

<template>
  <div class="page-container">
    <div class="card">
      <h1 class="page-title">Edit Profile</h1>
      <p class="page-subtitle">Update the user information for the selected profile.</p>

      <p v-if="isLoading">Loading profile...</p>

      <form class="form" @submit.prevent="handleSubmit">
        <div class="form-group">
          <label for="email">Email</label>
          <input id="email" v-model="form.email" class="input" type="email" required />
        </div>

        <div class="form-group">
          <label for="firstName">First Name</label>
          <input id="firstName" v-model="form.firstName" class="input" type="text" required />
        </div>

        <div class="form-group">
          <label for="lastName">Last Name</label>
          <input id="lastName" v-model="form.lastName" class="input" type="text" required />
        </div>

        <div class="form-group">
          <label for="role">Role</label>
          <input id="role" v-model="form.role" class="input" type="text" required />
        </div>

        <div class="form-group">
          <label>
            <input v-model="form.active" type="checkbox" />
            Active
          </label>
        </div>

        <p v-if="successMessage" class="message success">{{ successMessage }}</p>
        <p v-if="errorMessage" class="message error">{{ errorMessage }}</p>

        <div class="form-actions">
          <button class="button" type="submit" :disabled="isSubmitting">
            {{ isSubmitting ? 'Saving...' : 'Save Profile' }}
          </button>
        </div>
      </form>

      <div class="nav-links">
        <RouterLink :to="`/users/${route.params.id}`">View Profile</RouterLink>
        <RouterLink to="/login">Login</RouterLink>
      </div>
    </div>
  </div>
</template>
