<script setup>
import { onMounted, reactive, ref } from 'vue'
import { RouterLink, useRoute, useRouter } from 'vue-router'
import { getUserById, updateUser } from '../services/userApi'

const route = useRoute()
const router = useRouter()
const currentUser = JSON.parse(localStorage.getItem('currentUser') || 'null')

const form = reactive({
  id: Number(route.params.id),
  email: '',
  firstName: '',
  lastName: '',
})

const successMessage = ref('')
const errorMessage = ref('')
const isSubmitting = ref(false)
const isLoading = ref(false)

async function loadUser() {
  if (!currentUser?.id) {
    await router.replace('/login')
    return
  }

  if (currentUser.role !== 'ADMIN' && String(route.params.id) !== String(currentUser.id)) {
    await router.replace(`/users/${currentUser.id}/edit`)
    return
  }

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
      id: Number(route.params.id),
      email: form.email,
      firstName: form.firstName,
      lastName: form.lastName,
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
