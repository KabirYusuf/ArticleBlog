<script setup>
import { RouterView } from "vue-router";
import Header from "@/components/layouts/Header.vue"
import Footer from "@/components/layouts/Footer.vue"
</script>

<template>
  <Header />

  <main>
    <RouterView />
  </main>

  <Footer />
  
</template>

<style lang="scss">
@import "@/assets/main.scss";
</style>
