<script setup>
import { RouterView } from "vue-router";
</script>

<template>
  <header class="header">
    <div class="header__overlay">
   <nav class="nav">
    <div class="nav__logoContainer">
      <a class="nav__logoLink" href="#">RUNO</a>
    </div>
    <ul class="nav__list">
      <li class="nav__listItem nav_listItem--pushRight"><a class="nav__listLink" href="#">Home</a></li>
      <li class="nav__listItem"><a class="nav__listLink" href="#">About</a></li>
      <li class="nav__listItem"><a class="nav__listLink" href="#">Article</a></li>
      <li class="nav__listItem"><a class="nav__listLink" href="#">Sign in</a></li>
    </ul>
   </nav>
    </div>

   <div class="headerText">
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
  </header>

  <main>
    <RouterView />
  </main>
  <footer class="footer">
    <section class="footer__inner footerInner--top">
      <div class="footerTop__contact">
        <h4 class="footerTop__header">Contact the Publisher</h4>
        <p class="footerTop__content footerTop--email">mike@runo.com</p>
        <p class="footerTop__content footerTop--number">+944 450 904 505</p>
      </div>
      <div class="footerTop__explorate">
        <h4 class="footerTop__header">Explorate</h4>
        <ul class="explorate__innerList">
          <li class="explorate__innerListItem"><a class="explorate__innerLink" href="#">About</a></li>
          <li class="explorate__innerListItem"><a class="explorate__innerLink" href="#">Partners</a></li>
          <li class="explorate__innerListItem"><a class="explorate__innerLink" href="#">Job Opportunities</a></li>
          <li class="explorate__innerListItem"><a class="explorate__innerLink" href="#">Advertise</a></li>
          <li class="explorate__innerListItem"><a class="explorate__innerLink" href="#">Membership</a></li>
        </ul>
      </div>
      <div class="footerTop__headquaters">
        <h4 class="footerTop__header">Headquarter</h4>
        <p class="footerTop__content footerContent--Para">191 Middleville Road, NY 1001, Sydney Australia</p>
      </div>
      <div class="footerTop__headquarter">
        <h4 class="footerTop__header">Headquarter</h4>
        <p class="footerTop__content footerContent--Para">191 Middleville Road, NY 1001, Sydney Australia</p>
      </div>
    </section>
    <section class="footer__inner footerInner--bottom">
      <p class="footerBottom__inner">2021 | RUNO Publisher Studio</p>
      <p class="footerBottom__inner"><a class="footerBottom__innerLink" href="#">Subscribe Now</a></p>
    </section>
  </footer>
</template>

<style lang="scss">
@import "@/assets/main.scss";
</style>
