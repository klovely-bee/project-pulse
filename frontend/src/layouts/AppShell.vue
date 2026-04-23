<template>
  <div class="app-shell">
    <header class="hero">
      <div>
        <p class="eyebrow">TCU Senior Design</p>
        <h1>Project Pulse</h1>
        <p class="subtitle">Weekly activity reports, peer evaluations, and section management in one place.</p>
      </div>
      <nav class="nav-links">
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/login">Login</RouterLink>
        <RouterLink to="/admin">Admin</RouterLink>
        <RouterLink to="/sections">Sections</RouterLink>
        <RouterLink to="/teams">Teams</RouterLink>
        <RouterLink to="/people/students">Students</RouterLink>
        <RouterLink to="/people/instructors">Instructors</RouterLink>
        <RouterLink to="/war">Submit WAR</RouterLink>
        <RouterLink to="/reports">Reports</RouterLink>
      </nav>
    </header>
    <main class="content-panel">
      <RouterView />
    </main>
  </div>
</template>
<script setup></script>
