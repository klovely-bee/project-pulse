<script setup>
import { computed } from 'vue'
import { RouterView, useRoute } from 'vue-router'
import AppShell from './layouts/AppShell.vue'

const route = useRoute()
const isPublicRoute = computed(() => Boolean(route.meta.public))
</script>

<template>
  <RouterView v-if="isPublicRoute" />
  <AppShell v-else />
</template>