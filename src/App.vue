<script setup>
import AppHeader from './components/AppHeader.vue'
import Drinks from './components/Drinks.vue'
</script>

<template>
  <div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
      <AppHeader />
      <main class="mdl-layout__content mdl-color--grey-100">
          <div class="page-content">
              <Drinks />
          </div>
      </main>
  </div>
</template>

<style scoped>

</style>
