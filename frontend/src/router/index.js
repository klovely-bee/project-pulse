import { createRouter, createWebHistory } from 'vue-router'

// Auth & user
import LoginView from '../features/auth/views/LoginView.vue'
import StudentRegisterView from '../features/auth/views/StudentRegisterView.vue'
import InstructorRegisterView from '../features/auth/views/InstructorRegisterView.vue'
import UserProfileView from '../features/user/views/UserProfileView.vue'
import EditProfileView from '../features/user/views/EditProfileView.vue'

// Branch 2 admin setup
import CreateRubricView from '../features/rubric/views/CreateRubricView.vue'
import CreateSectionView from '../features/section/views/CreateSectionView.vue'
import SectionListView from '../features/section/views/SectionListView.vue'
import SectionDetailView from '../features/section/views/SectionDetailView.vue'
import EditSectionView from '../features/section/views/EditSectionView.vue'
import SectionWeeksView from '../features/section/views/SectionWeeksView.vue'
import CreateTeamView from '../features/team/views/CreateTeamView.vue'
import TeamListBySectionView from '../features/team/views/TeamListBySectionView.vue'
import TeamDetailView from '../features/team/views/TeamDetailView.vue'
import EditTeamView from '../features/team/views/EditTeamView.vue'
import TeamAssignmentsView from '../features/team/views/TeamAssignmentsView.vue'

// People management
import StudentListView from '../features/people/views/StudentListView.vue'
import StudentDetailView from '../features/people/views/StudentDetailView.vue'
import StudentInviteView from '../features/people/views/StudentInviteView.vue'
import InstructorListView from '../features/people/views/InstructorListView.vue'
import InstructorDetailView from '../features/people/views/InstructorDetailView.vue'
import InstructorInviteView from '../features/people/views/InstructorInviteView.vue'

// WAR / reports
import WARView from '../features/war/views/WARView.vue'
import ReportsWorkspaceView from '../features/reports/views/ReportsWorkspaceView.vue'

const routes = [
  // Root redirect
  { path: '/', redirect: '/login' },

  // Old shell shortcuts
  { path: '/admin', redirect: '/sections' },
  { path: '/teams', redirect: '/sections' },

  // Auth routes
  { path: '/login', name: 'login', component: LoginView, meta: { public: true } },
  { path: '/register/student', name: 'register-student', component: StudentRegisterView, meta: { public: true } },
  { path: '/register/instructor', name: 'register-instructor', component: InstructorRegisterView, meta: { public: true } },
  { path: '/users/:id', name: 'user-profile', component: UserProfileView, meta: { requiresAuth: true } },
  { path: '/users/:id/edit', name: 'edit-profile', component: EditProfileView, meta: { requiresAuth: true } },

  // Branch 2 admin setup
  { path: '/rubrics/create', name: 'rubric-create', component: CreateRubricView, meta: { roles: ['ADMIN'] } },
  { path: '/sections', name: 'section-list', component: SectionListView, meta: { roles: ['ADMIN'] } },
  { path: '/sections/create', name: 'section-create', component: CreateSectionView, meta: { roles: ['ADMIN'] } },
  { path: '/sections/:id', name: 'section-detail', component: SectionDetailView, meta: { roles: ['ADMIN'] } },
  { path: '/sections/:id/edit', name: 'section-edit', component: EditSectionView, meta: { roles: ['ADMIN'] } },
  { path: '/sections/:id/weeks', name: 'section-weeks', component: SectionWeeksView, meta: { roles: ['ADMIN'] } },
  { path: '/teams/create', name: 'team-create', component: CreateTeamView, meta: { roles: ['ADMIN'] } },
  { path: '/teams/section/:sectionId', name: 'team-list-by-section', component: TeamListBySectionView, meta: { roles: ['ADMIN', 'INSTRUCTOR'] } },
  { path: '/teams/:id', name: 'team-detail', component: TeamDetailView, meta: { roles: ['ADMIN', 'INSTRUCTOR'] } },
  { path: '/teams/:id/edit', name: 'team-edit', component: EditTeamView, meta: { roles: ['ADMIN'] } },
  { path: '/teams/:id/assignments', name: 'team-assignments', component: TeamAssignmentsView, meta: { roles: ['ADMIN'] } },

  // People management
  { path: '/people/students', name: 'students', component: StudentListView, meta: { roles: ['ADMIN', 'INSTRUCTOR'] } },
  { path: '/people/students/invite', name: 'student-invite', component: StudentInviteView, meta: { roles: ['ADMIN'] } },
  { path: '/people/students/:id', name: 'student-detail', component: StudentDetailView, meta: { roles: ['ADMIN', 'INSTRUCTOR'] } },
  { path: '/people/instructors', name: 'instructors', component: InstructorListView, meta: { roles: ['ADMIN'] } },
  { path: '/people/instructors/invite', name: 'instructor-invite', component: InstructorInviteView, meta: { roles: ['ADMIN'] } },
  { path: '/people/instructors/:id', name: 'instructor-detail', component: InstructorDetailView, meta: { roles: ['ADMIN'] } },

  // WAR / reports
  { path: '/war', name: 'war', component: WARView, meta: { roles: ['STUDENT'] } },
  { path: '/reports', name: 'reports', component: ReportsWorkspaceView, meta: { roles: ['ADMIN', 'INSTRUCTOR', 'STUDENT'] } },

  // Fallback
  { path: '/:pathMatch(.*)*', redirect: '/login' },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

function getCurrentUser() {
  const stored = localStorage.getItem('currentUser')
  return stored ? JSON.parse(stored) : null
}

function getHomeRoute(role) {
  if (role === 'ADMIN') return '/sections'
  if (role === 'INSTRUCTOR') return '/people/students'
  if (role === 'STUDENT') return '/war'
  return '/login'
}

router.beforeEach((to) => {
  const user = getCurrentUser()
  const isPublic = Boolean(to.meta.public)

  if (!isPublic && !user) {
    return '/login'
  }

  if (!user) {
    return true
  }

  if (to.name === 'user-profile' || to.name === 'edit-profile') {
    if (user.role !== 'ADMIN' && String(to.params.id) !== String(user.id)) {
      return `/users/${user.id}${to.name === 'edit-profile' ? '/edit' : ''}`
    }
  }

  const allowedRoles = to.meta.roles
  if (allowedRoles?.length && !allowedRoles.includes(user.role)) {
    return getHomeRoute(user.role)
  }

  return true
})

export default router
