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
import EditTeamView from '../features/team/views/EditTeamView.vue'
import TeamAssignmentsView from '../features/team/views/TeamAssignmentsView.vue'

// People management
import StudentListView from '../features/people/views/StudentListView.vue'
import StudentDetailView from '../features/people/views/StudentDetailView.vue'
import StudentInviteView from '../features/people/views/StudentInviteView.vue'
import InstructorListView from '../features/people/views/InstructorListView.vue'
import InstructorDetailView from '../features/people/views/InstructorDetailView.vue'
import InstructorInviteView from '../features/people/views/InstructorInviteView.vue'

// WAR
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

  // Branch 2 admin setup
  { path: '/rubrics/create', name: 'rubric-create', component: CreateRubricView },
  { path: '/sections', name: 'section-list', component: SectionListView },
  { path: '/sections/create', name: 'section-create', component: CreateSectionView },
  { path: '/sections/:id', name: 'section-detail', component: SectionDetailView },
  { path: '/sections/:id/edit', name: 'section-edit', component: EditSectionView },
  { path: '/sections/:id/weeks', name: 'section-weeks', component: SectionWeeksView },
  { path: '/teams/create', name: 'team-create', component: CreateTeamView },
  { path: '/teams/section/:sectionId', name: 'team-list-by-section', component: TeamListBySectionView },
  { path: '/teams/:id/edit', name: 'team-edit', component: EditTeamView },
  { path: '/teams/:id/assignments', name: 'team-assignments', component: TeamAssignmentsView },

  // People management
  { path: '/people/students', name: 'students', component: StudentListView },
  { path: '/people/students/invite', name: 'student-invite', component: StudentInviteView },
  { path: '/people/students/:id', name: 'student-detail', component: StudentDetailView },
  { path: '/people/instructors', name: 'instructors', component: InstructorListView },
  { path: '/people/instructors/invite', name: 'instructor-invite', component: InstructorInviteView },
  { path: '/people/instructors/:id', name: 'instructor-detail', component: InstructorDetailView },

  // WAR
  { path: '/war', name: 'war', component: WARView },

  // Fallback
  { path: '/:pathMatch(.*)*', redirect: '/login' },
]

export default createRouter({
  history: createWebHistory(),
  routes,
})