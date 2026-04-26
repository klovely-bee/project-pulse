import { createRouter, createWebHistory } from 'vue-router'

// Auth & user (feature/auth-user-foundation)
import LoginView from '../features/auth/views/LoginView.vue'
import StudentRegisterView from '../features/auth/views/StudentRegisterView.vue'
import InstructorRegisterView from '../features/auth/views/InstructorRegisterView.vue'
import UserProfileView from '../features/user/views/UserProfileView.vue'
import EditProfileView from '../features/user/views/EditProfileView.vue'

// People management (feature/admin-user-management — UC-11, 15-18, 21-24)
import StudentListView from '../features/people/views/StudentListView.vue'
import StudentDetailView from '../features/people/views/StudentDetailView.vue'
import StudentInviteView from '../features/people/views/StudentInviteView.vue'
import InstructorListView from '../features/people/views/InstructorListView.vue'
import InstructorDetailView from '../features/people/views/InstructorDetailView.vue'
import InstructorInviteView from '../features/people/views/InstructorInviteView.vue'

// WAR (feature/student-war — UC-27)
import WARView from '../features/war/views/WARView.vue'

const routes = [
  // Root redirect
  { path: '/', redirect: '/login' },

  // Auth routes (public — no AppShell)
  { path: '/login', name: 'login', component: LoginView, meta: { public: true } },
  { path: '/register/student', name: 'register-student', component: StudentRegisterView, meta: { public: true } },
  { path: '/register/instructor', name: 'register-instructor', component: InstructorRegisterView, meta: { public: true } },
  { path: '/users/:id', name: 'user-profile', component: UserProfileView },
  { path: '/users/:id/edit', name: 'edit-profile', component: EditProfileView },

  // People management
  { path: '/people/students', name: 'students', component: StudentListView },
  { path: '/people/students/invite', name: 'student-invite', component: StudentInviteView },
  { path: '/people/students/:id', name: 'student-detail', component: StudentDetailView },
  { path: '/people/instructors', name: 'instructors', component: InstructorListView },
  { path: '/people/instructors/invite', name: 'instructor-invite', component: InstructorInviteView },
  { path: '/people/instructors/:id', name: 'instructor-detail', component: InstructorDetailView },

  // WAR
  { path: '/war', name: 'war', component: WARView },

  { path: '/:pathMatch(.*)*', redirect: '/login' },
]

export default createRouter({ history: createWebHistory(), routes })
