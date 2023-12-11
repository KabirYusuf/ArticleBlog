<template>
  <HambugerMenu />
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
        <li v-if="!userStore.isLoggedIn" class="nav__listItem" @click="modalStore.openModal('login')"><a
            class="nav__listLink" href="#">Sign in</a>
        </li>
        <li v-if="!userStore.isLoggedIn" class="nav__listItem" @click="modalStore.openModal('register')"><a
            class="nav__listLink" href="#">Register</a></li>

        <li v-if="userStore.isLoggedIn" class="nav__listItem">
          <router-link to="/my-profile" class="nav__listLink">My profile</router-link>
        </li>

        <li v-if="userStore.isLoggedIn" class="nav__listItem" @click="handleLogout">
          <a class="nav__listLink" href="#">Logout</a>
        </li>
      </ul>

    </nav>
  </div>

  <Modal v-if="modalStore.showLogin">
    <LoginModal />
  </Modal>

  <Modal v-if="modalStore.showRegister">
    <RegisterModal />
  </Modal>
</template>

<script setup>
import LoginModal from "@/components/modals/LoginModal.vue"
import RegisterModal from "@/components/modals/RegisterModal.vue"
import Modal from '@/components/modals/Modal.vue'
import { useModalStore } from "@/store/modalStore";
import HambugerMenu from "../hamburger/HambugerMenu.vue";
import { useUserStore } from "../../store/userStore";
import { useRouter } from 'vue-router';


const modalStore = useModalStore();
const userStore = useUserStore();
const router = useRouter();

const handleLogout = () => {
  userStore.logOut();
  router.push({ name: 'home' });
};
</script>