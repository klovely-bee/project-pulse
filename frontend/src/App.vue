<script setup>
import { computed } from 'vue'
import { RouterView, useRoute } from 'vue-router'
import AppShell from './layouts/AppShell.vue'

const route = useRoute()
const isPublicRoute = computed(() => Boolean(route.meta.public))
</script>

<template>
  <div v-if="isPublicRoute" class="auth-route-shell">
    <RouterView />
  </div>
  <AppShell v-else />
</template>
