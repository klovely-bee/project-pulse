import { createRouter, createWebHistory } from 'vue-router'
import AppShell from '../layouts/AppShell.vue'
import HomeView from '../shared/components/HomeView.vue'
import LoginView from '../features/auth/views/LoginView.vue'
import AdminDashboardView from '../features/admin/views/AdminDashboardView.vue'
import SectionWorkspaceView from '../features/sections/views/SectionWorkspaceView.vue'
import TeamWorkspaceView from '../features/teams/views/TeamWorkspaceView.vue'
import ReportsWorkspaceView from '../features/reports/views/ReportsWorkspaceView.vue'
import WARView from '../features/war/views/WARView.vue'

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
      { path: 'war', name: 'war', component: WARView },
    ]
  }
]

export default createRouter({ history: createWebHistory(), routes })
