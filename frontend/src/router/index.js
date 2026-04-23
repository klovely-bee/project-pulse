import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../features/auth/views/LoginView.vue'
import StudentRegisterView from '../features/auth/views/StudentRegisterView.vue'
import InstructorRegisterView from '../features/auth/views/InstructorRegisterView.vue'
import UserProfileView from '../features/user/views/UserProfileView.vue'
import EditProfileView from '../features/user/views/EditProfileView.vue'

const routes = [
  {
    path: '/login',
    name: 'login',
    component: LoginView,
  },
  {
    path: '/register/student',
    name: 'register-student',
    component: StudentRegisterView,
  },
  {
    path: '/register/instructor',
    name: 'register-instructor',
    component: InstructorRegisterView,
  },
  {
    path: '/users/:id',
    name: 'user-profile',
    component: UserProfileView,
  },
  {
    path: '/users/:id/edit',
    name: 'edit-profile',
    component: EditProfileView,
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/login',
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
