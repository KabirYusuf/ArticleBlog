<template>
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
      <div v-if="!isConnection" class="footerTop__headquarter">
        <h4 class="footerTop__header">Headquarter</h4>
        <p class="footerTop__content footerContent--Para">191 Middleville Road, NY 1001, Sydney Australia</p>
      </div>
      <div v-if="isConnection" class="footerTop__headquarter">
        <h4 class="footerTop__header">Connection</h4>
        <i class="fab fa-facebook"></i>
        <i class="fab fa-twitter"></i>
        <i class="fab fa-instagram"></i>
        <i class="fab fa-youtube"></i>
        <i class="fab fa-twitter"></i>
      </div>
    </section>
    <div class="footerBottom__container">
      <div class="footer__inner footerInner--bottom">
      <p class="footerBottom__inner">2021 | RUNO Publisher Studio</p>
      <p class="footerBottom__inner"><a class="footerBottom__innerLink" href="#">Subscribe Now</a></p>
      </div>
    </div>
  </footer>
</template>

<script setup>

defineProps(['isConnection'])
</script>