<template>
    <header class="header" :style="{ backgroundImage: 'url(' + headerBackgroundImage + ')' }">
      <HambugerMenu/>
    <div class="header__overlay">
   <nav class="nav">
    <div class="nav__logoContainer">
      <a class="nav__logoLink" href="#">RUNO</a>
    </div>
    <ul class="nav__list">
      <li class="nav__listItem nav_listItem--pushRight">
          <router-link to="/" class="nav__listLink" >Home</router-link>
      </li>
      <li class="nav__listItem"><a class="nav__listLink" href="#">About</a></li>
      <li class="nav__listItem"><a class="nav__listLink" href="#">Article</a></li>
      <li class="nav__listItem" @click="modalStore.openModal('login')"><a class="nav__listLink" href="#">Sign in</a></li>
      <li class="nav__listItem" @click="modalStore.openModal('register')"><a class="nav__listLink" href="#">Register</a></li>
    </ul>
   </nav>
    </div>

   <div class="headerText__container">
    <div class="headerText__inner">
      <ul class="headerText__list">
        <li class="headerText__listItem">ADVENTURE</li>
        <li class="headerText__listItem">TRAVEL</li>
      </ul>
      <p class="headerText__para">{{ title }}</p>

      <div class="timeSection">
        <time class="timeSection__time">{{ date }}</time>
        <p class="timeSection__seperator"></p>
        <p class="timeSection__para">{{ content }}</p>
      </div>
      <ul class="headerText__dotList">
        <li class="headerText__dotListItem headerText--big">.</li>
        <li class="headerText__dotListItem">.</li>
        <li class="headerText__dotListItem">.</li>
      </ul>
    </div>
   </div>
  </header>
  <Modal>
    <LoginModal/>
    <RegisterModal/>
  </Modal>
  
  
</template>

<script setup>
import LoginModal from "../modals/LoginModal.vue"
import { useModalStore } from "@/store/modalStore";
import RegisterModal from "../modals/RegisterModal.vue"
import Modal from '../modals/Modal.vue'

defineProps(
        ['title', 'content', 'date', 'headerBackgroundImage']
    )

    const modalStore = useModalStore();


</script>