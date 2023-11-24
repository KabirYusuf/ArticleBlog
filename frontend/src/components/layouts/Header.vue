<template>
    <header class="header">
      <HambugerMenu/>
    <div class="header__overlay">
   <nav class="nav">
    <div class="nav__logoContainer">
      <a class="nav__logoLink" href="#">RUNO</a>
    </div>
    <ul class="nav__list">
      <li class="nav__listItem nav_listItem--pushRight"><a class="nav__listLink" href="#">Home</a></li>
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
      <p class="headerText__para">Richird Norton photorealistic rendering as real photos</p>

      <div class="timeSection">
        <time class="timeSection__time">08:08:2021</time>
        <p class="timeSection__seperator"></p>
        <p class="timeSection__para">Progressively incentivize coperative system through technically sound
          functionalities. The credibly productivate seamless data
        </p>
      </div>
      <ul class="headerText__dotList">
        <li class="headerText__dotListItem headerText--big">.</li>
        <li class="headerText__dotListItem">.</li>
        <li class="headerText__dotListItem">.</li>
      </ul>
    </div>
   </div>
  </header>
  <!-- <RegisterModal/>
  <LoginModal/> -->
  <Modal>
    <LoginModal/>
    <RegisterModal/>
  </Modal>
  
  
</template>

<script setup>
// import Modal from "../modals/Modal.vue";
import LoginModal from "../modals/LoginModal.vue"
import { useModalStore } from "@/store/modalStore";
import RegisterModal from "../modals/RegisterModal.vue"
import Modal from '../modals/Modal.vue'

    const modalStore = useModalStore();


</script>