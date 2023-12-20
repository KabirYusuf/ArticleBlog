<template>
    <div class="form__control">

        <div class="form__left">
            <slot/>
        </div>

        <div class="vertical-line"></div>

        <div class="form__right">
            <SocialButton/>
        </div>
    </div>
</template>

<script setup>
import SocialButton from "@/components/socials/SocialButton.vue";
</script>
