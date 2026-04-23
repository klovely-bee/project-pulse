<script setup>
import { reactive, ref } from 'vue'
import { RouterLink } from 'vue-router'
import { registerStudent } from '../services/authApi'

const form = reactive({
  email: '',
  password: '',
  firstName: '',
  lastName: '',
})

const successMessage = ref('')
const errorMessage = ref('')
const isSubmitting = ref(false)

async function handleSubmit() {
  successMessage.value = ''
  errorMessage.value = ''
  isSubmitting.value = true

  try {
    const response = await registerStudent({ ...form })
    console.log(response.data.data)
    successMessage.value = response.data.message
  } catch (error) {
    console.error('Student registration failed', error.response?.data ?? error)
    errorMessage.value =
      error.response?.data?.message || error.message || 'Student registration failed'
  } finally {
    isSubmitting.value = false
  }
}
</script>

<template>
  <div class="page-container">
    <div class="card">
      <h1 class="page-title">Student Registration</h1>
      <p class="page-subtitle">Create a student account.</p>

      <form class="form" @submit.prevent="handleSubmit">
        <div class="form-group">
          <label for="firstName">First Name</label>
          <input id="firstName" v-model="form.firstName" class="input" type="text" required />
        </div>

        <div class="form-group">
          <label for="lastName">Last Name</label>
          <input id="lastName" v-model="form.lastName" class="input" type="text" required />
        </div>

        <div class="form-group">
          <label for="email">Email</label>
          <input id="email" v-model="form.email" class="input" type="email" required />
        </div>

        <div class="form-group">
          <label for="password">Password</label>
          <input
            id="password"
            v-model="form.password"
            class="input"
            type="password"
            required
          />
        </div>

        <p v-if="successMessage" class="message success">{{ successMessage }}</p>
        <p v-if="errorMessage" class="message error">{{ errorMessage }}</p>

        <div class="form-actions">
          <button class="button" type="submit" :disabled="isSubmitting">
            {{ isSubmitting ? 'Submitting...' : 'Register Student' }}
          </button>
        </div>
      </form>

      <div class="nav-links">
        <RouterLink to="/login">Login</RouterLink>
        <RouterLink to="/register/instructor">Instructor Registration</RouterLink>
      </div>
    </div>
  </div>
</template>
