<template>
  <header class="header">
    <NavBar />

    <div class="headerText__container" v-if="!isCenter">
      <div class="headerText__inner">
        <ul class="headerText__list">
          <li class="headerText__listItem">ADVENTURE</li>
          <li class="headerText__listItem">TRAVEL</li>
        </ul>
        <p class="headerText__para">{{ card?.title }}</p>

        <div class="timeSection">
          <time class="timeSection__time">{{ card?.createdAt }}</time>
          <p class="timeSection__seperator"></p>
          <p class="timeSection__para">{{ card?.title }}</p>
        </div>
        <ul class="headerText__dotList">
          <li class="headerText__dotListItem headerText--big">.</li>
          <li class="headerText__dotListItem">.</li>
          <li class="headerText__dotListItem">.</li>
        </ul>
      </div>
    </div>
    <div class="headerText__container--center" v-if="isCenter">
      <div class="headerTextCenter__content">
        <p class="headerText__center  headerText__center--title">{{ card?.title }}</p>
        <p class="headerText__center  headerText__center--content">{{ card?.content }}</p>
        <p class="headerText__center  headerText__center--authorName">By {{ card?.user.firstName }} {{ card?.user.lastName
        }}</p>
      </div>
    </div>
  </header>
</template>

<script setup>

import HambugerMenu from '../hamburger/HambugerMenu.vue'
import NavBar from './NavBar.vue'

defineProps(
  ['card', 'isCenter']
)




</script>