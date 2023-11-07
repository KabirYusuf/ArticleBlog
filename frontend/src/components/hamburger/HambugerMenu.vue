<template>
    <div  :class="[isMenuOpen ? 'open' : 'close', 'hamburger__container']">
      <div v-if="!isMenuOpen" class="nav__logoContainer">
      <a class="nav__logoLink" href="#">RUNO</a>
    </div>
      <button class="hamburger" @click="toggleMenu">
        <span class="hamburger__line"></span>
        <span class="hamburger__line"></span>
        <span class="hamburger__line"></span>
      </button>
      <ul v-if="isMenuOpen" class="nav__list">
        <li class="nav__listItem"><a class="nav__listLink" href="#">Home</a></li>
        <li class="nav__listItem"><a class="nav__listLink" href="#">About</a></li>
        <li class="nav__listItem"><a class="nav__listLink" href="#">Article</a></li>
        <li class="nav__listItem"><a class="nav__listLink" href="#">Sign in</a></li>
      </ul>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  
  const isMenuOpen = ref(false);
  
  function toggleMenu() {
    isMenuOpen.value = !isMenuOpen.value;
  }
  </script>
  
  <style scoped>
  .hamburger__container{
    position: fixed;
    width: 100vw;
    z-index: 10;
    height: 100vh;
    
    @media only screen and (min-width: 414px) {
     display: none;
    }
  }
  .nav__logoContainer{
    position: absolute;
    top: 20px;
    left: 30px;
  }
  
  .hamburger {
    position: absolute;
    display: none;
    flex-direction: column;
    justify-content: space-around;
    width: 2rem;
    height: 2rem;
    background: transparent;
    border: none;
    cursor: pointer;
    z-index: 20;
    top: 20px;
    right: 30px;

    @media only screen and (max-width: 414px) {
      display: flex;
    }
  }
  .open{
    background-color: black;
    @media only screen and (min-width: 414px) {
      background-color: transparent;
    }
  }
  
  .hamburger:focus {
    outline: none;
  }
  
  .hamburger__line {
    display: block;
    width: 2rem;
    height: 0.25rem;
    background: #FFFFFF;
    border-radius: 10px;
    position: relative;
  }
    
  .nav__list {
    flex-direction: column;
  }

  .nav__listItem {
    padding: 20px 20px;
  }
  
  
  .nav__listLink {
   color: white;
   font-size: 30px;
  }
  </style>
  