<template>

<div class="header__overlay">
    <nav class="nav">
        <div class="nav__logoContainer">
          <a class="nav__logoLink" href="#">RUNO</a>
        </div>
        <ul class="nav__list">
          <li class="nav__listItem nav_listItem--pushRight">
            <router-link to="/" class="nav__listLink">Home</router-link>
          </li>
          <li class="nav__listItem"><a class="nav__listLink" href="#">About</a></li>
          <li class="nav__listItem"><a class="nav__listLink" href="#">Article</a></li>
          <li class="nav__listItem" @click="modalStore.openModal('login')"><a class="nav__listLink" href="#">Sign in</a>
          </li>
          <li class="nav__listItem" @click="modalStore.openModal('register')"><a class="nav__listLink"
              href="#">Register</a></li>
        </ul>
      </nav>
</div>
</template>

<script setup>

</script>