import { createRouter, createWebHistory } from 'vue-router'
import AppShell from '../layouts/AppShell.vue'
import HomeView from '../shared/components/HomeView.vue'
import LoginView from '../features/auth/views/LoginView.vue'
import AdminDashboardView from '../features/admin/views/AdminDashboardView.vue'
import SectionWorkspaceView from '../features/sections/views/SectionWorkspaceView.vue'
import TeamWorkspaceView from '../features/teams/views/TeamWorkspaceView.vue'
import ReportsWorkspaceView from '../features/reports/views/ReportsWorkspaceView.vue'
import StudentListView from '../features/people/views/StudentListView.vue'
import StudentDetailView from '../features/people/views/StudentDetailView.vue'
import StudentInviteView from '../features/people/views/StudentInviteView.vue'
import InstructorListView from '../features/people/views/InstructorListView.vue'
import InstructorDetailView from '../features/people/views/InstructorDetailView.vue'
import InstructorInviteView from '../features/people/views/InstructorInviteView.vue'

const routes = [
  {
    path: '/',
    component: AppShell,
    children: [
      { path: '', name: 'home', component: HomeView },
      { path: 'login', name: 'login', component: LoginView },
      { path: 'admin', name: 'admin', component: AdminDashboardView },
      { path: 'sections', name: 'sections', component: SectionWorkspaceView },
      { path: 'teams', name: 'teams', component: TeamWorkspaceView },
      { path: 'reports', name: 'reports', component: ReportsWorkspaceView },
      { path: 'people/students', name: 'students', component: StudentListView },
      { path: 'people/students/invite', name: 'student-invite', component: StudentInviteView },
      { path: 'people/students/:id', name: 'student-detail', component: StudentDetailView },
      { path: 'people/instructors', name: 'instructors', component: InstructorListView },
      { path: 'people/instructors/invite', name: 'instructor-invite', component: InstructorInviteView },
      { path: 'people/instructors/:id', name: 'instructor-detail', component: InstructorDetailView },
    ]
  }
]

export default createRouter({ history: createWebHistory(), routes })
