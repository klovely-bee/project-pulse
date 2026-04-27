<script setup>
import { computed } from 'vue'
import { RouterLink, RouterView, useRoute, useRouter } from 'vue-router'
import { logout } from '../features/auth/services/authApi'

const route = useRoute()
const router = useRouter()

const currentUser = computed(() => {
  const stored = localStorage.getItem('currentUser')
  return stored ? JSON.parse(stored) : null
})

const homePath = computed(() => {
  if (currentUser.value?.role === 'ADMIN') return '/sections'
  if (currentUser.value?.role === 'INSTRUCTOR') return '/people/students'
  if (currentUser.value?.role === 'STUDENT') return '/war'
  return '/login'
})

const pageTitle = computed(() => {
  const titles = {
    login: 'Login',
    'register-student': 'Student Registration',
    'register-instructor': 'Instructor Registration',
    'user-profile': 'User Profile',
    'edit-profile': 'Edit Profile',
    'rubric-create': 'Create Rubric',
    'section-list': 'Sections',
    'section-create': 'Create Section',
    'section-detail': 'Section Details',
    'section-edit': 'Edit Section',
    'section-weeks': 'Active Weeks',
    'team-create': 'Create Team',
    'team-list-by-section': 'Teams',
    'team-detail': 'Team Details',
    'team-edit': 'Edit Team',
    'team-assignments': 'Team Assignments',
    students: 'Students',
    'student-invite': 'Invite Student',
    'student-detail': 'Student Details',
    instructors: 'Instructors',
    'instructor-invite': 'Invite Instructor',
    'instructor-detail': 'Instructor Details',
    war: 'Weekly Activity Reports',
    reports: 'Reports',
  }

  return titles[route.name] ?? 'Project Pulse'
})

const breadcrumb = computed(() => {
  const labels = {
    sections: 'Sections',
    rubrics: 'Create Rubric',
    teams: 'Teams',
    people: 'People',
    students: 'Students',
    instructors: 'Instructors',
    war: 'Weekly Activity Reports',
    reports: 'Reports',
    users: 'My Profile',
  }

  const parts = route.path.split('/').filter(Boolean)
  return parts.map((part) => labels[part] ?? part)
})

const profilePath = computed(() => {
  return currentUser.value?.id ? `/users/${currentUser.value.id}` : '/login'
})

const userDisplayName = computed(() => {
  if (currentUser.value?.email) {
    return currentUser.value.email
  }

  return 'Project Pulse'
})

const userInitial = computed(() => userDisplayName.value.charAt(0).toUpperCase())

const isAdminUser = computed(() => currentUser.value?.role === 'ADMIN')
const isInstructorUser = computed(() => currentUser.value?.role === 'INSTRUCTOR')
const isStudentUser = computed(() => currentUser.value?.role === 'STUDENT')

const isHomeActive = computed(() => {
  return route.path === homePath.value || route.path.startsWith(`${homePath.value}/`)
})
const isWarActive = computed(() => route.path.startsWith('/war'))
const isReportsActive = computed(() => route.path.startsWith('/reports'))
const isAdminActive = computed(() => isAdminUser.value && (
  route.path.startsWith('/sections') ||
  route.path.startsWith('/rubrics') ||
  route.path.startsWith('/teams') ||
  route.path.startsWith('/people')
))
const isProfileActive = computed(() => route.path.startsWith('/users'))

async function handleLogout() {
  try {
    await logout()
  } catch {
    // Local session cleanup should still proceed even if logout request fails.
  }
  localStorage.removeItem('currentUser')
  await router.push('/login')
}
</script>

<template>
  <div class="dashboard-shell">
    <aside class="sidebar">
      <div class="sidebar-brand">
        <p class="sidebar-overline">TCU Senior Design</p>
        <RouterLink class="sidebar-logo" :to="homePath">
          <span class="sidebar-logo-mark">~+</span>
          <span>Project Pulse</span>
        </RouterLink>
      </div>

      <nav class="sidebar-nav">
        <RouterLink class="sidebar-link" :class="{ active: isHomeActive }" :to="homePath">
          <span class="sidebar-icon">🏠</span>
          <span>Home</span>
        </RouterLink>

        <details v-if="isStudentUser" class="sidebar-group" :open="isWarActive">
          <summary class="sidebar-summary" :class="{ active: isWarActive }">
            <span class="sidebar-icon">📝</span>
            <span>Weekly Activity Reports</span>
          </summary>
          <div class="sidebar-subnav">
            <RouterLink
              class="sidebar-sublink"
              :class="{ active: route.path.startsWith('/war') }"
              to="/war"
            >
              Submit WAR
            </RouterLink>
          </div>
        </details>

        <details v-if="isStudentUser || isInstructorUser || isAdminUser" class="sidebar-group" :open="isReportsActive">
          <summary class="sidebar-summary" :class="{ active: isReportsActive }">
            <span class="sidebar-icon">📊</span>
            <span>{{ isStudentUser ? 'Peer Evaluations' : 'Reports' }}</span>
          </summary>
          <div class="sidebar-subnav">
            <RouterLink
              class="sidebar-sublink"
              :class="{ active: route.path.startsWith('/reports') }"
              to="/reports"
            >
              {{ isStudentUser ? 'My Evaluations' : 'Reporting Workspace' }}
            </RouterLink>
          </div>
        </details>

        <details v-if="isAdminUser" class="sidebar-group" :open="isAdminActive">
          <summary class="sidebar-summary" :class="{ active: isAdminActive }">
            <span class="sidebar-icon">🛠️</span>
            <span>Admin Management</span>
          </summary>
          <div class="sidebar-subnav">
            <RouterLink
              class="sidebar-sublink"
              :class="{ active: route.path.startsWith('/sections') }"
              to="/sections"
            >
              Sections
            </RouterLink>
            <RouterLink
              class="sidebar-sublink"
              :class="{ active: route.path.startsWith('/rubrics') }"
              to="/rubrics/create"
            >
              Create Rubric
            </RouterLink>
            <RouterLink
              class="sidebar-sublink"
              :class="{ active: route.path.startsWith('/teams') }"
              to="/teams/create"
            >
              Teams
            </RouterLink>
            <RouterLink
              class="sidebar-sublink"
              :class="{ active: route.path.startsWith('/people/students') }"
              to="/people/students"
            >
              Students
            </RouterLink>
            <RouterLink
              class="sidebar-sublink"
              :class="{ active: route.path.startsWith('/people/instructors') }"
              to="/people/instructors"
            >
              Instructors
            </RouterLink>
          </div>
        </details>

        <details v-if="isInstructorUser" class="sidebar-group" :open="route.path.startsWith('/people/students')">
          <summary
            class="sidebar-summary"
            :class="{ active: route.path.startsWith('/people/students') }"
          >
            <span class="sidebar-icon">👥</span>
            <span>Students</span>
          </summary>
          <div class="sidebar-subnav">
            <RouterLink
              class="sidebar-sublink"
              :class="{ active: route.path.startsWith('/people/students') }"
              to="/people/students"
            >
              Students
            </RouterLink>
          </div>
        </details>

        <details class="sidebar-group" :open="isProfileActive">
          <summary class="sidebar-summary" :class="{ active: isProfileActive }">
            <span class="sidebar-icon">👤</span>
            <span>My Profile</span>
          </summary>
          <div class="sidebar-subnav">
            <RouterLink
              class="sidebar-sublink"
              :class="{ active: route.path.startsWith('/users') }"
              :to="profilePath"
            >
              User Profile
            </RouterLink>
          </div>
        </details>

        <button class="sidebar-logout" type="button" @click="handleLogout">
          <span class="sidebar-icon">↩</span>
          <span>Logout</span>
        </button>
      </nav>
    </aside>

    <main class="dashboard-content">
      <header class="content-header">
        <div class="content-header-copy">
          <p class="eyebrow">Dashboard</p>
          <h1 class="content-title">{{ pageTitle }}</h1>
          <p v-if="breadcrumb.length" class="content-breadcrumb">
            Dashboard / {{ breadcrumb.join(' / ') }}
          </p>
        </div>
        <div class="content-user">
          <div class="content-user-meta">
            <strong>{{ userDisplayName }}</strong>
            <span>{{ currentUser?.role ?? 'Project Workspace' }}</span>
          </div>
          <div class="content-user-avatar">{{ userInitial }}</div>
        </div>
      </header>

      <section class="content-panel">
        <RouterView />
      </section>
    </main>
  </div>
</template>
