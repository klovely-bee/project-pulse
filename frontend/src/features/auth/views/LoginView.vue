<script setup>
import { reactive, ref } from 'vue'
import { RouterLink, useRouter } from 'vue-router'
import { login } from '../services/authApi'

const router = useRouter()

const form = reactive({
  email: '',
  password: '',
})

const successMessage = ref('')
const errorMessage = ref('')
const isSubmitting = ref(false)

async function handleSubmit() {
  successMessage.value = ''
  errorMessage.value = ''
  isSubmitting.value = true

  try {
    const response = await login({ ...form })
    const user = response.data.data
    console.log(user)
    successMessage.value = response.data.message
    await router.push(`/users/${user.id}`)
  } catch (error) {
    console.error('Login failed', error.response?.data ?? error)
    errorMessage.value =
      error.response?.data?.message || error.message || 'Login failed'
  } finally {
    isSubmitting.value = false
  }
}
</script>

<template>
  <div class="page-container">
    <div class="card">
      <h1 class="page-title">Login</h1>
      <p class="page-subtitle">Sign in with your Project Pulse account.</p>

      <form class="form" @submit.prevent="handleSubmit">
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
            {{ isSubmitting ? 'Logging in...' : 'Login' }}
          </button>
        </div>
      </form>

      <div class="nav-links">
        <RouterLink to="/register/student">Student Registration</RouterLink>
        <RouterLink to="/register/instructor">Instructor Registration</RouterLink>
      </div>
    </div>
  </div>
</template>
